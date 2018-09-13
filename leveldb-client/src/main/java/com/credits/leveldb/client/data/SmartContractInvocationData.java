package com.credits.leveldb.client.data;

import java.nio.ByteBuffer;
import java.util.List;

public class SmartContractInvocationData {

    private String sourceCode;
    private byte[] byteCode;
    private String hashState;
    private String method;
    private List<String> params;
    private boolean forgetNewState;

    public SmartContractInvocationData(String sourceCode, byte[] byteCode, String hashState, String method, List<String> params, boolean forgetNewState) {
        this.sourceCode = sourceCode;
        this.byteCode = byteCode;
        this.hashState = hashState;
        this.method = method;
        this.params = params;
        this.forgetNewState = forgetNewState;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public byte[] getByteCode() {
        return byteCode;
    }

    public void setByteCode(byte[] byteCode) {
        this.byteCode = byteCode;
    }

    public String getHashState() {
        return hashState;
    }

    public void setHashState(String hashState) {
        this.hashState = hashState;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public boolean isForgetNewState() {
        return forgetNewState;
    }

    public void setForgetNewState(boolean forgetNewState) {
        this.forgetNewState = forgetNewState;
    }
}
