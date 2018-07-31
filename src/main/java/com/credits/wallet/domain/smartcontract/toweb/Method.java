package com.credits.wallet.domain.smartcontract.toweb;

import java.io.Serializable;
import java.util.List;

public class Method implements Serializable {

    private String name;
    private List<Param> params;

    public Method(
            String name,
            List<Param> params
    ) {
        this.name = name;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }
}
