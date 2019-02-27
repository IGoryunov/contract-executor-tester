
import com.credits.client.node.pojo.SmartContractData


fun loadContractsFromDisk(contractsFolderPath: String): List<SmartContractData> {
    val contracts = mutableListOf<SmartContractData>()
//    for (contractFolder in File(contractsFolderPath).listFiles()
//        ?: throw FileNotFoundException("Contracts folder \"$contractsFolderPath\" not found")) {
//        val address = decodeFromBASE58(contractFolder.name)
//        val sourcecode =
//            contractFolder.walkTopDown().filter { file -> file.nameWithoutExtension == "Contract" }.firstOrNull()
//                ?.readText()
//        val bytecode = sourcecode?.let {
//            try {
//                compile(sourcecode, "Contract")
//            } catch (e: CompilationException) {
//                println("warning: can't compile contract ${contractFolder.name}")
//                return@let null
//            }
//        }
//        var state: ByteArray? = null
//        File(contractsFolderPath + separator + contractFolder.name + separator + "state.bin").let {
//            if (it.exists()) state = readFromFile(it.absolutePath)
//        }
//        contracts.add(
//            SmartContractData(
//                address,
//                byteArrayOf(),
//                SmartContractDeployData(sourcecode, bytecode, CreditsExtended),
//                state
//            )
//        )
//    }
    return contracts
}
