package service

import loadContractsFromDisk
import saveContractStateOnDisk
import writeToFile
import java.io.File.separator
import java.nio.ByteBuffer.wrap
import java.util.stream.Collectors

class ContractExecutorService(private val contractsFolder: String, selectContractIndex: Int = 0) {
    private val selectedContractData = loadContractsFromDisk(contractsFolder)[selectContractIndex]
    private val client = ContractExecutorThriftClient("localhost", 9080)

    fun executeMethod(args: List<String>) {
        val methodName = args[0]
        val params = if (args.size > 1) args.subList(1, args.size) else listOf()

        client.executeMethod(selectedContractData, methodName, params).let { result ->
            println("smart contract method execute result: $result")
            result.getContractState()?.let { state ->
                selectedContractData.contractState = state
                saveContractStateOnDisk(selectedContractData, contractsFolder)
            }
        }
    }

    fun executeMultipleMethod(args: List<String>) {
        val methodName = args[0]
        val params: List<List<String>> = if (args.size > 1) {
            val tableArgs = args.subList(1, args.size)
            val rowArgs = mutableListOf<List<String>>()
            tableArgs.forEach { row -> rowArgs.add(row.split(";").toList()) }
            rowArgs

        } else listOf()

        println("smart contract method execute result: ${client.executeMultiple(selectedContractData, methodName, params).getResults()}")
    }

    fun getContractMethods() {
        client.getContractMethods(wrap(selectedContractData.byteCode)).getMethods()?.forEach { method ->
            println("${method.returnType} ${method.name}(${method.arguments.stream().map { arg -> "${arg.getType()} ${arg.getName()}" }.collect(Collectors.joining(", "))})")
        }
    }

    fun getContractVariables() {
        if (selectedContractData.contractState == null) throw IllegalArgumentException("contractState not found, you have to executeByteCode method previous")
        client.getContractVariables(wrap(selectedContractData.byteCode), wrap(selectedContractData.contractState)).getContractVariables()?.forEach {
            println(it)
        }
    }

    fun compileSourceCode(): ByteArray = with(client.compileSourceCode(selectedContractData.sourceCode)) {
        writeToFile("$contractsFolder$separator${selectedContractData.address}${separator}bytecode.bin", getBytecode())
        getBytecode()
    }


}