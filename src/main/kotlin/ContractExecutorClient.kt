import com.credits.leveldb.client.data.SmartContractData
import com.credits.thrift.generated.ContractExecutor
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.TSocket
import java.io.File.separator
import java.nio.ByteBuffer
import java.nio.ByteBuffer.wrap

class ContractExecutorClient(private val contractsFolder: String, selectContractIndex: Int = 0) {

    private val transport = TSocket("localhost", 9080)
    private val selectedContractData = loadContractsFromDisk(contractsFolder)[selectContractIndex]

    fun executeMethod(args: List<String>) {
        val methodName = args[0]
        val params = if (args.size > 1) args.subList(1, args.size) else listOf()

        executeMethod(selectedContractData, methodName, params)?.let { result ->
            println("smart contract method execute result: ${result.getRet_val()}")
            result.getContractState()?.let { state ->
                selectedContractData.contractState = state
                saveContractStateOnDisk(selectedContractData, contractsFolder)
            }
        }
    }

    fun getContractMethods() {
        getContractMethods(wrap(selectedContractData.byteCode))?.getMethods()?.forEach {
            println(it)
        }
    }

    fun getContractVariables() {
        if (selectedContractData.contractState == null) throw IllegalArgumentException("contractState not found, you have to executeByteCode method previous")
        getContractVariables(wrap(selectedContractData.byteCode), wrap(selectedContractData.contractState))?.getContractVariables()?.forEach {
            println(it)
        }
    }

    fun compileSourceCode() = transport.call { client ->
        client.compileBytecode(selectedContractData.sourceCode).let { response ->
            println(response.getStatus())
            writeToFile("$contractsFolder$separator${selectedContractData.address}${separator}bytecode.bin", response.getBytecode())
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
            return body(ContractExecutor.Client(TBinaryProtocol(this)))
        }
    }
}
