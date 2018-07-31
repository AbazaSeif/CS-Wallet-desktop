package com.credits.wallet.domain.smartcontract.toweb;

import java.io.Serializable;
import java.util.List;

public class Param implements Serializable {

    private String name;
    private String type;

    public Param(
            String name,
            String type
    ) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
