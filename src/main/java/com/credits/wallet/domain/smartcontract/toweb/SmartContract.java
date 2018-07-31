package com.credits.wallet.domain.smartcontract.toweb;

import java.io.Serializable;
import java.util.List;

public class SmartContract implements Serializable {
    private String address;
    private SourceCode sourceCode;
    private String hashState;

    public SmartContract(
            String address,
            SourceCode sourceCode,
            String hashState,
            String method,
            List<String> params
    ) {
        this.address = address;
        this.sourceCode = sourceCode;
        this.hashState = hashState;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHashState() {
        return hashState;
    }

    public void setHashState(String hashState) {
        this.hashState = hashState;
    }

    public SourceCode getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(SourceCode sourceCode) {
        this.sourceCode = sourceCode;
    }
}
