package com.credits.wallet.desktop.struct;

public class TransactionTabRow {
    private long innerId;
    private String target;
    private byte currency;
    private String amount;

    public long getInnerId() {
        return innerId;
    }

    public void setInnerId(long innerId) {
        this.innerId = innerId;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public byte getCurrency() {
        return currency;
    }

    public void setCurrency(byte currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
