package com.credits.wallet.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.ByteBuffer;

public class Transaction implements Serializable {

    private long innerId;
    private String source;
    private String target;
    private BigDecimal amount;
    private BigDecimal balance;
    private byte currency;
    private ByteBuffer signature;
    private BigDecimal offeredMaxFee;

    public long getInnerId() {
        return innerId;
    }

    public void setInnerId(long innerId) {
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

    public BigDecimal getOfferedMaxFee() {
        return offeredMaxFee;
    }

    public void setOfferedMaxFee(BigDecimal offeredMaxFee) {
        this.offeredMaxFee = offeredMaxFee;
    }

    public ByteBuffer getSignature() {
        return signature;
    }

    public void setSignature(ByteBuffer signature) {
        this.signature = signature;
    }
}