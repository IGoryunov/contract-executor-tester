
import compiler.SimpleInMemoryCompilator.compile
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Paths

class CompileUtils {
    companion object {

        fun loadContractBytecode(path: String): ByteBuffer? {
            val sourceCode = String(Files.readAllBytes(Paths.get(path)))
            val bytes: Any = compile(sourceCode, "Contract")
            return ByteBuffer.wrap(bytes as ByteArray)
        }


    }
}