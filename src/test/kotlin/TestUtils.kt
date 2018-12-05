import com.credits.leveldb.client.data.SmartContractData
import compiler.CompilationException
import compiler.SimpleInMemoryCompilator
import utils.Utils
import java.io.File
import java.io.FileNotFoundException

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