package service.emulation

import com.credits.client.executor.thrift.generated.apiexec.APIEXEC
import org.apache.thrift.server.TThreadPoolServer
import org.apache.thrift.server.TThreadPoolServer.Args
import org.apache.thrift.transport.TServerSocket


internal class NodeServer(private val port: Int, private val contractsFolder: String) {

    fun start() {
        val args = Args(TServerSocket(port))
                .processor(APIEXEC.Processor(NodeApiHandler(contractsFolder)))
        println("Node emulation server started on port $port")
        TThreadPoolServer(args).serve()
    }
}
