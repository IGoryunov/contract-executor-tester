package service

//import loadContractsFromDisk
//import org.junit.jupiter.api.Test

//internal class ContractExecutorServiceTest {
//
//    private val contractFolder = "src/test/resources/contracts"
//    private val smartContracts = loadContractsFromDisk(contractFolder)
//    lateinit var service: ContractExecutorService
//
//    @Test
//    fun executeMethodWithStringParams() {
//        service = ContractExecutorService(contractFolder, smartContracts[0])
//        service.executeMethod(listOf("register"))
//        service.executeMethod(listOf("balanceOf", "5B3YXqDTcWQFGAqEJQJP3Bg1ZK8FFtHtgCiFLT5VAxpe"))
//        service.executeMethod(
//                listOf(
//                        "transfer",
//                        "5B3YXqDTcWQFGAqEJQJP3Bg1ZK8FFtHtgCiFLT5VAxpe",
//                        "0"
//                )
//        )
//        service.executeMethod(
//                listOf(
//                        "transfer",
//                        "5B3YXqDTcWQFGAqEJQJP3Bg1ZK8FFtHtgCiFLT5VAxpe",
//                        "100"
//                )
//        )
//    }
//
//    @Test
//    fun executeMethodWithIntParams() {
//        service = ContractExecutorService(contractFolder, smartContracts[1])
//        service.executeMethod(listOf("openSocket", "5000"))
//    }
//}