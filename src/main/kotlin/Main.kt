
import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import service.ContractExecutorService
import java.io.File.separator
import java.nio.file.Paths

object Options {
    private val currentDirectory = Paths.get(System.getProperty("user.dir")).toString()

    @Parameter(names = ["-h", "--help"], help = true)
    var help: Boolean = false

    @Parameter(names = ["-e"], description = "call thrift available methods [executeByteCode, getContractMethods, getContractVariables, compileSourceCode]")
    var method: String? = "executeByteCode"

    @Parameter(names = ["-t"], description = "amount threads")
    var threads: Int = 1

    @Parameter(names =["-a"], description = "list of arguments [arg0, arg1, arg2...]")
    var arguments: MutableList<String> = mutableListOf("")

    @Parameter(names = ["-p"], description = "contracts folder path")
    var contractsFolder: String = currentDirectory + separator + "contracts" + separator

    @Parameter(names = ["-i"], description = "index of current contract into \"contracts\" folder")
    var contractIndex: Int = 0

}

fun main(args: Array<String>) {
    val jcd = JCommander.newBuilder().addObject(Options).build()
    jcd.parse(*args)
    if (Options.help) {
        jcd.usage()
        return
    }

    val executorService = ContractExecutorService(Options.contractsFolder, Options.contractIndex)
    Options.method?.let { method ->
        with(executorService) {
            when (method) {
                "getContractMethods" -> getContractMethods()
                "getContractVariables" -> getContractVariables()
                "executeByteCode" -> async(Options.threads, 30, method) { executeMethod(Options.arguments) }
                "executeByteCodeMultiple" -> println("this method not supported yet :(")
                "compileSourceCode" -> compileSourceCode()
                else -> print("unknown method")
            }
        }
    }
}








