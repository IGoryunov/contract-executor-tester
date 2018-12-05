package service

import com.credits.leveldb.client.data.SmartContractData
import loadContractsFromDisk
import org.apache.thrift.transport.TTransportException
import org.junit.Assert
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.ByteBuffer.allocate
import java.nio.ByteBuffer.wrap
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
                executorClient.getContractMethods(allocate(0))
            } catch (e: TTransportException) {
                errorMessage = e.localizedMessage
                return@assumeTrue false
            }
            true
        }, { "Integration test unavailable. Reason: $errorMessage" })
    }

    @Test
    fun executeBytecodeTest() {
        with(smartContracts[1]) {
            executorClient.executeMethod(this, "addAndGet", listOf("100"))
        }.apply {
            println(this)

            with(getStatus()) {
                assertEquals(0.toByte(), code)
                assertEquals("success", message)
            }

            assertEquals(1334, getRet_val().v_i32)
        }
    }

    @Test
    fun getContractMethodsTest() {
        with(smartContracts[0]) {
            executorClient.getContractMethods(wrap(byteCode))
        }.apply {
            println(this)

            with(getStatus()) {
                assertEquals(0.toByte(), code)
                assertEquals("success", message)
            }

            assertEquals(15, methods.size)
        }
    }

    @Test
    fun `compile sourcecode without errors`(){
        with(smartContracts[0]) {
            executorClient.compileSourceCode(sourceCode)
        }.apply {
            println(this)

            with(getStatus()) {
                assertEquals(0.toByte(), code)
                assertEquals("success", message)
            }
        }
    }

    @Test
    fun `compile sourcecode with errors`(){
        with(smartContracts[2]) {
            executorClient.compileSourceCode(sourceCode)
        }.apply {
            println(this)

            with(getStatus()) {
                assertEquals(1.toByte(), code)
                assertNotNull(message)
                assertTrue(message.isNotEmpty())
            }
        }
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