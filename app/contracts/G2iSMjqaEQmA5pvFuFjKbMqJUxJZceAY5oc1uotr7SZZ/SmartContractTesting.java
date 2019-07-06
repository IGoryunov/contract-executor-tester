import com.credits.scapi.v1.SmartContract;

import java.net.ServerSocket;
import java.net.URI;
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
        System.out.println("total = " + total);
        return total;
    }

    public void createFile() throws Exception {
        Files.createFile(Paths.get(new URI("file:///./some.file")));
    }

    public void addTotal(Integer amount) {
        total += amount;
        System.out.println(java.lang.Integer.toString(amount) + " tokens were added to total");
    }

    public int externalCall(@ContractAddress(id = 0) String address, @ContractMethod(id = 0) String method) {
        return (int) invokeExternalContract(address, method);
    }

    @Contract(address = "FTWo7QNzweb7JMNL1kuFC32pdkTeQ716mhKThbzXQ9wK", method = "addTokens")
    public void externalCallChangeState(@ContractAddress(id = 0) String address, @ContractMethod(id = 0) String method, Integer value) {
        invokeExternalContract(address, method, value);
    }

    public void recursionExternalContractGetterCall(int count) {
        System.out.println("count = " + count);
        if (count-- > 0) {
            invokeExternalContract(contractAddress, "recursionExternalContractCall", count);
        }
    }

    public Integer recursionExternalContractSetterCall(int count) {
        System.out.println("count = " + count);
        if (count-- > 0) {
            addTotal(count);
            return (int) invokeExternalContract(contractAddress, "recursionExternalContractSetterCall", count);
        }
        return getTotal();
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

    public void createSocketOnNewThreadUseLambda() {
        new Thread(() -> {
            try {
                openSocket(5555);
                System.out.println("new Thread");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void killProcess() {
        System.exit(-1);
    }

    public void killThread() {
        Thread.currentThread().stop();
    }
}

