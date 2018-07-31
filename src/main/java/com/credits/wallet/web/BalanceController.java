package com.credits.wallet.web;

import com.credits.common.exception.CreditsException;
import com.credits.leveldb.client.exception.CreditsNodeException;
import com.credits.leveldb.client.exception.LevelDbClientException;
import com.credits.wallet.exception.WalletWebException;
import com.credits.wallet.utils.WalletWebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@CrossOrigin
public class BalanceController extends AbstractController {

    private static Logger LOGGER = LoggerFactory.getLogger(BalanceController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/balance")
    public BigDecimal getBalance(@RequestParam String address, @RequestParam byte currency) {
        LOGGER.info("Getting balance for [{}], currency [{}]", address, currency);
        try {
            BigDecimal balance = apiClient.getBalance(address, currency);
            LOGGER.info("Received [{}]", balance);
            return balance;
        } catch (CreditsException e) {
            throw new WalletWebException(ERROR_CODE, WalletWebUtils.createWalletWebExceptionMessage(e));
        }
    }
}
