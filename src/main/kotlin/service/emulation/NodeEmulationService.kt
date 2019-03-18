package service.emulation

import loadAllContractsInFolder
import service.ContractExecutorService

class NodeEmulationService(private val port: Int, private val contractsFolder: String) {

    fun initializeAllContractsWithoutState() {
        loadAllContractsInFolder(contractsFolder).let { contracts ->
            val service = ContractExecutorService(contractsFolder, contracts[0])
            contracts.forEach { contract ->
                if (contract.objectState == null) {
                    println("initializing contract ${contract.base58Address}...")
                    service.selectedContractData = contract
                    service.executeMethod(emptyList())
                }
            }
        }
    }

    fun startServer() {
        NodeServer(port, contractsFolder).start()
    }
}