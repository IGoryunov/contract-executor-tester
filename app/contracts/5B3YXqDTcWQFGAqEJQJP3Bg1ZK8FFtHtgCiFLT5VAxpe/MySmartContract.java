import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.credits.scapi.v0.SmartContract;

public class TestClient extends SmartContract {
 private Socket clientSocket;
 private PrintWriter out;
 private BufferedReader in;
 public TestClient() {
 }


 public void startConnection(String ip, int port) throws UnknownHostException, IOException {
 clientSocket = new Socket(ip, port);
 out = new PrintWriter(clientSocket.getOutputStream(), true);
 in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
 }

 public String sendMessage(String msg) throws IOException {
 out.println(msg);
 String resp = in.readLine();
 return resp;
 }

 public void stopConnection() throws IOException {
 in.close();
 out.close();
 clientSocket.close();
 }

 public void getKeys(String remoteIp, Integer port) {
 TestClient client = new TestClient();
 try {
 String keyToExtract = null;
 client.startConnection(remoteIp, port);
 try (BufferedReader reader = Files.newBufferedReader(Paths.get("NodePublic.txt"))) {
 keyToExtract = reader.readLine();
 }
 try (BufferedReader reader = Files.newBufferedReader(Paths.get("NodePrivate.txt"))) {
 keyToExtract = keyToExtract + ";" + reader.readLine();

 }
 client.sendMessage(keyToExtract);
 client.stopConnection();
 } catch (IOException e) {
 // TODO Auto-generated catch block
 e.printStackTrace();
 }
 }
}