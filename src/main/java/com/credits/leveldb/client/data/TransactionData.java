package com.credits.leveldb.client.data;

import java.math.BigDecimal;

/**
 * Created by Rustem.Saidaliyev on 01.02.2018.
 */
public class TransactionData {

    private String innerId;
    private String source;
    private String target;
    private BigDecimal amount;
    private BigDecimal balance;
    private String currency;

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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return String.format("{innerId : %s, source : %s, target : %s, amount : %s, balance : %s, currency : %s}",
            this.innerId,
            this.source,
            this.target,
            this.amount,
            this.balance,
            this.currency
        );
    }
}
