import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.ServerSocket;

public class Contract extends SmartContract {
	
	public int digit = 1234;
    
	public Contract() {
        System.out.println("Constructor");
    }

    public void initialize() {

	    COMPILATION ERROR HERE

    public void balanceGet() throws Exception {
        System.out.println("getBalance()");
        java.math.BigDecimal balance = getBalance("1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2");
        System.out.println("getBalance=" + balance);
    }

    public void sendZeroCS() throws Exception {
        System.out.println("try to send 0 credits...");
        sendTransaction("1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2", 0, 0, new byte[0]);
        System.out.println("success");
    }

    public void useReflection() throws Exception {
        getClass().getConstructor().newInstance();
    }

    public void openSocket() throws Exception {
        new ServerSocket(5555);
        System.out.println();
    }
	
	public int addAndGet(int value){
		return digit += value;
	}
};