package com.credits.leveldb.client;

import com.credits.common.exception.CreditsException;
import com.credits.leveldb.client.exception.LevelDbClientException;
import com.credits.leveldb.client.thrift.Amount;
import com.credits.leveldb.client.util.LevelDbClientConverter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class LevelDbClientConverterTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LevelDbClientConverterTest.class);

    @Test
    public void doubleToAmountTest() {
        try {
            LOGGER.info("doubleToAmountTest, {}", LevelDbClientConverter.doubleToAmount(99.999));
        } catch (LevelDbClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void amountToDoubleTest() {
        Amount amount = new Amount(0, 390000000000000L);

            LOGGER.info("amountToDoubleTest, {}", LevelDbClientConverter.amountToDouble(amount));
    }

    @Test
    public void bigDecimalToAmountTest() {
        try {
            String valueAsString =
//                    "111111119.1111111111111111111"
//                    "0.1111111111111111"
//                    "0.1"
                    "0.1000000000000000055511151231257827021181583404541015625"
//                    "1.55555555555555"
                    ;
            BigDecimal value = null;
            try {
                value = com.credits.common.utils.Converter.toBigDecimal(valueAsString);
            } catch (CreditsException e) {
                e.printStackTrace();
                assert false;
            }
            LOGGER.info("bigDecimalToAmountTest, {}", LevelDbClientConverter.bigDecimalToAmount(value));
        } catch (LevelDbClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void amountToBigDecimalTest() {

        int integral =
//                123456789
                1111111111
                ;
        long fraction =
//                123456789012345678L
                999999999999999999L
                ;
        Amount amount = new Amount(integral, fraction);
        try {
            LOGGER.info("amountToBigDecimalTest, {}", LevelDbClientConverter.amountToBigDecimal(amount));
        } catch (LevelDbClientException e) {
            e.printStackTrace();
        }
    }
}
