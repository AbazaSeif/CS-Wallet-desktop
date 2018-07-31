package com.credits.wallet.web;

import com.credits.common.exception.CreditsException;
import com.credits.leveldb.client.data.SmartContractData;
import com.credits.wallet.domain.smartcontract.toweb.SmartContract;
import com.credits.wallet.domain.transformer.SmartContractTransformer;
import com.credits.wallet.domain.transformer.Transformer;
import com.credits.wallet.exception.WalletWebException;
import com.credits.wallet.utils.WalletWebUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/smartcontracts")
public class SmartContractsController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmartContractsController.class);

    @RequestMapping(method = RequestMethod.GET, value = "{address}")
    public ResponseEntity<List<SmartContract>> getSmartContract(@PathVariable String address) {

        Transformer<SmartContractData, SmartContract> transformer =
            new Transformer<>(SmartContractTransformer.TO_WALLET);
        try {
            List<SmartContractData> smartContractDatas = apiClient.getSmartContracts(address);
            return new ResponseEntity<List<SmartContract>>(transformer.batchTransform(smartContractDatas), HttpStatus.OK);
        } catch (CreditsException e) {
            throw new WalletWebException(ERROR_CODE, WalletWebUtils.createWalletWebExceptionMessage(e));
        }
    }

    @GetMapping(value = "author/{address}")
    public List<SmartContract> getSmartContracts(@PathVariable String address) {
        LOGGER.info("Receiving smart contracts by owner's address {}", address);
        Transformer<SmartContractData, SmartContract> transformer =
            new Transformer<>(SmartContractTransformer.TO_WALLET);
        try {
            List<SmartContractData> smartContracts = apiClient.getSmartContracts(address);
            return transformer.batchTransform(smartContracts);
        } catch (CreditsException e) {
            throw new WalletWebException(ERROR_CODE, WalletWebUtils.createWalletWebExceptionMessage(e));
        }
    }
}
