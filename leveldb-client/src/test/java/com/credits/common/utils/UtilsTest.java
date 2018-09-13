package com.credits.common.utils;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class UtilsTest {

    @Test
    public void parseSubArrayTest() {
        byte[] bytes = new byte[64];
        for (int i = 0; i < 64; i++) {
            bytes[i] = (byte) i;
        }
        byte[] seedArray = Utils.parseSubarray(bytes, 0, 32);
        Assert.assertEquals(32, seedArray.length);
        Assert.assertEquals(0, seedArray[0]);
        Assert.assertEquals(31, seedArray[31]);

    }

    @Test
    public void concatinateArrayTest() {
        byte[] arr1 = new byte[32];
        byte[] arr2 = new byte[32];
        for (int i = 0; i < 32; i++) {
            arr1[i] = (byte) i;
            arr2[i] = (byte) i;
        }
        byte[] concatenatedArray = Utils.concatinateArrays(arr1, arr2);
        Assert.assertEquals(64, concatenatedArray.length);
        Assert.assertEquals(0, concatenatedArray[0]);
        Assert.assertEquals(31, concatenatedArray[31]);
        Assert.assertEquals(0, concatenatedArray[32]);
        Assert.assertEquals(31, concatenatedArray[63]);

    }

    @Test
    public void randomAlphaNumericTest() {
        String string = Utils.randomAlphaNumeric(29);
        Assert.assertEquals(29, string.length());
    }
}
