import com.credits.client.node.pojo.SmartContractData
import com.credits.client.node.pojo.SmartContractDeployData
import com.credits.client.node.pojo.TokenStandartData.NotAToken
import com.credits.general.pojo.ByteCodeObjectData
import com.credits.general.util.GeneralConverter.decodeFromBASE58
import com.credits.general.util.GeneralConverter.encodeToBASE58
import com.credits.general.util.compiler.InMemoryCompiler.compileSourceCode
import compiler.CompilationException
import java.io.File
import java.io.File.separator
import java.io.FileNotFoundException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

fun <T : Any> async(amountThreads: Int = 10, timeout: Long = 30, monitor: T, run: () -> Unit) {
    if (amountThreads < 0) {
        val executorService = Executors.newFixedThreadPool(100)
        while (true) {
//            CompletableFuture.runAsync(Runnable { run() }, executorService)
            executorService.execute { run() }
        }
    }
    val countDownLatch = CountDownLatch(amountThreads)
    val threads = Array(amountThreads) {
        thread {
            synchronized(monitor) {
                run()
                countDownLatch.countDown()
            }
        }
    }

    threads.forEach { if (!it.isAlive) it.start() }
    countDownLatch.await(timeout, TimeUnit.SECONDS)
}


fun loadContractsFromDisk(contractsFolderPath: String, debugInfo: Boolean = false): List<SmartContractData> {
    val contracts = mutableListOf<SmartContractData>()
    for (contractFolder in File(contractsFolderPath).listFiles()
            ?: throw FileNotFoundException("Contracts folder \"$contractsFolderPath\" not found")) {
        val address = decodeFromBASE58(contractFolder.name)
        val sourcecode =
                contractFolder.walkTopDown().filter { file -> file.nameWithoutExtension == "Contract" }.firstOrNull()
                        ?.readText()
        val byteCodeObjects = sourcecode?.let {
            try {
                compileSourceCode(sourcecode).units.map { ByteCodeObjectData(it.name, it.byteCode) }
            } catch (e: CompilationException) {
                println("warning: can't compile contract ${contractFolder.name}")
                if (debugInfo) println(e.message)
                return@let null
            }
        }
        var state: ByteArray? = null
        File(contractFolder.absolutePath + separator + "state.bin").let {
            if (it.exists()) {
                state = readFromFile(it.absolutePath)
                if (debugInfo) println("state for contract ${contractFolder.name} loaded with hash - ${state?.hashCode()}")
            }
        }
        contracts.add(
                SmartContractData(
                        address,
                        byteArrayOf(),
                        SmartContractDeployData(sourcecode, byteCodeObjects, NotAToken),
                        state
                )
        )
    }
    return contracts
}

fun saveContractStateOnDisk(smartContractData: SmartContractData, contractsFolderPath: String) =
        with(smartContractData) {
            writeToFile("$contractsFolderPath$separator${encodeToBASE58(address)}${separator}state.bin", objectState)
        }

fun writeToFile(fileName: String, bytes: ByteArray) =
        File(fileName).outputStream().use { file ->
            file.write(bytes)
        }


fun readFromFile(fileName: String): ByteArray =
        File(fileName).inputStream().use { file ->
            return file.readBytes()
        }

