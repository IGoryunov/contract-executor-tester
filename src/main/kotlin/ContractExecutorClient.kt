import com.credits.leveldb.client.data.SmartContractData
import com.credits.thrift.generated.ContractExecutor
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.TSocket
import java.nio.ByteBuffer
import java.nio.ByteBuffer.wrap

class ContractExecutorClient {

    private val transport = TSocket("localhost", 9080)
    private val smartContractData = loadContractsFromDisk(Options.contractsFolder)[0]

    fun executeMethod(args: List<String>) {
        val methodName = args[0]
        val params = if (args.size > 1) args.subList(1, args.size) else listOf()

        executeMethod(smartContractData, methodName, params)?.let { result ->
            println("smart contract method execute result: ${result.getRet_val()}")
            result.getContractState()?.let { state ->
                smartContractData.contractState = state
                saveContractStateOnDisk(smartContractData, Options.contractsFolder)
            }
        }
    }

    fun getContractMethods() {
        getContractMethods(wrap(smartContractData.byteCode))?.getMethods()?.forEach {
            println(it)
        }
    }

    fun getContractVariables() {
        if (smartContractData.contractState == null) throw IllegalArgumentException("contractState not found, you have to executeByteCode method previous")
        getContractVariables(wrap(smartContractData.byteCode), wrap(smartContractData.contractState))?.getContractVariables()?.forEach {
            println(it)
        }
    }

    private fun getContractMethods(bytecode: ByteBuffer) = transport.call { client ->
        client.getContractMethods(bytecode)
    }

    private fun getContractVariables(bytecode: ByteBuffer, contractState: ByteBuffer) = transport.call { client ->
        client.getContractVariables(bytecode, contractState)
    }


    private fun executeMethod(smartContractData: SmartContractData, methodName: String, params: List<String> = mutableListOf(), executionTime: Long = 5000) = transport.call { client ->
        client.executeByteCode(
                wrap(byteArrayOf(0x1)),              //todo add correct address converter
                wrap(smartContractData.byteCode),
                if (smartContractData.contractState != null) wrap(smartContractData.contractState) else wrap(byteArrayOf()),
                methodName,
                params,
                executionTime)
    }

    private fun <R> TSocket.call(body: (ContractExecutor.Client) -> R): R {
        use {
            open()
            val result = body(ContractExecutor.Client(TBinaryProtocol(this)))
            close()
            return result
        }
    }
}
