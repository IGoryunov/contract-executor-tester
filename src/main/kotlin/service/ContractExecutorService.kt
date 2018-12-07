package service

import com.credits.client.node.pojo.SmartContractData
import com.credits.general.util.GeneralConverter
import saveContractStateOnDisk
import writeToFile
import java.io.File.separator
import java.util.stream.Collectors

class ContractExecutorService(
    private val contractsFolder: String,
    private val selectedContractData: SmartContractData
) {
    //    private val selectedContractData = loadContractsFromDisk(contractsFolder)[selectContractIndex]
    private val client = ContractExecutorThriftClient("localhost", 9080)

    fun executeMethod(args: List<String>) {
        val methodName = args[0]

        //todo add converter from variant
        with(selectedContractData) {
            client.executeByteCode(address, smartContractDeployData.byteCode, objectState, methodName, listOf(), 500)
                .let { result ->
                    println("smart contract method execute result: $result")
                    result.getContractState()?.let { state ->
                        selectedContractData.objectState = state
                        saveContractStateOnDisk(selectedContractData, contractsFolder)
                    }
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

        //todo add convert to variant
        with(selectedContractData) {
            client.executeByteCodeMultiple(
                address,
                smartContractDeployData.byteCode,
                objectState,
                methodName,
                listOf(),
                500
            ).let { result -> println("smart contract method execute result: $result") }
        }
    }

    fun getContractMethods() {
        client.getContractMethods(selectedContractData.smartContractDeployData.byteCode).getMethods()
            ?.forEach { method ->
                println(
                    "${method.returnType} ${method.name}(${method.arguments.stream().map { arg -> "${arg.getType()} ${arg.getName()}" }.collect(
                        Collectors.joining(", ")
                    )})"
                )
            }
    }

    fun getContractVariables() {
        with(selectedContractData) {
            if (objectState == null) throw IllegalArgumentException("contractState not found, you have to executeByteCode method previous")
            client.getContractVariables(
                selectedContractData.smartContractDeployData.byteCode,
                objectState
            ).getContractVariables()?.forEach {
                println(it)
            }
        }
    }

    fun compileSourceCode(): ByteArray =
        with(client.compileSourceCode(selectedContractData.smartContractDeployData.sourceCode)) {
            println(this)
            writeToFile(
                "$contractsFolder$separator${GeneralConverter.encodeToBASE58(selectedContractData.address)}${separator}bytecode.bin",
                getByteCode()
            )
            getByteCode()
        }


}