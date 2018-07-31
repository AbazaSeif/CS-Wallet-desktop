package com.credits.wallet.domain;

public class HttpResponse {
    String value;

    public HttpResponse(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
