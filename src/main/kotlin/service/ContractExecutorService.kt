package service

import loadContractsFromDisk
import saveContractStateOnDisk

class ContractExecutorService(private val contractsFolder: String, selectContractIndex: Int = 0) {
    private val selectedContractData = loadContractsFromDisk(contractsFolder)[selectContractIndex]
    private val client = ContractExecutorThriftClient("localhost", 9080)

    fun executeMethod(args: List<String>) {
        val methodName = args[0]
//        val params = if (args.size > 1) args.subList(1, args.size) else listOf()

        //todo add variant converter
        client.executeMethod(selectedContractData, methodName, listOf()).let { result ->
            println("smart contract method execute result: $result")
            result.getContractState()?.let { state ->
                selectedContractData.objectState = state
                saveContractStateOnDisk(selectedContractData, contractsFolder)
            }
        }
    }
}