package com.credits.leveldb.client.util;

import com.credits.common.utils.Utils;
import com.credits.crypto.Ed25519;
import com.credits.leveldb.client.data.TransactionFlowData;
import com.credits.leveldb.client.exception.LevelDbClientException;

import java.math.BigDecimal;

public class Validator {
    /**
     * Checking transaction hash
     * @param hash
     * @throws LevelDbClientException
     */
    public static void validateTransactionHash(String hash) throws LevelDbClientException {

        if (hash.length() != 8) {
            throw new LevelDbClientException(String.format("Invalid transaction hash: [%s]. Example of valid hash: [%s]", hash, "8iif6oqo"));
        }
    }

    /**
     * Checking the correctness of the wallet to which the transaction is being made (transfer)
     * @param toAddress
     */
    public static void validateToAddress(String toAddress) throws LevelDbClientException {
        // Check: is toAddress the correct public key of the provider ED25519
        try {
            byte[] toAddressBytes = com.credits.common.utils.Converter.decodeFromBASE58(toAddress);
            Ed25519.bytesToPublicKey(toAddressBytes);
        } catch (Exception e) {
            throw new LevelDbClientException("Invalid To address");
        }
    }

    public static void validateTransactionFlowData(TransactionFlowData transactionFlowData) throws LevelDbClientException {

        if (Utils.isEmpty(transactionFlowData.getSource())) {
            throw new LevelDbClientException("source is empty");
        }
        if (Utils.isEmpty(transactionFlowData.getTarget())) {
            throw new LevelDbClientException("target is empty");
        }
        if (Utils.isEmpty(transactionFlowData.getSignature())) {
            throw new LevelDbClientException("signature is empty");
        }
        validateTransactionAmount(transactionFlowData.getAmount());
        validateTransactionBalance(transactionFlowData.getBalance());
    }

    public static void validateTransactionAmount(BigDecimal amount) throws LevelDbClientException {

        int numberOfDecimalPlaces = Utils.getNumberOfDecimalPlaces(amount);
        if (numberOfDecimalPlaces > 18) {
            throw new LevelDbClientException(String.format("Invalid transaction amount %s, number of decimal places %s larger 18",
                    amount,
                    numberOfDecimalPlaces)
            );
        }
    }

    public static void validateTransactionBalance(BigDecimal balance) throws LevelDbClientException {

        int numberOfDecimalPlaces = Utils.getNumberOfDecimalPlaces(balance);
        if (numberOfDecimalPlaces > 18) {
            throw new LevelDbClientException(String.format("Invalid transaction balance %s, number of decimal places %s larger 18",
                    balance,
                    numberOfDecimalPlaces)
            );
        }
    }
}
