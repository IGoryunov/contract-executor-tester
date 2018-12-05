package service

import com.credits.leveldb.client.data.SmartContractData
import loadContractsFromDisk
import org.apache.thrift.transport.TTransportException
import org.junit.Assert
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.ByteBuffer
import java.util.stream.Collectors
import java.util.stream.Stream


internal class ContractExecutorThriftClientIntegrationTest : Assert() {

    private val smartContracts = loadContractsFromDisk("src/test/resources/contracts")
    private lateinit var executorClient: ContractExecutorThriftClient


    @BeforeEach
    fun setUp() {
        executorClient = ContractExecutorThriftClient("localhost", 9080)

        var errorMessage = "unknown error"
        assumeTrue({
            try {
                executorClient.getContractMethods(ByteBuffer.allocate(0))
            } catch (e: TTransportException) {
                errorMessage = e.localizedMessage
                return@assumeTrue false
            }
            true
        }, { "Integration test unavailable. Reason: $errorMessage" })
    }

    @Test
    fun `correct using executeMultiple`() {
        val amountParams = 100L
        val args: List<List<String>> = Stream.generate { listOf<String>() }.limit(amountParams).collect(Collectors.toList())

        with(smartContracts[0]) {
            executorClient.executeMultiple(SmartContractData("5B3YXqDTcWQFGAqEJQJP3Bg1ZK8FFtHtgCiFLT5VAxpd", sourceCode, byteCode, contractState, hashState), "getDecimal", args)
        }.apply {
            println(this)

            assertEquals(amountParams.toInt(), getResults().size)

            getResults().forEach {
                assertEquals(3, it.getRet_val().v_i32)
            }

            with(getStatus()) {
                assertEquals(0.toByte(), code)
                assertEquals("success", message)
            }
        }
    }
}