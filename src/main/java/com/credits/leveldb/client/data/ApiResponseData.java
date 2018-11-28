package com.credits.leveldb.client.data;

/**
 * Created by Rustem Saidaliyev on 17.05.2018.
 */
public class ApiResponseData {
    private byte code;
    private String message;

    public ApiResponseData(
            byte code,
            String message
    ) {
        this.code = code;
        this.message = message;
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
}
