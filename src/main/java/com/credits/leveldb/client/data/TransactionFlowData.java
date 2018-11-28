package com.credits.leveldb.client.data;

import java.math.BigDecimal;

/**
 * Created by Rustem.Saidaliyev on 01.02.2018.
 */
public class TransactionFlowData {

    private String innerId;
    private String source;
    private String target;
    private BigDecimal amount;
    private BigDecimal balance;
    private String currency;
    private String signature;

    public TransactionFlowData(
            String innerId,
            String source,
            String target,
            BigDecimal amount,
            BigDecimal balance,
            String currency,
            String signature
    ) {
        this.setInnerId(innerId);
        this.setSource(source);
        this.setTarget(target);
        this.setAmount(amount);
        this.setBalance(balance);
        this.setCurrency(currency);
        this.setSignature(signature);
    }

    public String getInnerId() {
        return innerId;
    }

    public void setInnerId(String innerId) {
        this.innerId = innerId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
