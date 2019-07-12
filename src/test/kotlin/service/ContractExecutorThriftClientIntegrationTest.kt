package service

//import loadContractsFromDisk
//import org.junit.Assert
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//

//internal class ContractExecutorThriftClientIntegrationTest : Assert() {

//    private val smartContracts = loadContractsFromDisk("src/test/resources/contracts")
//    private lateinit var executorClient: ContractExecutorThriftApiClient


//    @BeforeEach
    fun setUp() {
//        executorClient = ContractExecutorThriftApiClient.getInstance("localhost", 9080)
//
//        var errorMessage = "unknown error"
//        assumeTrue({
//            try {
//                executorClient.getContractMethods(smartContracts[0].smartContractDeployData.byteCode)
//            } catch (e: TTransportException) {
//                errorMessage = e.localizedMessage
//                return@assumeTrue false
//            }
//            true
//        }, "Integration test unavailable. Reason: $errorMessage")
    }

//    @Test
//    fun executeBytecodeTest() {
//        with(smartContracts[1]) {
//            executorClient.executeByteCode(
//                address,
//                smartContractDeployData.byteCode,
//                objectState,
//                "addAndGet",
//                listOf(Variant.v_i32(100)),
//                500
//            )
//        }.apply {
//            println(this)
//
//            with(getStatus()) {
//                assertEquals(0.toByte(), code)
//                assertEquals("success", message)
//            }
//
//            assertEquals(1334, getRet_val().v_i32)
//        }
//    }
//}