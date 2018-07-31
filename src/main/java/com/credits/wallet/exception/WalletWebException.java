package com.credits.wallet.exception;

import com.credits.common.exception.CreditsCommonException;
import com.credits.common.exception.CreditsException;
import com.credits.leveldb.client.exception.CreditsNodeException;
import com.credits.leveldb.client.exception.LevelDbClientException;

/**
 * Класс создан для передачи ошибки на фронтэнд.
 * Пример использования:
 * throw new WalletWebException("КОД ОШИБКИ", "Текст ошибки");
 * На фронтэнд возвращается HTTP 500 и response body
 * {"errorCode":"КОД ОШИБКИ","errorMessage":"Текст ошибки"}
 * <p>
 * Created by Rustem.Saidaliyev on 01.02.2018.
 */
public class WalletWebException extends RuntimeException {

    private String errorCode;

    public WalletWebException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
