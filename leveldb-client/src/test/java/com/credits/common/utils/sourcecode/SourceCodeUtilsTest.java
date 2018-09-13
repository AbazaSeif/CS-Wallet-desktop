package com.credits.common.utils.sourcecode;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SourceCodeUtilsTest {
    private static Logger LOGGER = LoggerFactory.getLogger(SourceCodeUtilsTest.class);

    private String sourceCode = "public class Contract extends SmartContract {" +
            "public Contract() {" +
            "  total = 0; " +
            "  } " +
            "public void method01(String arg01, String arg02) {" +
            "  total = 1;" +
            "}" +
            "}";

    @Test
    public void normalizeSourceCodeTest() {
        String expectedNormalizedCode = "public class Contract extends SmartContract {public Contract() " +
            "{ total = 0; } public void method01(String arg01, String arg02) { total = 1;}}";
        String normalizedSourceCode = SourceCodeUtils.normalizeSourceCode(sourceCode);
        Assert.assertEquals(expectedNormalizedCode, normalizedSourceCode);
        LOGGER.info("normalizedSourceCode =\n {}", normalizedSourceCode);
    }

    @Test
    public void parseMethodsTest() {
        List<MethodDeclaration> methods = SourceCodeUtils.parseMethods(this.sourceCode);
        Assert.assertEquals("method01", methods.get(0).getName().toString());
        methods.forEach(method -> LOGGER.info("method.getName() = {}", method.getName()));

    }

    @Test
    public void getMethodParametersTest() {
        List<MethodDeclaration> methodDeclarations = SourceCodeUtils.parseMethods(this.sourceCode);
        methodDeclarations.forEach(methodDeclaration -> {
            List parameters = SourceCodeUtils.getMethodParameters(methodDeclaration);
            Assert.assertEquals("String arg01", parameters.get(0).toString());
            LOGGER.info("parameters = {}", parameters);
        });
    }

    @Test
    public void formatSourceCodeTest() {
        String sourceCode = "public class Contract extends SmartContract {public Contract() {total = 0; }}";
        String expectedFormattedCode = "public class Contract extends SmartContract {\r\n" +
            "\tpublic Contract() {\r\n" +
            "\t\ttotal = 0;\r\n" +
            "\t}\r\n" +
            "}";
        String formattedSourceCode = SourceCodeUtils.formatSourceCode(sourceCode);
        Assert.assertEquals(expectedFormattedCode, formattedSourceCode);
        LOGGER.info("formattedSourceCode =\n {}", formattedSourceCode);
    }
}
