package com.credits.leveldb.client.data;

/**
 * Created by Rustem Saidaliyev on 16.05.2018.
 */
public class SmartContractData {

    private String address;
    private String sourceCode;
    private byte[] byteCode;
    private byte[] contractState;
    private String hashState;

    public SmartContractData(
            String address,
            String sourceCode,
            byte[] byteCode,
            byte[] contractState,
            String hashState
    ) {
        this.setAddress(address);
        this.setSourceCode(sourceCode);
        this.setByteCode(byteCode);
        this.setHashState(hashState);
        this.setContractState(contractState);
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getContractState() {
        return contractState;
    }

    public void setContractState(byte[] contractState) {
        this.contractState = contractState;
    }
}
