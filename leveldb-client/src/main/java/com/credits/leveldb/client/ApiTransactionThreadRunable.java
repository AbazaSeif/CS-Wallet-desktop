package com.credits.leveldb.client;

import com.credits.common.utils.Converter;
import com.credits.leveldb.client.data.ApiResponseData;
import com.credits.leveldb.client.exception.CreditsNodeException;
import com.credits.leveldb.client.pool.ThriftClientPool;
import com.credits.leveldb.client.thrift.API;
import com.credits.leveldb.client.thrift.Transaction;
import com.credits.leveldb.client.thrift.TransactionFlowResult;
import com.credits.leveldb.client.util.ApiAlertUtils;
import com.credits.leveldb.client.util.ApiClientUtils;
import com.credits.leveldb.client.util.LevelDbClientConverter;
import com.credits.leveldb.client.util.TransactionTypeEnum;
import com.credits.thrift.generated.Variant;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import java.util.Map;

public class ApiTransactionThreadRunable implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiTransactionExecutor.class);
    private Transaction transaction;
    private API.Client client;
    private TransactionTypeEnum transactionTypeEnum;
    private ThriftClientPool<API.Client> pool;

    ApiTransactionThreadRunable(Transaction transaction, API.Client client, ThriftClientPool<API.Client> pool,
        TransactionTypeEnum transactionTypeEnum) {
        this.transactionTypeEnum = transactionTypeEnum;
        this.client = client;
        this.transaction = transaction;
        this.pool = pool;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(Arrays.toString(transaction.getTarget()));
        String source = Converter.encodeToBASE58(transaction.getSource());
        Map<String, Transaction> sourceMap = ApiTransactionExecutor.sourceMap.get(source);
        try {
            LOGGER.info("Start execute transaction");
            sourceMap.put(String.valueOf(transaction.getId()), transaction);
            TransactionFlowResult result = client.TransactionFlow(transaction);
            ApiResponseData resultData = LevelDbClientConverter.apiResponseToApiResponseData(result.getStatus(),
                result.isSetSmart_contract_result() ? result.getSmart_contract_result() : null);
            createSuccessAlert(resultData);
            pool.returnResource(client);
            ApiClientUtils.logApiResponse(result.getStatus());
            ApiClientUtils.processApiResponse(result.getStatus());
            LOGGER.info("End execute transaction");
        } catch (TException e) {
            LOGGER.info(e.getMessage());
            ApiAlertUtils.showAlertError(transactionTypeEnum.getValue() + " was execute with timeout exception");
            pool.returnBrokenResource(client);
        } catch (CreditsNodeException e) {
            LOGGER.info(e.getMessage());
            ApiAlertUtils.showAlertError(transactionTypeEnum.getValue() + " was execute with node exception");
            pool.returnBrokenResource(client);
        } finally {
            sourceMap.remove(String.valueOf(transaction.getId()));
        }
    }

    private void createSuccessAlert(ApiResponseData resultData) {
        if (resultData.getCode() == ApiClient.API_RESPONSE_SUCCESS_CODE) {
            if (transactionTypeEnum.equals(TransactionTypeEnum.EXECUTE_TRANSACTION)) {
                ApiAlertUtils.showAlertInfo(transactionTypeEnum.getValue() + " was success");
            } else if (transactionTypeEnum.equals(TransactionTypeEnum.DEPLOY_SMARTCONTRACT)) {
                successDeploySmartProcess();
            } else if (transactionTypeEnum.equals(TransactionTypeEnum.EXECUTE_SMARTCONTRACT)) {
                Variant res = resultData.getScExecRetVal();
                if (res != null) {
                    String retVal = res.toString() + '\n';
                    LOGGER.info("Return value is {}", retVal);
                    ApiAlertUtils.showAlertInfo(
                        transactionTypeEnum.getValue() + " was success: return value is: " + retVal);
                } else {
                    ApiAlertUtils.showAlertInfo(transactionTypeEnum.getValue() + " was success");
                }
            }
        } else {
            ApiAlertUtils.showAlertWarning(transactionTypeEnum.getValue() + "is not execute");
        }
    }

    private void successDeploySmartProcess() {
        String target = Converter.encodeToBASE58(transaction.getTarget());
        StringSelection selection = new StringSelection(target);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        ApiAlertUtils.showAlertInfo(
            String.format("Smart-contract address\n\n%s\n\nhas generated and copied to clipboard", target));
    }


}
