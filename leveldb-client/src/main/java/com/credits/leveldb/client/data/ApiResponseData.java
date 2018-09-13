package com.credits.leveldb.client.data;

import com.credits.thrift.generated.Variant;

public class ApiResponseData {
    private byte code;
    private String message;
    private Variant scExecRetVal;

    public ApiResponseData(
            byte code,
            String message,
            Variant scExecRetVal
    ) {
        this.code = code;
        this.message = message;
        this.scExecRetVal = scExecRetVal;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Variant getScExecRetVal() {
        return scExecRetVal;
    }

    public void setScExecRetVal(Variant scExecRetVal) {
        this.scExecRetVal = scExecRetVal;
    }
}

