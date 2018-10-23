package com.credits.leveldb.client.util;

public enum TransactionTypeEnum {
    DEPLOY_SMARTCONTRACT("Deploy smart contract"),
    EXECUTE_SMARTCONTRACT("Execute smart contract"),
    EXECUTE_TRANSACTION("Execute transaction");

    private String value;

    TransactionTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
