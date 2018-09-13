package com.credits.leveldb.client.data;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

public class TransactionFlowData {

    private long innerId;
    private byte[] source;
    private byte[] target;
    private BigDecimal amount;
    private BigDecimal balance;
    private byte currency;
    private byte[] signature;
    private BigDecimal fee;

    public TransactionFlowData(
            long innerId,
            byte[] source,
            byte[] target,
            BigDecimal amount,
            BigDecimal balance,
            byte currency,
            byte[] signature,
            BigDecimal fee
    ) {
        this.setInnerId(innerId);
        this.setSource(source);
        this.setTarget(target);
        this.setAmount(amount);
        this.setBalance(balance);
        this.setCurrency(currency);
        this.setSignature(signature);
        this.setFee(fee);
    }

    public long getInnerId() {
        return innerId;
    }

    public void setInnerId(long innerId) {
        this.innerId = innerId;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public byte getCurrency() {
        return currency;
    }

    public void setCurrency(byte currency) {
        this.currency = currency;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
}
