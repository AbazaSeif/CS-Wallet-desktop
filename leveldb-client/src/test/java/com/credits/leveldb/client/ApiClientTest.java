package com.credits.leveldb.client;

import com.credits.common.exception.CreditsCommonException;
import com.credits.common.utils.Converter;
import com.credits.common.utils.ThreadUtil;
import com.credits.leveldb.client.data.*;
import com.credits.leveldb.client.exception.CreditsNodeException;
import com.credits.leveldb.client.exception.LevelDbClientException;
import com.credits.leveldb.client.pool.ThriftClientPool;
import com.credits.leveldb.client.thrift.API;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.MINUTES;

public class ApiClientTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiClientTest.class);
    private static final String API_HOST =
            "localhost";
    private static final Integer API_PORT = 9090;
    private ApiClient apiClient = ApiClient.getInstance(API_HOST, API_PORT);

    @Test
    public void getBalanceTest() throws LevelDbClientException, CreditsNodeException {
        String walletBASE58 = "8J7oZawJadpRFoMpQb42ffV3HN5zxtLjUJfNN8jpxUgp";
        BigDecimal balance = null;
        try {
            balance = apiClient.getBalance(Converter.decodeFromBASE58(walletBASE58), (byte) 1);
        } catch (CreditsCommonException e) {
            e.printStackTrace();
        }
        LOGGER.info("Balance = {}", balance);
    }

    @Test
    @Ignore
    public void getBalanceTestInMultipleThreads() {
        ThriftClientPool<API.Client> pool = apiClient.getPool();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                try {
                    LOGGER.info("Balance = {}", apiClient.getBalance("1_GJnW172n4CdN0".getBytes(), (byte) 1));
                    LOGGER.info("Active client num in pool: {}, Idle client num in pool: {}", pool.getNumActive(), pool.getNumIdle());
                } catch (Throwable e) {
                    LOGGER.info(e.getMessage());
                }
            });
        }
        ThreadUtil.shutdownAndAwaitTermination(executorService, 1, MINUTES);
    }

    @Test
    public void getTransactionsTest() throws LevelDbClientException, CreditsNodeException {
        List<TransactionData> transactionDataList = null;
        try {
            transactionDataList = apiClient.getTransactions(Converter.decodeFromBASE58("bxG+hrz1KuOLZHNpTz69Vij4vXAYIB26c25zFH7EJ6g="), 0, 10);
        } catch (CreditsCommonException e) {
            e.printStackTrace();
            assert false;
        }
        LOGGER.info("transactionDataList = {}", transactionDataList);
    }

    @Test
    public void getTransactionTest() throws LevelDbClientException, CreditsNodeException {
        TransactionData transactionData = apiClient.getTransaction(
                new TransactionIdData("qwerty".getBytes(), 1)
        );
        LOGGER.info("transactionData = {}", transactionData);
    }

    @Test
    public void transactionFlowTest() throws LevelDbClientException, CreditsNodeException, CharacterCodingException {
        Charset charset = Charset.forName("UTF-8");
        CharsetEncoder encoder = charset.newEncoder();
        TransactionFlowData transactionFlowData = new TransactionFlowData(
            12327, "qwerty".getBytes(), "qwerty".getBytes(), new BigDecimal("0.123"), new BigDecimal("0.123"), (byte) 1,
            "signature".getBytes(),
            new BigDecimal("0.01")
        );
        ApiResponseData responseData = apiClient.transactionFlow(transactionFlowData, false);
        Assert.assertEquals(0, responseData.getCode());
    }

    @Test
    public void getSmartContractTest() throws LevelDbClientException, CreditsNodeException {
        String walletBASE58 = "AoRKdBEbozwTKt5sirqx6ERv2DPsrvTk81hyztnndgWC";
        SmartContractData smartContractData = null;
        try {
            smartContractData = apiClient.getSmartContract(Converter.decodeFromBASE58(walletBASE58));
        } catch (CreditsCommonException e) {
            e.printStackTrace();
            assert false;
        }
        LOGGER.info("Smart contract hashState = {}", smartContractData.getHashState());
    }

    @Test
    public void getSmartContractsTest() throws LevelDbClientException, CreditsNodeException {
        String walletBASE58 = "8J7oZawJadpRFoMpQb42ffV3HN5zxtLjUJfNN8jpxUgp";
        List<SmartContractData> smartContractDataList = null;
        try {
            smartContractDataList = apiClient.getSmartContracts(Converter.decodeFromBASE58(walletBASE58));
        } catch (CreditsCommonException e) {
            e.printStackTrace();
            assert false;
        }
        smartContractDataList.forEach(smartContractData ->
            LOGGER.info("sourceCode = {}", smartContractData.getSourceCode()));
    }

    @Test
    public void deploySmartContractTest() throws LevelDbClientException, CreditsNodeException {
        String address = "address";
        long transactionInnerId = 12327;
        String transactionSource = "4ESD7KpGzJCfDL8pZKhMfcfekqdoBdjSBUF5FiJdkBAC";
        String transactionTarget = "transactionTarget";
        String smartContractCode = "public class Contract extends SmartContract {\n" +
            "\n" +
            "    public Contract() {\n" +
            "        total = 0;\n" +
            "    }\n" +
            "}";
        SmartContractInvocationData smartContractInvocationData = new SmartContractInvocationData(
                "sourceCode",
                address.getBytes(),
                "hashState",
                "method",
                null,
                true);

        ApiResponseData responseData = null;
        try {
            responseData = apiClient.deploySmartContract(
                    transactionInnerId,
                    Converter.decodeFromBASE58(transactionSource),
                    Converter.decodeFromBASE58(transactionTarget),
                    smartContractInvocationData,
                    "signature01".getBytes()
            );
        } catch (CreditsCommonException e) {
            e.printStackTrace();
            assert false;
        }

        Assert.assertEquals(0, responseData.getCode());
    }
}
