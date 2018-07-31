package com.credits.wallet.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by Rustem.Saidaliyev on 01.02.2018.
 */
@ControllerAdvice
public class ExceptionHandlingController {

    private static Logger logger = LoggerFactory.getLogger(ExceptionHandlingController.class);

    @ExceptionHandler(WalletWebException.class)
    public ExceptionResponse errorOccured(WalletWebException walletWebException) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode(walletWebException.getErrorCode());
        response.setErrorMessage(walletWebException.getMessage());

        logger.error(walletWebException.getMessage(), walletWebException);

        return response;
    }
}
