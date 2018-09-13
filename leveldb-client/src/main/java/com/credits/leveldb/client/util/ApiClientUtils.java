package com.credits.leveldb.client.util;

import com.credits.leveldb.client.ApiClient;
import com.credits.leveldb.client.data.SmartContractData;
import com.credits.leveldb.client.data.SmartContractInvocationData;
import com.credits.leveldb.client.exception.CreditsNodeException;
import com.credits.leveldb.client.exception.LevelDbClientException;
import com.credits.leveldb.client.thrift.APIResponse;
import com.credits.leveldb.client.thrift.SmartContract;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializable;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiClientUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiClientUtils.class.getName());

    public static void logApiResponse(APIResponse apiResponse) {
        byte resultCode = apiResponse.getCode();
        String resultMessage = apiResponse.getMessage();
        LOGGER.info(String.format("<--- resultCode = %s; resultMessage = %s", resultCode, resultMessage));
    }

    public static void processApiResponse(APIResponse apiResponse) throws CreditsNodeException {
        byte resultCode = apiResponse.getCode();
        if (resultCode != ApiClient.API_RESPONSE_SUCCESS_CODE) {
            String resultMessage = apiResponse.getMessage();
            throw new CreditsNodeException(String.format("Credits Node error: %s", resultMessage));
        }
    }

    public static byte[] serializeByThrift(Object object) throws LevelDbClientException {
        TBase tBase;
        if (object == null) {
            throw new LevelDbClientException("object is null");
        }
        if (object.getClass().equals(SmartContractData.class)) {
            tBase = LevelDbClientConverter.smartContractDataToSmartContract((SmartContractData)object);
        } else if (object.getClass().equals(SmartContractInvocationData.class)) {
            tBase = LevelDbClientConverter.smartContractInvocationDataToSmartContractInvocation((SmartContractInvocationData)object);
        } else {
            throw new LevelDbClientException("Invalid TBase object");
        }
        TSerializer tSerializer = new TSerializer();
        try {
            return tSerializer.serialize(tBase);
        } catch (TException e) {
            throw new LevelDbClientException(e);
        }
    }
}
