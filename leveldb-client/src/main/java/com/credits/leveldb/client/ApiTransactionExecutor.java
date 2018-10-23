package com.credits.leveldb.client;

import com.credits.common.utils.Converter;
import com.credits.leveldb.client.exception.LevelDbClientException;
import com.credits.leveldb.client.pool.ThriftClientPool;
import com.credits.leveldb.client.thrift.API;
import com.credits.leveldb.client.thrift.APIResponse;
import com.credits.leveldb.client.thrift.Transaction;
import com.credits.leveldb.client.thrift.TransactionFlowResult;
import com.credits.leveldb.client.util.TransactionTypeEnum;
import org.apache.thrift.TException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApiTransactionExecutor {
    private static final int N_THREADS = 3;
    private static volatile ApiTransactionExecutor instance;
    private ExecutorService threadPoolExecutor;

    static public Map<String, Map<String, Transaction>> sourceMap;

    public static ApiTransactionExecutor getInstance() {
        ApiTransactionExecutor localInstance = instance;
        if (localInstance == null) {
            synchronized (ApiClient.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ApiTransactionExecutor();
                }
            }
        }
        return localInstance;
    }

    private ApiTransactionExecutor() {
        sourceMap = new HashMap<>();
        threadPoolExecutor = Executors.newFixedThreadPool(N_THREADS);
    }

    TransactionFlowResult executeAsyncTransactionFlow(Transaction transaction, API.Client client,
        ThriftClientPool<API.Client> pool, TransactionTypeEnum transactionTypeEnum) throws LevelDbClientException {
        if (ApiTransactionExecutor.sourceMap.get(Converter.encodeToBASE58(transaction.getSource())) == null) {
            sourceMap.put(Converter.encodeToBASE58(transaction.getSource()),
                Collections.synchronizedMap(new HashMap<>()));
        }
        ApiTransactionThreadRunable threadRunable =
            new ApiTransactionThreadRunable(transaction, client, pool, transactionTypeEnum);
        threadPoolExecutor.submit(threadRunable);
        return new TransactionFlowResult().setStatus(new APIResponse());
    }

    TransactionFlowResult executeSyncTransactionFlow(Transaction transaction, API.Client client,
        ThriftClientPool<API.Client> pool) throws LevelDbClientException {
        TransactionFlowResult result;
        try {
            result = client.TransactionFlow(transaction);
            pool.returnResource(client);
        } catch (TException e) {
            pool.returnBrokenResource(client);
            throw new LevelDbClientException(e);
        }
        return result;
    }

}
