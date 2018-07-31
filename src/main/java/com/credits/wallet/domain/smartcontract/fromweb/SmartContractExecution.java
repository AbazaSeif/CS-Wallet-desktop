package com.credits.wallet.domain.smartcontract.fromweb;

import java.util.List;

public class SmartContractExecution {

    private String smartContractHashState;
    private String smartContractAddress;
    private String executionMethod;
    private List<String> executionMethodParamsVals;
    private long transactionInnerId;
    private String transactionSource;
    private String signatureBase58;

    public String getSmartContractHashState() {
        return smartContractHashState;
    }

    public void setSmartContractHashState(String smartContractHashState) {
        this.smartContractHashState = smartContractHashState;
    }

    public String getSmartContractAddress() {
        return smartContractAddress;
    }

    public void setSmartContractAddress(String smartContractAddress) {
        this.smartContractAddress = smartContractAddress;
    }

    public String getExecutionMethod() {
        return executionMethod;
    }

    public void setExecutionMethod(String executionMethod) {
        this.executionMethod = executionMethod;
    }

    public List<String> getExecutionMethodParamsVals() {
        return executionMethodParamsVals;
    }

    public void setExecutionMethodParamsVals(List<String> executionMethodParamsVals) {
        this.executionMethodParamsVals = executionMethodParamsVals;
    }

    public long getTransactionInnerId() {
        return transactionInnerId;
    }

    public void setTransactionInnerId(long transactionInnerId) {
        this.transactionInnerId = transactionInnerId;
    }

    public String getTransactionSource() {
        return transactionSource;
    }

    public void setTransactionSource(String transactionSource) {
        this.transactionSource = transactionSource;
    }

    public String getSignatureBase58() {
        return signatureBase58;
    }

    public void setSignature(String signatureBase58) {
        this.signatureBase58 = signatureBase58;
    }
}
