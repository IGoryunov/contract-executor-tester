import com.credits.leveldb.client.data.SmartContractData
import compiler.SimpleInMemoryCompilator.compile
import utils.Utils.encrypt
import java.io.File
import java.io.File.separator
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

fun <T: Any> async(amountThreads: Int = 10, timeout: Long = 30, monitor: T, run: () -> Unit) {
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
    File(contractsFolderPath).walkTopDown().filterIndexed { index, _ -> index == 1 }.forEach { folder ->
        val address = folder.name
        val sourcecode = folder.walkTopDown().filterIndexed { index, _ -> index == 1 }.firstOrNull()?.readText()
        val bytecode = sourcecode?.let { compile(sourcecode, "Contract") }
        var state :ByteArray? = null
        File(contractsFolderPath+ separator + address + separator + "state.bin").let {
            if(it.exists()) state = readFromFile(it.absolutePath)
        }
        contracts.add(SmartContractData(address, sourcecode, bytecode, state, encrypt(bytecode)))
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
