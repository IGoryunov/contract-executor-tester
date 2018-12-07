package service

import com.credits.client.executor.thrift.generated.ContractExecutor
import com.credits.client.executor.thrift.generated.ExecuteByteCodeResult
import com.credits.client.executor.thrift.generated.GetContractMethodsResult
import com.credits.general.pojo.SmartContractData
import com.credits.general.thrift.generated.Variant
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.TSocket
import java.nio.ByteBuffer
import java.nio.ByteBuffer.wrap

class ContractExecutorThriftClient(host: String, port: Int) {
    private val transport = TSocket(host, port)

    fun getContractMethods(bytecode: ByteBuffer): GetContractMethodsResult = transport.call { client ->
        client.getContractMethods(bytecode)
    }

    fun checkConnection() = transport.use {
        transport.open()
        transport.isOpen
    }

    fun executeMethod(smartContractData: SmartContractData, methodName: String, params: List<Variant> = mutableListOf(), executionTime: Long = 5000): ExecuteByteCodeResult = transport.call { client ->
        with(smartContractData) {
            client.executeByteCode(
                    wrap(address),
                    wrap(smartContractDeployData.byteCode),
                    if (objectState != null) wrap(objectState) else wrap(byteArrayOf()),
                    methodName,
                    params)
        }
    }

    private fun <R> TSocket.call(body: (ContractExecutor.Client) -> R): R {
        use {
            open()
            return body(ContractExecutor.Client(TBinaryProtocol(this)))
        }
    }
}

