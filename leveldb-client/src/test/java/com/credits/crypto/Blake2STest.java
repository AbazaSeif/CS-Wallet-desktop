package com.credits.crypto;

import com.credits.common.utils.Converter;
import com.credits.crypto.exception.CreditsCryptoException;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Blake2STest {
    private static Logger LOGGER = LoggerFactory.getLogger(Blake2STest.class);

    @Test
    public void generateHashTest() throws CreditsCryptoException {
        byte[] hash = Blake2S.generateHash(4);
        Assert.assertEquals(4, hash.length);
        LOGGER.info("hash = {}", Converter.bytesToHex(hash));
    }
}
