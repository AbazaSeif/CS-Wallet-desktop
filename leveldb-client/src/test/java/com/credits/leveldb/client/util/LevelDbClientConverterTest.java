package com.credits.leveldb.client.util;

import com.credits.common.exception.CreditsCommonException;
import com.credits.leveldb.client.exception.LevelDbClientException;
import com.credits.leveldb.client.thrift.Amount;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class LevelDbClientConverterTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LevelDbClientConverterTest.class);

    @Ignore //Method isn't used
    @Test
    public void doubleToAmountTest() throws LevelDbClientException {
        Amount amount = LevelDbClientConverter.doubleToAmount(99.999);
        LOGGER.info("doubleToAmountTest, {}", LevelDbClientConverter.doubleToAmount(99.999));
        Assert.assertEquals(99, amount.getIntegral());
        Assert.assertEquals(999000000000000000L, amount.getFraction());
    }

    @Ignore //Method isn't used
    @Test
    public void amountToDoubleTest() {
        Amount amount = new Amount(0, 390000000000000L);
        LOGGER.info("amountToDoubleTest, {}", LevelDbClientConverter.amountToDouble(amount));
    }

    @Test
    public void bigDecimalToAmountTest() throws CreditsCommonException, LevelDbClientException {
        String valueAsString ="1.1111111111111";
        BigDecimal value = com.credits.common.utils.Converter.toBigDecimal(valueAsString);
        LOGGER.info("bigDecimalToAmountTest, {}", LevelDbClientConverter.bigDecimalToAmount(value));
        Assert.assertEquals(1, value.intValue());
        Assert.assertEquals(1111111111111L, value.longValue());
    }

    @Test
    public void amountToBigDecimalTest() throws LevelDbClientException {
        int integral =1111111111;
        long fraction =999999999999999999L;
        Amount amount = new Amount(integral, fraction);
        BigDecimal bigDecimalValue = LevelDbClientConverter.amountToBigDecimal(amount);
        LOGGER.info("amountToBigDecimalTest, {}", bigDecimalValue);
        Assert.assertEquals((Object) 1111111111.999999999999999999, bigDecimalValue.doubleValue());
    }
}
