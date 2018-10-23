package com.credits.leveldb.client;

import com.credits.common.utils.Converter;
import com.credits.leveldb.client.data.*;
import com.credits.leveldb.client.exception.CreditsNodeException;
import com.credits.leveldb.client.exception.LevelDbClientException;
import com.credits.leveldb.client.pool.ThriftClientPool;
import com.credits.leveldb.client.thrift.*;
import com.credits.leveldb.client.util.ApiClientUtils;
import com.credits.leveldb.client.util.LevelDbClientConverter;
import com.credits.leveldb.client.util.TransactionTypeEnum;
import com.credits.leveldb.client.util.Validator;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiClient.class.getName());

    public static final byte API_RESPONSE_SUCCESS_CODE = 0;

    private static volatile ApiClient instance;

    // Singleton pattern
    // Double Checked Locking & volatile
    public static ApiClient getInstance(String host, Integer port) {
        ApiClient localInstance = instance;
        if (localInstance == null) {
            synchronized (ApiClient.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ApiClient(host, port);
                }
            }
        }
        return localInstance;
    }

    private ThriftClientPool<API.Client> pool;

    private String apiServerHost;
    private Integer apiServerPort;
    private ApiTransactionExecutor apiTransactionExecutor;

    private ApiClient(String apiServerHost, Integer apiServerPort) {
        this.apiTransactionExecutor = ApiTransactionExecutor.getInstance();
        this.apiServerHost = apiServerHost;
        this.apiServerPort = apiServerPort;
        this.initPool();
    }

    private void initPool() {

        GenericObjectPool.Config poolConfig = new GenericObjectPool.Config();
        poolConfig.maxActive = 500;
        poolConfig.minIdle = 500;
        poolConfig.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
        poolConfig.testOnBorrow = true;
        poolConfig.testWhileIdle = true;
        poolConfig.numTestsPerEvictionRun = 10;
        poolConfig.maxWait = 3000;

        pool = new ThriftClientPool<>(API.Client::new, poolConfig, this.apiServerHost, this.apiServerPort);
    }

    public ThriftClientPool<API.Client> getPool() {
        return pool;
    }

    public BigDecimal getBalance(byte[] address, byte currency) throws LevelDbClientException, CreditsNodeException {
        LOGGER.info(
            String.format("---> address = %s; currency = %s", Converter.byteArrayToString(address, null), currency));
        API.Client client = getClient();
        BalanceGetResult result;
        try {
            result = client.BalanceGet(ByteBuffer.wrap(address), currency);
            pool.returnResource(client);
        } catch (TException e) {
            pool.returnBrokenResource(client);
            throw new LevelDbClientException(e);
        }
        ApiClientUtils.processApiResponse(result.getStatus());
        Amount amount = result.getAmount();
        BigDecimal balance = LevelDbClientConverter.amountToBigDecimal(amount);
        LOGGER.info(String.format("<--- balance = %s", balance));
        return balance;
    }

    public List<TransactionData> getTransactions(byte[] address, long offset, long limit)
        throws LevelDbClientException, CreditsNodeException {

        API.Client client = getClient();
        List<TransactionData> transactionDataList = new ArrayList<>();
        TransactionsGetResult result = null;
        try {
            result = client.TransactionsGet(ByteBuffer.wrap(address), offset, limit);
            pool.returnResource(client);
        } catch (TException e) {
            pool.returnBrokenResource(client);
            throw new LevelDbClientException(e);
        }
        ApiClientUtils.processApiResponse(result.getStatus());
        for (SealedTransaction sealedTransaction : result.getTransactions()) {
            transactionDataList.add(LevelDbClientConverter.transactionToTransactionData(sealedTransaction));
        }
        return transactionDataList;
    }

    public TransactionData getTransaction(TransactionIdData transactionIdData)
        throws LevelDbClientException, CreditsNodeException {
        API.Client client = getClient();
        TransactionGetResult result = null;
        try {
            result = client.TransactionGet(LevelDbClientConverter.transactionIdDataToTransactionId(transactionIdData));
            pool.returnResource(client);
        } catch (TException e) {
            pool.returnBrokenResource(client);
            throw new LevelDbClientException(e);
        }
        ApiClientUtils.processApiResponse(result.getStatus());
        return LevelDbClientConverter.transactionToTransactionData(result.getTransaction());
    }

    public PoolData getPoolInfo(byte[] hash, long index) throws LevelDbClientException, CreditsNodeException {

        ByteBuffer hashByteBuffer = Converter.bytesToByteBuffer(hash);
        API.Client client = getClient();
        PoolInfoGetResult result = null;
        try {
            result = client.PoolInfoGet(hashByteBuffer, index);
            pool.returnResource(client);
        } catch (TException e1) {
            pool.returnBrokenResource(client);
            throw new LevelDbClientException(e1);
        }
        ApiClientUtils.processApiResponse(result.getStatus());

        if (!result.isIsFound()) {
            throw new CreditsNodeException(String.format("Pool by hash %s and index %s is not found", hash, index));
        }
        return LevelDbClientConverter.poolToPoolData(result.getPool());
    }

    public List<PoolData> getPoolList(Long offset, Long limit) throws LevelDbClientException, CreditsNodeException {
        API.Client client = getClient();
        List<PoolData> poolDataList = new ArrayList<>();
        PoolListGetResult result;
        try {
            result = client.PoolListGet(offset, limit);
            pool.returnResource(client);
        } catch (TException e) {
            pool.returnBrokenResource(client);
            throw new LevelDbClientException(e);
        }
        ApiClientUtils.processApiResponse(result.getStatus());
        for (Pool pool : result.getPools()) {
            poolDataList.add(LevelDbClientConverter.poolToPoolData(pool));
        }
        return poolDataList;

    }

    private ApiResponseData syncTransactionFlowInternal(Transaction transaction) throws LevelDbClientException {
        API.Client client = getClient();
        TransactionFlowResult result = apiTransactionExecutor.executeSyncTransactionFlow(transaction, client, pool);
        return LevelDbClientConverter.apiResponseToApiResponseData(result.getStatus());
    }

    private ApiResponseData asyncTransactionFlowInternal(Transaction transaction,
        TransactionTypeEnum transactionTypeEnum) throws LevelDbClientException {
        API.Client client = getClient();
        TransactionFlowResult result = apiTransactionExecutor.executeAsyncTransactionFlow(transaction, client, pool, transactionTypeEnum);
        return LevelDbClientConverter.apiResponseToApiResponseData(result.getStatus());
    }

     //Deploying a smart contract (Generating a transaction with a smart contract)
    public ApiResponseData deploySmartContract(long transactionInnerId, byte[] transactionSource,
        byte[] transactionTarget, SmartContractInvocationData smartContractInvocationData, byte[] signature,
        TransactionTypeEnum transactionTypeEnum)
        throws LevelDbClientException, CreditsNodeException {

        if (smartContractInvocationData == null) {
            throw new LevelDbClientException("Empty smart-contract");
        }

        SmartContractInvocation smartContractInvocation =
            LevelDbClientConverter.smartContractInvocationDataToSmartContractInvocation(smartContractInvocationData);

        LOGGER.info(String.format(
            "---> transactionInnerId = %s; transactionSource = %s; transactionTarget = %s; smartContractInvocation.hashState = %s",
            transactionInnerId, transactionSource, transactionTarget, smartContractInvocation.getHashState()));

        API.Client client = getClient();
        Transaction transaction =
            new Transaction(transactionInnerId, ByteBuffer.wrap(transactionSource), ByteBuffer.wrap(transactionTarget),
                null, null, (byte) 1, ByteBuffer.wrap(signature), null);

        transaction.setSmartContract(smartContractInvocation);
        transaction.setSmartContractIsSet(true);

        TransactionFlowResult result = apiTransactionExecutor.executeAsyncTransactionFlow(transaction, client, pool,
            transactionTypeEnum);
        ApiClientUtils.logApiResponse(result.getStatus());
        ApiClientUtils.processApiResponse(result.getStatus());
        return LevelDbClientConverter.apiResponseToApiResponseData(result.getStatus());
    }

     //Smart contract execution
    public ApiResponseData executeSmartContract(long transactionInnerId, byte[] transactionSource,
        byte[] transactionTarget, SmartContractInvocationData smartContractInvocationData, byte[] signature,
        TransactionTypeEnum transactionTypeEnum)
        throws LevelDbClientException, CreditsNodeException {

        if (smartContractInvocationData == null) {
            throw new LevelDbClientException("Empty smart-contract");
        }

        SmartContractInvocation smartContractInvocation =
            LevelDbClientConverter.smartContractInvocationDataToSmartContractInvocation(smartContractInvocationData);

        LOGGER.info(
            "---> transactionInnerId = {}; transactionSource = {}; transactionTarget = {}; smartContract.hashState = {}; " +
                "smartContract.method = {}; smartContract.params = {};", transactionInnerId, transactionSource,
            transactionTarget, smartContractInvocationData.getHashState(), smartContractInvocationData.getMethod(),
            smartContractInvocationData.getParams() == null ? "" : smartContractInvocationData.getParams().toArray());

        API.Client client = getClient();

        Transaction transaction =
            new Transaction(transactionInnerId, ByteBuffer.wrap(transactionSource), ByteBuffer.wrap(transactionTarget),
                null, null, (byte) 1, ByteBuffer.wrap(signature), null);
        transaction.setSmartContract(smartContractInvocation);
        transaction.setSmartContractIsSet(true);

        TransactionFlowResult result = apiTransactionExecutor.executeAsyncTransactionFlow(transaction, client, pool,
            transactionTypeEnum);
        ApiClientUtils.logApiResponse(result.getStatus());
        ApiClientUtils.processApiResponse(result.getStatus());
        return LevelDbClientConverter.apiResponseToApiResponseData(result.getStatus(),
            result.isSetSmart_contract_result() ? result.getSmart_contract_result() : null);
    }

     //Transaction generation
    public ApiResponseData transactionFlow(TransactionFlowData transactionFlowData, boolean checkBalance)
        throws LevelDbClientException, CreditsNodeException {
        return syncTransactionFlowInternal(transactionFlowProcess(transactionFlowData, checkBalance));
    }

    public ApiResponseData asyncTransactionFlow(TransactionFlowData transactionFlowData, boolean checkBalance,
        TransactionTypeEnum transactionTypeEnum)
        throws LevelDbClientException, CreditsNodeException {
        return asyncTransactionFlowInternal(transactionFlowProcess(transactionFlowData, checkBalance),transactionTypeEnum);
    }


    private Transaction transactionFlowProcess(TransactionFlowData transactionFlowData, boolean checkBalance)
        throws LevelDbClientException, CreditsNodeException {
        Validator.validateTransactionFlowData(transactionFlowData);

        long innerId = transactionFlowData.getInnerId();
        byte[] source = transactionFlowData.getSource();
        byte[] target = transactionFlowData.getTarget();
        BigDecimal amount = transactionFlowData.getAmount();
        BigDecimal balance = transactionFlowData.getBalance();
        byte currency = transactionFlowData.getCurrency();
        byte[] signature = transactionFlowData.getSignature();
        BigDecimal fee = transactionFlowData.getFee();

        if (checkBalance) {
            BigDecimal balanceFromNode = getBalance(source, currency);

            if (balanceFromNode.compareTo(amount) < 0) {
                throw new LevelDbClientException(
                    String.format("Wallet %s with balance [%s %s] is less than transaction amount [%s %s]",
                        Converter.encodeToBASE58(source), Converter.toString(balanceFromNode), currency,
                        Converter.toString(amount), currency));
            }
        }


        Amount serverAmount = LevelDbClientConverter.bigDecimalToAmount(amount);
        Amount serverBalance = LevelDbClientConverter.bigDecimalToAmount(balance);
        Amount serverFee = LevelDbClientConverter.bigDecimalToAmount(fee);


        LOGGER.info(String.format(
            "---> source = %s; target = %s; amount = %s; balance = %s; currency = %s; signature = %s; innerId = %s",
            source, target, serverAmount, serverBalance, currency, signature, innerId));


        return new Transaction(innerId, ByteBuffer.wrap(source), ByteBuffer.wrap(target), serverAmount, serverBalance,
            currency, ByteBuffer.wrap(signature), serverFee);

    }

    public SmartContractData getSmartContract(byte[] address) throws LevelDbClientException, CreditsNodeException {

        LOGGER.info(String.format("---> address = %s", Converter.byteArrayToString(address, null)));
        API.Client client = getClient();

        SmartContractGetResult result = null;
        try {
            result = client.SmartContractGet(ByteBuffer.wrap(address));
            pool.returnResource(client);
        } catch (TException e) {
            pool.returnBrokenResource(client);
            throw new LevelDbClientException(e);
        }
        ApiClientUtils.logApiResponse(result.getStatus());
        ApiClientUtils.processApiResponse(result.getStatus());
        SmartContract smartContract = result.getSmartContract();
        SmartContractData smartContractData = LevelDbClientConverter.smartContractToSmartContractData(smartContract);
        LOGGER.info(String.format("<--- smart contract hashState = %s", smartContractData.getHashState()));
        return smartContractData;
    }

    private API.Client getClient() throws LevelDbClientException {
        API.Client client;
        try {
            client = pool.getResource();
        } catch (ThriftClientPool.ThriftClientException e) {
            throw new LevelDbClientException(e);
        }
        return client;
    }

    public List<SmartContractData> getSmartContracts(byte[] address)
        throws LevelDbClientException, CreditsNodeException {
        LOGGER.info(String.format("---> wallet address = %s", Converter.byteArrayToString(address, null)));
        API.Client client = getClient();
        List<SmartContractData> smartContractDataList;

        SmartContractsListGetResult result = null;
        try {
            result = client.SmartContractsListGet(ByteBuffer.wrap(address));
            pool.returnResource(client);
        } catch (TException e) {
            pool.returnBrokenResource(client);
            throw new LevelDbClientException(e);
        }
        ApiClientUtils.logApiResponse(result.getStatus());
        ApiClientUtils.processApiResponse(result.getStatus());
        smartContractDataList = result.getSmartContractsList()
            .stream()
            .map(LevelDbClientConverter::smartContractToSmartContractData)
            .collect(Collectors.toList());
        return smartContractDataList;


    }

    public List<ByteBuffer> getSmartContractAddresses(byte[] address)
        throws LevelDbClientException, CreditsNodeException {
        LOGGER.info(String.format("---> wallet address = %s", Converter.byteArrayToString(address, null)));
        API.Client client = getClient();
        SmartContractAddressesListGetResult result = null;
        try {
            result = client.SmartContractAddressesListGet(ByteBuffer.wrap(address));
            pool.returnResource(client);
        } catch (TException e) {
            pool.returnBrokenResource(client);
            throw new LevelDbClientException(e);
        }
        ApiClientUtils.logApiResponse(result.getStatus());
        ApiClientUtils.processApiResponse(result.getStatus());
        return result.getAddressesList();
    }
}
