package com.credits.wallet.web;

import com.credits.common.exception.CreditsException;
import com.credits.leveldb.client.data.TransactionData;
import com.credits.wallet.domain.Transaction;
import com.credits.wallet.domain.transformer.TransactionTransformer;
import com.credits.wallet.domain.transformer.Transformer;
import com.credits.wallet.exception.WalletWebException;
import com.credits.wallet.utils.WalletWebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
public class TransactionController extends AbstractController {

    private static Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/transaction")
    public Transaction getTransaction(@RequestParam long innerId) {
        try {
            TransactionData transactionData = apiClient.getTransaction(String.valueOf(innerId));

            Transformer<TransactionData, Transaction> transformer = new Transformer<>(TransactionTransformer.TO_WALLET);
            Transaction transaction = transformer.transformOne(transactionData);

            logger.info("Received [{}]", transaction);
            return transaction;
        } catch (CreditsException e) {
            throw new WalletWebException(ERROR_CODE, WalletWebUtils.createWalletWebExceptionMessage(e));
        }
    }
}
