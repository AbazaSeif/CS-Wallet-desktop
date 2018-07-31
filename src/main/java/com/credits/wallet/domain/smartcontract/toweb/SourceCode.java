package com.credits.wallet.domain.smartcontract.toweb;

import java.io.Serializable;
import java.util.List;

public class SourceCode implements Serializable {

    private String sourceCodeBody;
    private List<Method> methods;

    public SourceCode(
            String sourceCodeBody,
            List<Method> methods
    ) {
        this.sourceCodeBody = sourceCodeBody;
        this.methods = methods;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    public String getSourceCodeBody() {
        return sourceCodeBody;
    }

    public void setSourceCodeBody(String sourceCodeBody) {
        this.sourceCodeBody = sourceCodeBody;
    }
}
