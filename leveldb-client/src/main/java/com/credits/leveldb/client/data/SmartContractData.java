package com.credits.leveldb.client.data;

import com.credits.thrift.generated.Variant;

import java.nio.ByteBuffer;
import java.util.List;

public class SmartContractData {

    private byte[] address;
    private byte[] deployer;
    private String sourceCode;
    private byte[] byteCode;
    private String hashState;

    public SmartContractData(
            byte[] address,
            byte[] deployer,
            String sourceCode,
            byte[] byteCode,
            String hashState
    ) {
        this.setAddress(address);
        this.setDeployer(deployer);
        this.setSourceCode(sourceCode);
        this.setByteCode(byteCode);
        this.setHashState(hashState);
    }

    public byte[] getAddress() {
        return address;
    }

    public void setAddress(byte[] address) {
        this.address = address;
    }

    public byte[] getDeployer() {
        return deployer;
    }

    public void setDeployer(byte[] deployer) {
        this.deployer = deployer;
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
}
