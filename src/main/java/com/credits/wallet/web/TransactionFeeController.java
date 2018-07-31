package com.credits.wallet.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
public class TransactionFeeController extends AbstractController {

    private static Logger logger = LoggerFactory.getLogger(TransactionFeeController.class);

    private static double TRANSACTION_FEE = 0.1;

    @RequestMapping(method = RequestMethod.GET, value = "/transactionFee")
    public double transactionFee() {
        logger.debug("Transaction fee is [{}] CS", TRANSACTION_FEE);
        return TRANSACTION_FEE;
    }

}
