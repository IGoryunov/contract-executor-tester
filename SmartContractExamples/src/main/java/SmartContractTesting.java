import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SmartContractTesting extends SmartContract {

    private int total;

    public SmartContractTesting() throws Exception {
        String name = Thread.currentThread().getName();
        System.out.println("The constructor has been invoked. Thread: " + name);
    }

    public void openSocket(int port) throws Exception {
        System.out.println("Trying to open socket...");
        new ServerSocket(port);
        System.out.println("Opened");
    }

    public void setTotal(int value) {
        total = value;
    }

    public int getTotal() {
        return total;
    }

    public void createFile() throws Exception {
        try {
            Files.createFile(Paths.get(new URI("file:///./some.file")));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void createFileInProjectDir(String path) throws Exception {
        File file = new File(path);
        file.getParentFile().mkdirs();
        Files.createFile(Paths.get(file.getPath()));
    }

    public void addTokens(Integer amount) {
        total += amount;
        System.out.println(java.lang.Integer.toString(amount) + " tokens were added to total");
    }

    public int externalCall(@ContractAddress(id = 0) String address, @ContractMethod(id = 0) String method) {
        return (int) invokeExternalContract(address, method, null);
    }

    @Contract(address = "FTWo7QNzweb7JMNL1kuFC32pdkTeQ716mhKThbzXQ9wK", method = "addTokens")
    public void externalCallChangeState(@ContractAddress(id = 0) String address, @ContractMethod(id = 0) String method, Integer value) {
        invokeExternalContract(address, method, value);
    }

    public void openSocketOnNewThread() {
        Thread thread = new Thread(() -> {
            System.out.println("opening socket...");
            try {
                new ServerSocket(1500);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("failed");
                return;
            }
            System.out.println("success");
        });
        thread.start();
    }

    public void createFileOnNewThread() {
        Thread thread = new Thread(() -> {
            System.out.println("opening socket...");
            try {
                createFile();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("failed");
                return;
            }
            System.out.println("success");
        });
        thread.start();
    }

    public void killProcess() {
        System.exit(-1);
    }

    public void killThread() {
        Thread.currentThread().stop();
    }

    public void newThread() {
        new Thread(() -> {
            try {
                openSocket(5555);
                System.out.println("new Thread");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}

