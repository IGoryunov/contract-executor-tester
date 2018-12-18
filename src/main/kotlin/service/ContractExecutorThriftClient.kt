package service

import com.credits.client.executor.service.ContractExecutorThriftApi
import com.credits.client.executor.service.ContractExecutorThriftApiClient
import com.credits.client.executor.thrift.generated.ExecuteByteCodeResult
import com.credits.client.node.pojo.SmartContractData
import com.credits.general.thrift.generated.Variant

class ContractExecutorThriftClient(host: String, port: Int) :
    ContractExecutorThriftApi by ContractExecutorThriftApiClient.getInstance(host, port) {

    fun executeMethod(
        smartContractData: SmartContractData,
        methodName: String,
        params: List<Variant> = mutableListOf(),
        executionTime: Long = 5000
    ): ExecuteByteCodeResult = with(smartContractData) {
        executeByteCode(
            address,
            smartContractDeployData.byteCode,
            if (objectState != null) objectState else byteArrayOf(),
            methodName,
            params,
            executionTime
        )
    }
}
