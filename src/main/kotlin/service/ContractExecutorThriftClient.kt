package service

import com.credits.client.executor.service.ContractExecutorThriftApi
import com.credits.client.executor.service.ContractExecutorThriftApiClient

class ContractExecutorThriftClient(host: String, port: Int) :
    ContractExecutorThriftApi by ContractExecutorThriftApiClient.getInstance(host, port) {
}
