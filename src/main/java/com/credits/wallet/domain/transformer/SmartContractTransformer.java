package com.credits.wallet.domain.transformer;

import com.credits.common.utils.sourcecode.SourceCodeUtils;
import com.credits.leveldb.client.data.SmartContractData;
import com.credits.wallet.domain.smartcontract.toweb.Method;
import com.credits.wallet.domain.smartcontract.toweb.Param;
import com.credits.wallet.domain.smartcontract.toweb.SmartContract;
import com.credits.wallet.domain.smartcontract.toweb.SourceCode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SmartContractTransformer {
    public static Function<SmartContractData, SmartContract> TO_WALLET = smartContractData -> {
        String sourceCodeBody = smartContractData.getSourceCode();
        List<MethodDeclaration> methodDeclarations = SourceCodeUtils.parseMethods(sourceCodeBody);
        List<Method> methods = new ArrayList<>();
        methodDeclarations.forEach(methodDeclaration -> {
            methodDeclaration.setBody(null);
            List<SingleVariableDeclaration> parameters = SourceCodeUtils.getMethodParameters(methodDeclaration);
            List<Param> params = new ArrayList<>();
            parameters.forEach(parameter -> {
                Param param = new Param(parameter.getName().toString(), parameter.getType().toString());
                params.add(param);
            });
            Method method = new Method(methodDeclaration.toString(), params);
            methods.add(method);
        });
        String sourceCodeFormatted = SourceCodeUtils.formatSourceCode(sourceCodeBody);
        SourceCode sourceCode = new SourceCode(sourceCodeFormatted, methods);
        return new SmartContract(
                smartContractData.getAddress(),
                sourceCode,
                smartContractData.getHashState(),
                smartContractData.getMethod(),
                //smartContractData.getParams() TODO
                null
        );
    };
}
