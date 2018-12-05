package service

import com.credits.leveldb.client.data.SmartContractData
import com.credits.thrift.generated.*
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.TSocket
import java.nio.ByteBuffer
import java.nio.ByteBuffer.wrap

class ContractExecutorThriftClient(host: String, port: Int) {
    private val transport = TSocket(host, port)

    fun getContractMethods(bytecode: ByteBuffer): GetContractMethodsResult = transport.call { client ->
        client.getContractMethods(bytecode)
    }

    fun getContractVariables(bytecode: ByteBuffer, contractState: ByteBuffer): GetContractVariablesResult = transport.call { client ->
        client.getContractVariables(bytecode, contractState)
    }

    fun executeMethod(smartContractData: SmartContractData, methodName: String, params: List<String> = mutableListOf(), executionTime: Long = 5000): ExecuteByteCodeResult = transport.call { client ->
        client.executeByteCode(
                wrap(byteArrayOf(0x1)),              //todo add correct address converter
                wrap(smartContractData.byteCode),
                if (smartContractData.contractState != null) wrap(smartContractData.contractState) else wrap(byteArrayOf()),
                methodName,
                params,
                executionTime)
    }

    fun executeMultiple(smartContractData: SmartContractData, methodName: String, params: List<List<String>>, executionTime: Long = 5000): ExecuteByteCodeMultipleResult = transport.call { client ->
        client.executeByteCodeMultiple(
                wrap(byteArrayOf(0x1)),              //todo add correct address converter
                wrap(smartContractData.byteCode),
                if (smartContractData.contractState != null) wrap(smartContractData.contractState) else wrap(byteArrayOf()),
                methodName,
                params,
                executionTime)
    }

    fun compileSourceCode(sourceCode: String): CompileByteCodeResult = transport.call { client ->
        client.compileBytecode(sourceCode)
    }

    private fun <R> TSocket.call(body: (ContractExecutor.Client) -> R): R {
        use {
            open()
            return body(ContractExecutor.Client(TBinaryProtocol(this)))
        }
    }
}
