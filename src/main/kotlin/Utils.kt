import com.credits.client.node.pojo.SmartContractData
import com.credits.client.node.pojo.SmartContractDeployData
import com.credits.client.node.thrift.generated.TokenStandart.CreditsExtended
import com.credits.general.util.GeneralConverter.decodeFromBASE58
import com.credits.general.util.GeneralConverter.encodeToBASE58
import compiler.CompilationException
import compiler.SimpleInMemoryCompilator.compile
import java.io.File
import java.io.File.separator
import java.io.FileNotFoundException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

fun <T : Any> async(amountThreads: Int = 10, timeout: Long = 30, monitor: T, run: () -> Unit) {
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


fun loadContractsFromDisk(contractsFolderPath: String): List<SmartContractData> {
    val contracts = mutableListOf<SmartContractData>()
    for (contractFolder in File(contractsFolderPath).listFiles()
        ?: throw FileNotFoundException("Contracts folder \"$contractsFolderPath\" not found")) {
        val address = decodeFromBASE58(contractFolder.name)
        val sourcecode =
            contractFolder.walkTopDown().filter { file -> file.nameWithoutExtension == "Contract" }.firstOrNull()
                ?.readText()
        val bytecode = sourcecode?.let {
            try {
                compile(sourcecode, "Contract")
            } catch (e: CompilationException) {
                println("warning: can't compile contract ${contractFolder.name}")
                return@let null
            }
        }
        var state: ByteArray? = null
        File(contractsFolderPath + separator + address + separator + "state.bin").let {
            if (it.exists()) state = readFromFile(it.absolutePath)
        }
        contracts.add(
            SmartContractData(
                address,
                byteArrayOf(),
                SmartContractDeployData(sourcecode, bytecode, CreditsExtended),
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

