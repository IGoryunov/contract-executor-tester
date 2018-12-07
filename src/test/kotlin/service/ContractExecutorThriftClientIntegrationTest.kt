package service

import com.credits.general.thrift.generated.Variant
import loadContractsFromDisk
import org.apache.thrift.transport.TTransportException
import org.junit.Assert
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class ContractExecutorThriftClientIntegrationTest : Assert() {

    private val smartContracts = loadContractsFromDisk("src/test/resources/contracts")
    private lateinit var executorClient: ContractExecutorThriftClient


    @BeforeEach
    fun setUp() {
        executorClient = ContractExecutorThriftClient("localhost", 9080)

        var errorMessage = "unknown error"
        assumeTrue({
            try {
                executorClient.checkConnection()
            } catch (e: TTransportException) {
                errorMessage = e.localizedMessage
                return@assumeTrue false
            }
            true
        }, "Integration test unavailable. Reason: $errorMessage")
    }

    @Test
    fun executeBytecodeTest() {
        with(smartContracts[1]) {
            executorClient.executeMethod(this, "addAndGet", listOf(Variant.v_i32(100)))
        }.apply {
            println(this)

            with(getStatus()) {
                assertEquals(0.toByte(), code)
                assertEquals("success", message)
            }

            assertEquals(1334, getRet_val().v_i32)
        }
    }
}