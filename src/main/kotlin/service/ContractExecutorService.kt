package service

import loadContractsFromDisk
import saveContractStateOnDisk
import java.io.File.separator
import java.nio.ByteBuffer.wrap

class ContractExecutorService(private val contractsFolder: String, selectContractIndex: Int = 0) {
    private val selectedContractData = loadContractsFromDisk(contractsFolder)[selectContractIndex]
    private val client = ContractExecutorThriftClient("localhost", 9080)

    fun executeMethod(args: List<String>) {
        val methodName = args[0]
        val params = if (args.size > 1) args.subList(1, args.size) else listOf()

        client.executeMethod(selectedContractData, methodName, params).let { result ->
            println("smart contract method execute result: ${result.getRet_val()}")
            result.getContractState()?.let { state ->
                selectedContractData.contractState = state
                saveContractStateOnDisk(selectedContractData, contractsFolder)
            }
        }
    }

    fun getContractMethods() {
        client.getContractMethods(wrap(selectedContractData.byteCode)).getMethods()?.forEach {
            println(it)
        }
    }

    fun getContractVariables() {
        if (selectedContractData.contractState == null) throw IllegalArgumentException("contractState not found, you have to executeByteCode method previous")
        client.getContractVariables(wrap(selectedContractData.byteCode), wrap(selectedContractData.contractState)).getContractVariables()?.forEach {
            println(it)
        }
    }

    fun compileSourceCode() {
        client.compileSourceCode(selectedContractData.sourceCode, "$contractsFolder$separator${selectedContractData.address}${separator}bytecode.bin")
    }
}