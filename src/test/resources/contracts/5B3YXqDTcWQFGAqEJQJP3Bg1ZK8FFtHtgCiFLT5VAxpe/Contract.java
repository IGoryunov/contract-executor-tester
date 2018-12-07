public class Contract extends SmartContract {

    public static final String checkRegisterError = "This user is not registered, please call register() method";

    public static final String checkFrozenError =

            "This smartContract is frozen, only approve()";

    final String owner;
    private final java.math.BigDecimal tokenCost;
    private final int decimal;
    java.util.HashMap<String, java.math.BigDecimal> balances;
    private String name;
    private String symbol;
    private java.math.BigDecimal totalCoins;
    private java.util.HashMap<String, java.util.Map<String, java.math.BigDecimal>> allowed;
    private boolean frozen;


    public Contract() {
        name = "CreditsToken";
        symbol = "CST";
        decimal = 3;
        tokenCost = new java.math.BigDecimal(10).setScale(decimal, java.math.BigDecimal.ROUND_FLOOR);
        totalCoins = new java.math.BigDecimal(10_000_000).setScale(decimal, java.math.BigDecimal.ROUND_FLOOR);
        owner = "G2iSMjqaEQmA5pvFuFjKbMqJUxJZceAY5oc1uotr7SZZ";
        allowed = new java.util.HashMap<>();
        balances = new java.util.HashMap<>();
        balances.put(owner, new java.math.BigDecimal(1_000_000L).setScale(decimal, java.math.BigDecimal.ROUND_FLOOR));
    }

    public int getDecimal() {
        return decimal;
    }

    public void register() {
        balances.putIfAbsent(initiator, java.math.BigDecimal.ZERO.setScale(decimal, java.math.BigDecimal.ROUND_FLOOR));
    }

    public boolean setFrozen(boolean frozen) {
        if (initiator.equals(owner)) {
            this.frozen = frozen;
            return true;
        }
        return false;
    }


    public String getName() {
        return name;
    }


    public String getSymbol() {
        return symbol;
    }


    public String totalSupply() {
        return totalCoins.toString();
    }


    public String balanceOf(String owner) {
        checkRegistered(initiator);
        java.math.BigDecimal balance = balances.get(owner);
        return balance != null ? balance.toString() : "0";
    }


    public String allowance(String owner, String spender) {

        checkRegistered(initiator);

        if (allowed.get(owner) == null) {

            return "0";

        }

        java.math.BigDecimal amount = allowed.get(owner).get(spender);

        return amount != null ? amount.toString() : "0";

    }


    public boolean transfer(String to, String amount) {

        checkRegistered(initiator);

        checkFrozen();

        java.math.BigDecimal decimalAmount = new java.math.BigDecimal(amount).setScale(decimal, java.math.BigDecimal.ROUND_FLOOR);

        java.math.BigDecimal senderBalance = balances.get(initiator);

        java.math.BigDecimal targetBalance = balances.get(to);

        if (senderBalance == null || targetBalance == null || senderBalance.compareTo(decimalAmount) < 0) {

            return false;

        }

        balances.put(initiator, senderBalance.subtract(decimalAmount));

        balances.put(to, targetBalance.add(decimalAmount));


        return true;

    }


    public boolean transferFrom(String from, String to, String amount) {

        checkRegistered(initiator);

        checkFrozen();


        java.math.BigDecimal sourceBalance = balances.get(from);

        java.math.BigDecimal targetBalance = balances.get(to);

        java.math.BigDecimal decimalAmount = new java.math.BigDecimal(amount).setScale(decimal, java.math.BigDecimal.ROUND_FLOOR);

        if (sourceBalance == null || targetBalance == null || sourceBalance.compareTo(decimalAmount) < 0) {

            return false;

        }


        java.util.Map<String, java.math.BigDecimal> spender = allowed.get(from);

        if (spender == null) {

            return false;

        }

        java.math.BigDecimal allowTokens = spender.get(initiator);

        if (allowTokens == null || allowTokens.compareTo(decimalAmount) < 0) {

            return false;

        }


        spender.put(initiator, allowTokens.subtract(decimalAmount));

        balances.put(from, sourceBalance.subtract(decimalAmount));

        balances.put(to, targetBalance.add(decimalAmount));


        return true;

    }


    public void approve(String spender, String amount) {

        checkRegistered(initiator);

        java.math.BigDecimal decimalAmount = new java.math.BigDecimal(amount).setScale(decimal, java.math.BigDecimal.ROUND_FLOOR);

        java.util.Map<String, java.math.BigDecimal> initiatorSpenders = allowed.get(initiator);

        if (initiatorSpenders == null) {

            java.util.Map<String, java.math.BigDecimal> newSpender = new java.util.HashMap<>();

            newSpender.put(spender, decimalAmount);

            allowed.put(initiator, newSpender);

        } else {

            java.math.BigDecimal spenderAmount = initiatorSpenders.get(spender);

            initiatorSpenders.put(spender, spenderAmount == null ? decimalAmount : spenderAmount.add(decimalAmount));

        }

    }


    public boolean burn(String amount) {

        checkRegistered(initiator);

        checkFrozen();

        java.math.BigDecimal decimalAmount = new java.math.BigDecimal(amount).setScale(decimal, java.math.BigDecimal.ROUND_FLOOR);

        if (!initiator.equals(owner) || totalCoins.compareTo(decimalAmount) < 0) {

            return false;

        }

        totalCoins = totalCoins.subtract(decimalAmount);

        return true;

    }


    public boolean buyTokens(String amount) {

        checkRegistered(initiator);

        checkFrozen();

        java.math.BigDecimal decimalAmount = new java.math.BigDecimal(amount).setScale(decimal, java.math.BigDecimal.ROUND_FLOOR);

        sendTransaction(owner, tokenCost.multiply(decimalAmount).doubleValue(), 0.1, new byte[0]);

        if (balances.containsKey(initiator)) {

            balances.put(initiator, balances.get(initiator).add(decimalAmount));

        } else {

            balances.put(initiator, decimalAmount);

        }

        totalCoins = totalCoins.subtract(decimalAmount);

        return true;

    }


    public boolean checkRegistered(String owner) {
        if (balances.get(owner) == null) {

            throw new RuntimeException(checkRegisterError);

        }

        return true;

    }


    public boolean checkFrozen() {

        if (this.frozen) {

            throw new RuntimeException(checkFrozenError);

        }

        return true;

    }

}