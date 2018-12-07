
import com.credits.leveldb.client.data.SmartContractData
import compiler.CompilationException
import compiler.SimpleInMemoryCompilator
import utils.Utils
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
        val address = contractFolder.name
        val sourcecode = contractFolder.walkTopDown().filter { file -> file.nameWithoutExtension == "Contract" }.firstOrNull()?.readText()
        val bytecode = sourcecode?.let {
            try {
                SimpleInMemoryCompilator.compile(sourcecode, "Contract")
            } catch (e: CompilationException){
                println("Can't compile contract $address. Reason:${e.localizedMessage}")
                return@let null
            }
        }
        var state: ByteArray? = null
        File(contractsFolderPath + File.separator + address + File.separator + "state.bin").let {
            if (it.exists()) state = readFromFile(it.absolutePath)
        }
        contracts.add(SmartContractData(address, sourcecode, bytecode, state, Utils.encrypt(bytecode ?: byteArrayOf())))
    }
    return contracts
}

fun saveContractStateOnDisk(smartContractData: SmartContractData, contractsFolderPath: String) {
    writeToFile(contractsFolderPath + separator + smartContractData.address + separator + "state.bin", smartContractData.contractState)
}

fun writeToFile(fileName: String, bytes: ByteArray) {
    File(fileName).outputStream().use { file ->
        file.write(bytes)
    }
}

fun readFromFile(fileName: String): ByteArray {
    File(fileName).inputStream().use { file ->
        return file.readBytes()
    }
}
