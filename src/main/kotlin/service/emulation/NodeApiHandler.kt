package service.emulation

import com.credits.client.executor.thrift.generated.apiexec.APIEXEC
import com.credits.client.executor.thrift.generated.apiexec.GetSeedResult
import com.credits.client.executor.thrift.generated.apiexec.SendTransactionResult
import com.credits.client.executor.thrift.generated.apiexec.SmartContractGetResult
import com.credits.client.node.thrift.generated.Transaction
import com.credits.client.node.thrift.generated.WalletBalanceGetResult
import com.credits.client.node.thrift.generated.WalletIdGetResult
import com.credits.general.pojo.ApiResponseCode
import com.credits.general.pojo.ApiResponseCode.NOT_IMPLEMENTED
import com.credits.general.pojo.ApiResponseCode.SUCCESS
import com.credits.general.thrift.generated.APIResponse
import com.credits.general.thrift.generated.Amount
import com.credits.general.util.GeneralConverter.byteCodeObjectsDataToByteCodeObjects
import com.credits.general.util.GeneralConverter.encodeToBASE58
import loadContractFromDisk
import java.io.File.separator
import java.nio.ByteBuffer
import kotlin.random.Random

internal class NodeApiHandler(private val contractsFolder: String) : APIEXEC.Iface {

    override fun WalletBalanceGet(address: ByteBuffer?): WalletBalanceGetResult =
            WalletBalanceGetResult(successResponse, Amount(100, 1))


    private val successResponse = APIResponse(SUCCESS.code, "success")
    private val notImplementedResponse = APIResponse(NOT_IMPLEMENTED.code, "this method is not implemented")

    override fun SendTransaction(accessId: Long, transaction: Transaction?): SendTransactionResult? =
            SendTransactionResult(successResponse).also { println(it) }


    override fun WalletIdGet(accessId: Long, address: ByteBuffer?): WalletIdGetResult? =
            WalletIdGetResult(successResponse, 23).also { println(it) }


    override fun GetSeed(accessId: Long) =
            GetSeedResult(successResponse, ByteBuffer.wrap(Random(accessId).nextBytes(8))).also { println(it) }


    override fun SmartContractGet(accessId: Long, address: ByteBuffer?) =
            try {
                loadContractFromDisk(contractsFolder + separator + encodeToBASE58(address!!.array())).let {
                    SmartContractGetResult(successResponse,
                            byteCodeObjectsDataToByteCodeObjects(it.smartContractDeployData.byteCodeObjects),
                            ByteBuffer.wrap(it.objectState ?: byteArrayOf()),
                            true)
                }
            } catch (e: Throwable) {
                SmartContractGetResult(APIResponse(ApiResponseCode.FAILURE.code.toByte(), e.message), null, null, false)
            }.also { println(it) }

}



