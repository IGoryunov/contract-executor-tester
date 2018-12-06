package service

import com.credits.client.executor.thrift.generated.ContractExecutor
import com.credits.client.executor.thrift.generated.ExecuteByteCodeResult
import com.credits.client.executor.thrift.generated.GetContractMethodsResult
import com.credits.general.pojo.SmartContractData
import com.credits.general.util.Converter.objectToVariant
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.TSocket
import java.nio.ByteBuffer
import java.nio.ByteBuffer.wrap

class ContractExecutorThriftClient(host: String, port: Int) {
    private val transport = TSocket(host, port)

    fun getContractMethods(bytecode: ByteBuffer): GetContractMethodsResult = transport.call { client ->
        client.getContractMethods(bytecode)
    }

//    fun getContractVariables(bytecode: ByteBuffer, contractState: ByteBuffer): GetContractVariablesResult = transport.call { client ->
//        client.getContractVariables(bytecode, contractState)
//    }

    fun executeMethod(smartContractData: SmartContractData, methodName: String, params: List<String> = mutableListOf(), executionTime: Long = 5000): ExecuteByteCodeResult = transport.call { client ->
        with(smartContractData) {
            client.executeByteCode(
                    wrap(address),
                    wrap(smartContractDeployData.byteCode),
                    if (objectState != null) wrap(objectState) else wrap(byteArrayOf()),
                    methodName,
                    params.map { it -> objectToVariant(it) }.toCollection(mutableListOf()))
        }
    }

//    fun executeMultiple(smartContractData: SmartContractData, methodName: String, params: List<List<String>>, executionTime: Long = 5000): ExecuteByteCodeMultipleResult = transport.call { client ->
//        client.executeByteCodeMultiple(
//                wrap(byteArrayOf(0x1)),
//                wrap(smartContractData.byteCode),
//                if (smartContractData.contractState != null) wrap(smartContractData.contractState) else wrap(byteArrayOf()),
//                methodName,
//                params,
//                executionTime)
//    }

//    fun compileSourceCode(sourceCode: String): CompileByteCodeResult = transport.call { client ->
//        client.compileBytecode(sourceCode)
//    }

    private fun <R> TSocket.call(body: (ContractExecutor.Client) -> R): R {
        use {
            open()
            return body(ContractExecutor.Client(TBinaryProtocol(this)))
        }
    }
}
