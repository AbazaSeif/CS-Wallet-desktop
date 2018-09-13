package com.credits.leveldb.client.util;

import com.credits.common.utils.Converter;
import com.credits.leveldb.client.data.SmartContractData;
import com.credits.leveldb.client.exception.LevelDbClientException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class ApiClientUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiClientUtils.class);

    @Test
    public void serializeByThriftTest01() throws LevelDbClientException {
        SmartContractData smartContractData = new SmartContractData(
            "address".getBytes(),
            "deployer".getBytes(),
            "sourceCode",
            null,
            "hashState"
        );

        byte[] smartContractBytes = ApiClientUtils.serializeByThrift(smartContractData);
        LOGGER.info(Converter.encodeToBASE58(smartContractBytes));
    }
}
