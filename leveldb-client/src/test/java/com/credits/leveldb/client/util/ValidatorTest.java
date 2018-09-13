package com.credits.leveldb.client.util;

import com.credits.leveldb.client.exception.LevelDbClientException;
import com.credits.leveldb.client.util.Validator;
import org.junit.Test;

public class ValidatorTest {

    @Test
    public void validateTransactionHashTest() throws LevelDbClientException {
        String hash = "8iif6oqo";
        Validator.validateTransactionHash(hash);
    }

    @Test
    public void validateToAddressTest() throws LevelDbClientException {
        String toAddress = "HMXHATk9fRJrdHS1rkge5zgwHWXCPAnjUmiSxsGnBcq4";
        Validator.validateToAddress(toAddress);
    }
}
