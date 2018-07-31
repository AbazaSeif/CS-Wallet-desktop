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

import java.util.List;


@RestController
@CrossOrigin
public class TransactionsController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/transactions")
    public List<Transaction> getTransactions(@RequestParam String address, @RequestParam Long offset,
        @RequestParam Long limit) {

        LOGGER.info("Getting a list of transaction from [{}] to [{}] for address [{}]", offset, limit, address);
        try {
            List<TransactionData> transactionDataList = apiClient.getTransactions(address, offset, limit);

            Transformer<TransactionData, Transaction> transformer = new Transformer<>(TransactionTransformer.TO_WALLET);

            List<Transaction> transactionList = transformer.batchTransform(transactionDataList);
            LOGGER.info("Received [{}]", transactionDataList.size());
            return transactionList;
        } catch (CreditsException e) {
            throw new WalletWebException(ERROR_CODE, WalletWebUtils.createWalletWebExceptionMessage(e));
        }
    }
}
