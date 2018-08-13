package com.credits.wallet.desktop.utils.struct;

import com.credits.common.exception.CreditsCommonException;
import com.credits.common.utils.Converter;
import com.credits.crypto.Ed25519;
import com.credits.crypto.exception.CreditsCryptoException;
import com.credits.leveldb.client.data.SmartContractData;
import com.credits.leveldb.client.exception.CreditsNodeException;
import com.credits.leveldb.client.exception.LevelDbClientException;
import com.credits.leveldb.client.util.ApiClientUtils;
import com.credits.wallet.desktop.test.init.TestState;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class TransactionStructTest {

    @Before
    public void init() {
        TestState.init();
    }

    private static Logger LOGGER = LoggerFactory.getLogger(TransactionStructTest.class);
    @Test
    public void test01() {
        try {
            byte currencyByte = 1;

            TransactionStruct transactionStruct = new TransactionStruct(
                    1532529576028L,
                    "5B3YXqDTcWQFGAqEJQJP3Bg1ZK8FFtHtgCiFLT5VAxpe",
                    "B3EBaHgRU7sd353axMRrZfoL9aL2XjA3oXejDdPrMnHR",
                    new BigDecimal("10.0"),
                    new BigDecimal("0.1"),
                    currencyByte,
                    null
            );

            byte[] transactionStructBytes = transactionStruct.getBytes();

            StringBuilder builder = new StringBuilder();
            for(byte b : transactionStructBytes) {
                builder.append(b + ",");
            }
            LOGGER.info(builder.toString());


            try {
                byte[] sign = Ed25519.sign(
                        transactionStructBytes,
                        Ed25519.bytesToPrivateKey(
                                Converter.decodeFromBASE58("3rUevsW5xfob6qDxWMDFwwTQCq39SYhzstuyfUGSDvF2QHBRyPD8fSk49wFXaPk3GztfxtuU85QHfMV3ozfqa7rN")
                        )
                );

                builder = new StringBuilder();

                for(byte b : sign) {
                    builder.append(b + ",");
                }
                LOGGER.info(builder.toString());

            } catch (CreditsCryptoException e) {
                e.printStackTrace();
            }

        } catch (CreditsCommonException e) {
            e.printStackTrace();
        } catch (LevelDbClientException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void test02() {
        try {
            byte currencyByte = 1;

            TransactionStruct transactionStruct = new TransactionStruct(
                    1532529576028L,
                    "5B3YXqDTcWQFGAqEJQJP3Bg1ZK8FFtHtgCiFLT5VAxpe",
                    "B3EBaHgRU7sd353axMRrZfoL9aL2XjA3oXejDdPrMnHR",
                    new BigDecimal("10.0"),
                    new BigDecimal("0.1"),
                    currencyByte,
                    null
            );

            for(byte b : transactionStruct.getBytes()) {
                LOGGER.info(b + "");
            }

        } catch (CreditsCommonException e) {
            e.printStackTrace();
        } catch (LevelDbClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test03() {
        try {
            byte currencyByte = 1;

            SmartContractData smartContractData = null;
            try {
                smartContractData = TestState.apiClient.getSmartContract("6RNiVco3yb6jsQPaJ7GzjjRFc9aH1Rnyp21HTAh64FRn".getBytes());
            } catch (CreditsNodeException e) {
                e.printStackTrace();
            }

            byte[] smartContractBytes = ApiClientUtils.serializeByThrift(smartContractData);

            TransactionStruct transactionStruct = new TransactionStruct(
                    1533280630882L,
                    "8J7oZawJadpRFoMpQb42ffV3HN5zxtLjUJfNN8jpxUgp",
                    "6RNiVco3yb6jsQPaJ7GzjjRFc9aH1Rnyp21HTAh64FRn",
                    new BigDecimal(0),
                    new BigDecimal(0),
                    currencyByte,
                    smartContractBytes
            );

            byte[] transactionStructBytes = transactionStruct.getBytes();

            StringBuilder builder = new StringBuilder();
            for(byte b : transactionStructBytes) {
                builder.append(b + " ");
            }
            LOGGER.info(builder.toString());

            try {
                byte[] sign = Ed25519.sign(
                        transactionStructBytes,
                        Ed25519.bytesToPrivateKey(
                                Converter.decodeFromBASE58("5LCVyPsn5XAhNDfP4CcSo1pJEWdsCbgrXzhpauVoNZKtRWygEaZ3LrvF6T5EmPYJGRx7pfZXiSZP8tpYn5X6qLvU")
                        )
                );

                builder = new StringBuilder();

                for(byte b : sign) {
                    builder.append(b + " ");
                }
                LOGGER.info(builder.toString());

            } catch (CreditsCryptoException e) {
                e.printStackTrace();
            }

        } catch (CreditsCommonException e) {
            e.printStackTrace();
        } catch (LevelDbClientException e) {
            e.printStackTrace();
        }
    }


}
