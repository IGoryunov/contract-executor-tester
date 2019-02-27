package service

import com.credits.client.executor.thrift.generated.ContractExecutor
import com.credits.client.executor.thrift.generated.SmartContractBinary
import com.credits.client.node.pojo.SmartContractData
import com.credits.general.thrift.ThriftClientPool
import com.credits.general.thrift.ThriftClientPool.ClientFactory
import com.credits.general.util.ByteArrayContractClassLoader
import com.credits.general.util.GeneralConverter.byteCodeObjectsDataToByteCodeObjects
import com.credits.general.util.GeneralConverter.encodeToBASE58
import com.credits.general.util.variant.VariantConverter.variantDataToVariant
import com.credits.general.util.variant.VariantUtils.createVariantData
import saveContractStateOnDisk
import writeToFile
import java.io.File.separator
import java.nio.ByteBuffer
import java.util.stream.Collectors
import java.util.stream.IntStream
import kotlin.streams.toList

class ContractExecutorService(
        private val contractsFolder: String,
        private val selectedContractData: SmartContractData
) {
    private val thriftPool: ThriftClientPool<ContractExecutor.Client>

    init {
        thriftPool = ThriftClientPool<ContractExecutor.Client>(ClientFactory { tProtocol -> ContractExecutor.Client(tProtocol) }, "127.0.0.1", 9080)
    }

    fun executeMethod(args: List<String>) {
        val methodName = args[0]
        val types: List<String> = getMethodTypes(methodName)
        val values: List<String> = args.subList(1, args.size)

        val params = IntStream.range(0, types.size).mapToObj { i ->
            variantDataToVariant(createVariantData(types[i], values[i]))
        }.toList()

        with(selectedContractData) {
            println("executing $methodName...")
            thriftPool.useClient { client ->
                client.executeByteCode(
                        System.currentTimeMillis(),
                        ByteBuffer.wrap(address),
                        SmartContractBinary(
                                ByteBuffer.wrap(address),
                                byteCodeObjectsDataToByteCodeObjects(smartContractDeployData.byteCodeObjects),
                                ByteBuffer.wrap(if (objectState != null) objectState else byteArrayOf()),
                                true),
                        methodName,
                        params,
                        30_000
                ).let { result ->
                    println("smart contract method execute result: $result")
                    result.getInvokedContractState()?.let { state ->
                        objectState = state

                        synchronized(ContractExecutorService::class) {
                            saveContractStateOnDisk(selectedContractData, contractsFolder)
                        }
                    }
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

        /*
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
        */
    }

    fun getContractMethods() {
        thriftPool.useClient { client ->
            client.getContractMethods(byteCodeObjectsDataToByteCodeObjects(selectedContractData.smartContractDeployData.byteCodeObjects))
                    .getMethods()
                    ?.forEach { method ->
                        println(
                                "${method.returnType} ${method.name}(${method.arguments.stream().map { arg -> "${arg.getType()} ${arg.getName()}" }
                                        .collect(Collectors.joining(", "))})"
                        )
                    }
        }
    }

    fun getContractVariables() {
//        with(selectedContractData) {
//            if (objectState == null) throw IllegalArgumentException("contractState not found, you have to executeByteCode method previous")
//            client.getContractVariables(
//                    selectedContractData.smartContractDeployData.byteCode,
//                    objectState
//            ).getContractVariables()?.forEach {
//                println(it)
//            }
//        }
    }

    fun compileSourceCode() =
            thriftPool.useClient { client ->
                with(client.compileSourceCode(selectedContractData.smartContractDeployData.sourceCode)) {
                    println(this)
                    getByteCodeObjects().apply {
                        forEach {
                            writeToFile("$contractsFolder$separator${encodeToBASE58(selectedContractData.address)}$separator${it.name}.bin",
                                    it.getByteCode())
                        }
                    }
                }
            }


    private inline fun ThriftClientPool<ContractExecutor.Client>.useClient(block: (ContractExecutor.Client) -> Unit) {
        var client: ContractExecutor.Client
        synchronized(this) {
            client = resource
        }
        block(client)
        synchronized(this) {
            returnResource(client)
        }
    }

    private fun getMethodTypes(methodName: String): List<String> {
        val byteCodeObjectData = selectedContractData.smartContractDeployData.byteCodeObjects[0]
        val contractClass = ByteArrayContractClassLoader().buildClass(byteCodeObjectData.name, byteCodeObjectData.byteCode)
        return contractClass.methods.filter { it.name == methodName }[0].parameterTypes
                .map { parameter -> parameter.name.split(".").last() }
                .toList()
    }

}