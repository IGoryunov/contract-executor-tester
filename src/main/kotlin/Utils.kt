import com.credits.client.node.pojo.SmartContractData
import com.credits.client.node.pojo.SmartContractDeployData
import com.credits.client.node.pojo.TokenStandartData.NotAToken
import com.credits.general.pojo.ByteCodeObjectData
import com.credits.general.util.GeneralConverter.decodeFromBASE58
import com.credits.general.util.GeneralConverter.encodeToBASE58
import com.credits.general.util.compiler.CompilationException
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


fun loadAllContractsInFolder(contractsFolderPath: String, debugInfo: Boolean = false, loadByteCode: Boolean = false): List<SmartContractData> {
    val contracts = mutableListOf<SmartContractData>()
    for (contractFolder in File(contractsFolderPath).listFiles()
            ?: throw FileNotFoundException("Contracts folder \"$contractsFolderPath\" not found")) {
        contracts.add(loadContractFromDisk(contractFolder.absolutePath, debugInfo, loadByteCode))
    }
    return contracts
}

fun filterContractByFolderName(folderName: String, contracts: List<SmartContractData>): SmartContractData? =
        contracts.firstOrNull { smartContractData -> encodeToBASE58(smartContractData.address).startsWith(folderName) }


fun loadContractFromDisk(contractFolder: String, debugInfo: Boolean = false, loadByteCode: Boolean = false): SmartContractData {
    if (!File(contractFolder).exists()) throw FileNotFoundException("contract folder not found $contractFolder")

    val address = decodeFromBASE58(contractFolder.substringAfterLast(separator))
    val sourcecode = File(contractFolder).walkTopDown().filter { file -> file.extension == "java" }.firstOrNull()?.readText()
    val byteCodeObjects = if (loadByteCode) {
        loadByteCodeObjectDataFromFolder(contractFolder)
    } else {
        sourcecode?.let {
            try {
                compileSourceCode(sourcecode).units.map { ByteCodeObjectData(it.name, it.byteCode) }
            } catch (e: CompilationException) {
                println("warning: can't compile contract $contractFolder")
                if (debugInfo) println(e.message)
                return@let null
            }
        }
    }
    var state: ByteArray? = null
    File(contractFolder + separator + "object.state").let {
        if (it.exists()) {
            state = readFromFile(it.absolutePath)
            if (debugInfo) println("state for contract $contractFolder loaded with hash - ${state?.hashCode()}")
        }
    }
    return SmartContractData(
            address,
            byteArrayOf(),
            SmartContractDeployData(sourcecode, byteCodeObjects, NotAToken),
            state,
            1
    )
}

private fun loadByteCodeObjectDataFromFolder(contractFolder: String): List<ByteCodeObjectData> {
    return File(contractFolder).walkTopDown()
            .filter { file -> file.extension == "bin" }
            .map { file ->
                ByteCodeObjectData(file.nameWithoutExtension, file.readBytes())
                        .also { println("load bytecode from disk: ${it.name} ${it.byteCode.size} bytes") }
            }
            .toList()
}

fun saveContractStateOnDisk(smartContractData: SmartContractData, contractsFolderPath: String) =
        with(smartContractData) {
            writeToFile("$contractsFolderPath$separator${encodeToBASE58(address)}${separator}object.state", objectState)
        }

fun writeToFile(fileName: String, bytes: ByteArray) =
        File(fileName).outputStream().use { file ->
            file.write(bytes)
        }


fun readFromFile(fileName: String): ByteArray =
        File(fileName).inputStream().use { file ->
            return file.readBytes()
        }

