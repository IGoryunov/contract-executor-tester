/*
import java.util.Arrays;
import java.math.BigDecimal;
import java.text.NumberFormat;

public class MySmartContract extends SmartContract {

    public int total;

    public void initialize() {
        String name = Thread.currentThread().getName();
        System.out.println("The initialize method has been invoked");
        total = 1;
    }

    public MySmartContract() {
        System.out.println("It is initiator adderss - " + initiator);
    }

    public void addTokens(Integer amount) {
        total += amount;
        System.out.println(java.lang.Integer.toString(amount) + " tokens were added to total");
    }

    public void printTotal() {
        System.out.println("total = " + total);
    }

    public int getTotal() {
        System.out.println("total = " + total);
        return this.total;
    }

    public boolean payable(String amount, String currency) throws Exception{
	   NumberFormat nf = NumberFormat.getInstance(java.util.Locale.ENGLISH);
       BigDecimal val = new BigDecimal(nf.parse(amount).doubleValue());
       sendTransaction(initiator, contractAddress, val.doubleValue(), 1.0);
       return true;
   }
	
    public int externalCall(@ContractAddress(id = 0) String address, @ContractMethod(id = 0) String method) {
        return (int) invokeExternalContract(address, method, null);
    }

	@Contract(address = "FTWo7QNzweb7JMNL1kuFC32pdkTeQ716mhKThbzXQ9wK", method = "addTokens")
    public void externalCallChangeState(@ContractAddress(id = 0) String address, @ContractMethod(id = 0) String method, Integer value) {
        invokeExternalContract(address, method, value);
    }

    @Override
    public int hashCode() {
        return super.hashCode()+total;
    }

    public String getInitiatorAddress(){
        return initiator;
    }
}*/

import java.math.BigDecimal;
import java.net.ServerSocket;

public class Contract extends SmartContract {

  public Contract() {
    super();
    author = "5B3YXqDTcWQFGAqEJQJP3Bg1ZK8FFtHtgCiFLT5VAxpe";
    balance = new BigDecimal(0);
  }

  private String author;
  private int val = 0;
  private BigDecimal balance;

  public int calc() {
    ++val;
    return val;
  }

  public int waitFor(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException x) {
      return 2;
    }
    return 1;
  }

  //public void payable(String amount, String currency) {
    //BigDecimal val = new BigDecimal(amount);
    //balance = balance.add(val);
    //balance = balance.subtract(val);
    //sendTransaction(author, val.doubleValue(), 1.0, null);
  //}

  BigDecimal getBalance() {
    return balance;
  }

  public void openSocketOnNewThread() {

    Thread thread = new Thread() {
      public void run() {
        System.out.println("opening socket...");
        try {
          new ServerSocket(1502);
        } catch (Exception e) {
          System.out.println("fail");
          e.printStackTrace();
        }
        System.out.println("success");
      }
    };

    thread.start();
  }

}