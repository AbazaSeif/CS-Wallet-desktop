package com.credits.wallet.utils;

import com.credits.common.exception.CreditsException;
import com.credits.leveldb.client.exception.CreditsNodeException;
import com.credits.leveldb.client.exception.LevelDbClientException;

public class WalletWebUtils {
    public static String createWalletWebExceptionMessage(CreditsException creditsException) {
        if (creditsException instanceof LevelDbClientException) {
            return String.format("LevelDB client error: %s", creditsException.getMessage());
        }
        if (creditsException instanceof CreditsNodeException) {
            return String.format("Credits Node error: %s", creditsException.getMessage());
        }
        return creditsException.getMessage();
    }
}
