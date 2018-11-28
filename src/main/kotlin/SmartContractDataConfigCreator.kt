import com.credits.leveldb.client.data.SmartContractData
import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import com.thoughtworks.xstream.annotations.XStreamImplicit
import utils.Xml.serialize

class SmartContractDataConfigCreator {

    @XStreamAlias("smartcontracts")
    class SmartContracts(
            @XStreamAsAttribute
            val address: String = "",

            @XStreamImplicit(itemFieldName = "contract")
            val contracts: List<Contract>? = null

    )

    class Contract(
            @XStreamAsAttribute
            val address: String = "",

            @XStreamAsAttribute
            val bytecode: String = "",

            @XStreamAsAttribute
            val hashstate: String = "",

            @XStreamAlias("sourcecode")
            val sourceCode: String = ""
    )

    companion object {
        fun createConfig(contractsData: List<SmartContractData>) {
            serialize(SmartContracts("", List(contractsData.size) {
                Contract(contractsData[it].address, "bytecode path", contractsData[it].hashState, contractsData[it].sourceCode)
            }))
        }
    }
}