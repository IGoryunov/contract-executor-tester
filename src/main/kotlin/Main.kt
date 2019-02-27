import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import service.ContractExecutorService
import java.io.File.separator
import java.nio.file.Paths

object Options {
    private val currentDirectory = Paths.get(System.getProperty("user.dir")).toString()

    @Parameter(names = ["-h", "--help"], help = true)
    var help: Boolean = false

    @Parameter(
            names = ["-e"],
            description = "call thrift available methods [executeByteCode, executeByteCodeMultiple getContractMethods, getContractVariables, compileSourceCode]"
    )
    var method: String? = ""

    @Parameter(names = ["-t"], description = "amount threads")
    var threads: Int = 1

    @Parameter(names = ["-a"], description = "argument of method")
    var arguments: MutableList<String> = mutableListOf("")

    @Parameter(names = ["-p"], description = "contracts folder path")
    var contractsFolder: String = currentDirectory + separator + "contracts" + separator

    @Parameter(names = ["-i"], description = "index of current contract into \"contracts\" folder")
    var contractIndex: Int = 0

    @Parameter(names = ["-s"], description = "show selected contract sourcecode")
    var isShowContractSourceCode: Boolean = false

    @Parameter(names = ["-d"], description = "enable debug info")
    var isDebugInfoEnabled: Boolean = false

}

fun main(args: Array<String>) {
    val jcd = JCommander.newBuilder().addObject(Options).build()
    jcd.parse(*args)
    if (Options.help) {
        jcd.usage()
        return
    }

    Options.apply {
        val selectedContractData = loadContractsFromDisk(contractsFolder, isDebugInfoEnabled)[contractIndex]
        if (isShowContractSourceCode) println(selectedContractData.smartContractDeployData.sourceCode)
        with(ContractExecutorService(contractsFolder, selectedContractData)) {
            async(threads, 30, this) {
                when (method) {
                    "executeByteCode" -> executeMethod(arguments)
//                    "compileSourceCode" -> compileSourceCode()
                    "getContractMethods" -> getContractMethods()
                    "getContractVariables" -> getContractVariables()
                    "executeByteCodeMultiple" -> println("method not support yet")
                    else -> print("unknown method")
                }
            }
        }
    }
}








