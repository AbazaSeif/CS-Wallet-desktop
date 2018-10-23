package com.credits.leveldb.client.data;

import java.math.BigDecimal;

public class TransactionData {

    private String id;
    private byte[] source;
    private byte[] target;
    private BigDecimal amount;
    private BigDecimal balance;
    private byte currency;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getSource() {
        return source;
    }

    public void setSource(byte[] source) {
        this.source = source;
    }

    public byte[] getTarget() {
        return target;
    }

    public void setTarget(byte[] target) {
        this.target = target;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public byte getCurrency() {
        return currency;
    }

    public void setCurrency(byte currency) {
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
        return String.format("{id : %s, source : %s, target : %s, amount : %s, balance : %s, currency : %s}",
            this.id,
            this.source,
            this.target,
            this.amount,
            this.balance,
            this.currency
        );
    }
}
