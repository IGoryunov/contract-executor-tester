import com.credits.client.node.pojo.SmartContractData
import com.credits.client.node.pojo.SmartContractDeployData
import com.credits.client.node.pojo.TokenStandartData.NotAToken
import com.credits.general.exception.CompilationErrorException
import com.credits.general.pojo.ByteCodeObjectData
import com.credits.general.util.GeneralConverter.decodeFromBASE58
import com.credits.general.util.GeneralConverter.encodeToBASE58
import com.credits.general.util.compiler.InMemoryCompiler.compileSourceCode
import java.io.File
import java.io.File.separator
import java.io.FileNotFoundException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

fun async(amountThreads: Int = 10, timeout: Long = 30, run: () -> Unit) {
    if (amountThreads < 0) {
        val executorService = Executors.newFixedThreadPool(100)
        while (true) {
//            CompletableFuture.runAsync(Runnable { run() }, executorService)
            executorService.execute { run() }
        }
    }
    val countDownLatch = CountDownLatch(amountThreads)
    for (i in 1..amountThreads) {
        thread(start = true) {
            run()
            countDownLatch.countDown()
        }
    }
    countDownLatch.await(timeout, TimeUnit.SECONDS)
}


fun loadAllContractsInFolder(contractsFolderPath: String, debugInfo: Boolean = false): List<SmartContractData> {
    val contracts = mutableListOf<SmartContractData>()
    for (contractFolder in File(contractsFolderPath).listFiles()
            ?: throw FileNotFoundException("Contracts folder \"$contractsFolderPath\" not found")) {
        contracts.add(loadContractFromDisk(contractFolder.absolutePath, debugInfo))
    }
    return contracts
}

fun loadContractFromDisk(contractFolder: String, debugInfo: Boolean = false): SmartContractData {
    if (!File(contractFolder).exists()) throw FileNotFoundException("contract folder not found $contractFolder")

    val address = decodeFromBASE58(contractFolder.substringAfterLast(separator))
    val sourcecode = File(contractFolder).walkTopDown().filter { file -> file.extension == "java" }.firstOrNull()?.readText()
    val byteCodeObjects = sourcecode?.let {
        try {
            compileSourceCode(sourcecode).units.map { ByteCodeObjectData(it.name, it.byteCode) }
        } catch (e: CompilationErrorException) {
            println("warning: can't compile contract $contractFolder")
            if (debugInfo) e.errors.forEach { error -> println("Error on line ${error.lineNumber}: ${error.errorMessage}") }
            return@let null
        }
    }
    var state: ByteArray? = null
    File(contractFolder + separator + "state.bin").let {
        if (it.exists()) {
            state = readFromFile(it.absolutePath)
            if (debugInfo) println("state for contract $contractFolder loaded with hash - ${state?.hashCode()}")
        }
    }
    return SmartContractData(
            address,
            byteArrayOf(),
            SmartContractDeployData(sourcecode, byteCodeObjects, NotAToken),
            state
    )
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

