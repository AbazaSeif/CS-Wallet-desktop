package com.credits.common.utils;

import com.credits.common.exception.CreditsCommonException;
import com.credits.common.exception.CreditsException;
import com.credits.crypto.Ed25519;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Arrays;

public class ConverterTest {
    private static Logger LOGGER = LoggerFactory.getLogger(ConverterTest.class);

    @Test
    public void toStringTest() {
        Assert.assertEquals("1,0000000000001", Converter.toString(1.0000000000001));
        Assert.assertEquals("1000000001", Converter.toString(1000000001));
        Assert.assertEquals("111111111111111111", Converter.toString(111111111111111111L));
        Assert.assertEquals("2222222222222222222", Converter.toString(new BigDecimal(2222222222222222222L)));
    }

    @Test
    public void decodeFromBASE64Test() {
        String text = "KoX94CTSk7v7b28dzN7hXNvYPn8wxn7cStYfY3F7hMn";
        byte[] bytes = Converter.decodeFromBASE64(text);
        Assert.assertEquals(32, bytes.length);
        LOGGER.info(String.valueOf(bytes.length));
    }

    @Test
    public void base64EncodeDecodeTest() {
        byte[] data1 = "Hello World!!!".getBytes();
        LOGGER.info(Arrays.toString(data1));
        byte[] data2 = Converter.decodeFromBASE64(Converter.encodeToBASE64(data1));
        LOGGER.info(Arrays.toString(data2));
        Assert.assertArrayEquals(data1, data2);
    }

    @Test
    public void decodeFromBASE58Test() throws CreditsCommonException {
        String text = "aumu8afRGeQf5viweyNEuYoZzHvnpCahxoAeonmT5qUm";
        byte[] bytes = Converter.decodeFromBASE58(text);
        Assert.assertEquals(33, bytes.length);
        LOGGER.info(String.valueOf(bytes.length));
    }

    @Test
    public void encodeToBASE58Test01() {
        byte[] bytes = {1,2,3};
        String text = Converter.encodeToBASE58(bytes);
        Assert.assertEquals("Ldp", text);
        LOGGER.info(text);
    }

    @Test
    public void encodeToBASE58Test02() {
        int count = 0;
        for(int i = 0; i < 100; i++) {
            KeyPair keyPair = Ed25519.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            byte[] publicKeyBytes = Ed25519.publicKeyToBytes(publicKey);
            String publicKeyBASE58 = Converter.encodeToBASE58(publicKeyBytes);
            byte[] bytes = publicKeyBASE58.getBytes(StandardCharsets.US_ASCII);
            if (bytes.length != 44) {
                count++;
            }
        }
        LOGGER.info("count = {}", count);
    }

    @Test
    public void base58EncodeDecodeTest() throws CreditsCommonException {
        byte[] data1 = "KoX94CTSk7v7b28dzN7hXNvYPn8wxn7cStYfY3F7hMn".getBytes();
        LOGGER.info(Arrays.toString(data1));
        byte[] data2 = Converter.decodeFromBASE58(Converter.encodeToBASE58(data1));
        LOGGER.info(Arrays.toString(data2));
        Assert.assertArrayEquals(data1, data2);
    }

    @Test
    public void base58DecodeTest01() throws CreditsCommonException {
        String base58String = "Ks8XUU7MRbGWGk7CJ5b2fn1zL2zQUoLP7oZUJaAT1UxTjViUxNjfuU9ttQiyBmVsXicoAeeoMMaEGQ5icVnBsXL";
        byte[] bytes = Converter.decodeFromBASE58(base58String);
        StringBuilder builder = new StringBuilder();
        for(byte b : bytes) {
            builder.append(b + ",");
        }
        LOGGER.info(builder.toString());
    }


    @Test
    public void toBigDecimalTest() throws CreditsCommonException {
        String longString = "111111111111111111L";
        String intString = "1111111111";
        String doubleString = "1.1111111111111111";
        Long longValue = Converter.toBigDecimal(longString).longValue();
        Integer integerValue = Converter.toBigDecimal(intString).intValue();
        Double doubleValue = Converter.toBigDecimal(doubleString).doubleValue();
        LOGGER.info("Long value = " + longValue);
        LOGGER.info("Integer value = " + integerValue);
        LOGGER.info("Double value = " + doubleValue);
        Assert.assertEquals(111111111111111111L, (Object) longValue);
        Assert.assertEquals(1111111111, (Object) integerValue);
        Assert.assertEquals(1.1111111111111, (Object) doubleValue);
    }
}
