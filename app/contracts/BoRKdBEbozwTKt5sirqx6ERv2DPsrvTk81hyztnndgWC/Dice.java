import com.credits.scapi.v0.SmartContract;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static java.lang.Math.abs;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.DOWN;

public class Dice extends SmartContract {
    Map<String, NumAndRollType> userInput = new HashMap<>();

    public Dice() {
        super();
    }

    private void play(String bet, NumAndRollType numAndRollType) {
        int randomNumber = generateRandomNumber(getSeed());
        BigDecimal betDecimal = new BigDecimal(bet);
        BigDecimal reward = calculateReward(new BigDecimal(bet).setScale(2, DOWN), numAndRollType.number, numAndRollType.rollOver);
        if (numAndRollType.number < randomNumber) {
            reward = ZERO;
        }
        String dice = "number -> " + numAndRollType.number + " | " + randomNumber + " <- random\n";
        System.out.println(randomNumber < numAndRollType.number
                ? dice + "Winning reward(" + reward.doubleValue() + ")" + " - bet(" + betDecimal.doubleValue() + ") = " + reward.subtract(betDecimal) + " tokens"
                : dice + "Loosing");
        sendTransaction(contractAddress, initiator, reward.doubleValue(), 0.1);
    }

    public void selectNumber(int userNumber, boolean rollOver) {
        if (userNumber < 2 || userNumber > 99) {
            throw new IllegalArgumentException("Incorrect number(" + userNumber + "). Available userInput 2-99");
        }
        userInput.put(initiator, new NumAndRollType(userNumber, rollOver));
    }

    public BigDecimal getContractBalance() {
        return getBalance(contractAddress);
    }

    public void payable(String amount, String currency) {
        if(userInput.containsKey(initiator)) {
            play(amount, userInput.get(initiator));
        }
    }

    BigDecimal calculateReward(BigDecimal bet, int number, boolean rollOver) {
        BigDecimal k = new BigDecimal(rollOver ? (double) number / 100 : 1 - (double) number / 100);
        return bet.multiply(new BigDecimal(1).add(k)).setScale(2, DOWN);
    }

    int generateRandomNumber(byte[] seed) {
        Random rand = new Random((long) Arrays.hashCode(seed));
        return abs(rand.nextInt() % 100);
    }

    private class NumAndRollType implements Serializable {
        final int number;
        final boolean rollOver;

        public NumAndRollType(int number, boolean rollOver) {
            this.number = number;
            this.rollOver = rollOver;
        }
    }
}
