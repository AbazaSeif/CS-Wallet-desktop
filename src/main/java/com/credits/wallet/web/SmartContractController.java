package com.credits.wallet.web;

import com.credits.common.exception.CreditsException;
import com.credits.common.utils.Converter;
import com.credits.leveldb.client.ApiClient;
import com.credits.leveldb.client.data.ApiResponseData;
import com.credits.leveldb.client.data.SmartContractData;
import com.credits.leveldb.client.util.ApiClientUtils;
import com.credits.wallet.domain.HttpResponse;
import com.credits.wallet.domain.smartcontract.toweb.SmartContract;
import com.credits.wallet.domain.smartcontract.fromweb.SmartContractExecution;
import com.credits.wallet.domain.transformer.SmartContractTransformer;
import com.credits.wallet.domain.transformer.Transformer;
import com.credits.wallet.exception.WalletWebException;
import com.credits.wallet.utils.WalletWebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.ByteBuffer;

@RestController
@CrossOrigin
@RequestMapping(value = "/smartcontract")
public class SmartContractController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmartContractsController.class);

    @RequestMapping(method = RequestMethod.GET, value = "{address}")
    public ResponseEntity<SmartContract> getSmartContract(@PathVariable String address) {
        LOGGER.info("Receiving smart contract with address {}", address);
        Transformer<SmartContractData, SmartContract> transformer =
            new Transformer<>(SmartContractTransformer.TO_WALLET);
        try {
            SmartContractData smartContractData = apiClient.getSmartContract(address);
            return new ResponseEntity<SmartContract>(transformer.transformOne(smartContractData), HttpStatus.OK);
        } catch (CreditsException e) {
            throw new WalletWebException(ERROR_CODE, WalletWebUtils.createWalletWebExceptionMessage(e));
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/bytes/{address}")
    public ResponseEntity<HttpResponse> getSmartContractBytesBase58(@PathVariable String address) {
        LOGGER.info("Receiving smart contract with address {}", address);
        try {
            SmartContractData smartContractData = apiClient.getSmartContract(address);
            byte[] bytes = ApiClientUtils.serializeByThrift(smartContractData);
            String bytesBase58 = Converter.encodeToBASE58(bytes);
            return new ResponseEntity<HttpResponse>(new HttpResponse(bytesBase58), HttpStatus.OK);
        } catch (CreditsException e) {
            throw new WalletWebException(ERROR_CODE, WalletWebUtils.createWalletWebExceptionMessage(e));
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/execute", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpResponse> execute(@RequestBody SmartContractExecution smartContractExecution) {

        SmartContractData smartContractData;
        try {
            smartContractData = apiClient.getSmartContract(smartContractExecution.getSmartContractAddress());
        } catch (CreditsException e) {
            throw new WalletWebException(ERROR_CODE, WalletWebUtils.createWalletWebExceptionMessage(e));
        }

        if (!smartContractExecution.getSmartContractHashState().equals(smartContractData.getHashState())) {
            throw new WalletWebException(ERROR_CODE, "Smart contract you're trying to execute has been changed");
        }

        smartContractData.setMethod(smartContractExecution.getExecutionMethod());
        //smartContractExecution.getExecutionMethodParamsVals()
        smartContractData.setParams(null);

        ApiResponseData apiResponseData;
        try {
            apiResponseData = apiClient.executeSmartContract(
                    smartContractExecution.getTransactionInnerId(),
                    smartContractExecution.getTransactionSource(),
                    smartContractExecution.getSmartContractAddress(),
                    smartContractData,
                    ByteBuffer.wrap(Converter.decodeFromBASE58(smartContractExecution.getSignatureBase58()))
            );
            String responseMessage;
            if (apiResponseData.getCode() == ApiClient.API_RESPONSE_SUCCESS_CODE) {
                if (apiResponseData.getScExecRetVal() != null) {
                    StringBuilder retVal = new StringBuilder();
                    retVal.append("v_bool=");
                    retVal.append(apiResponseData.getScExecRetVal().getV_bool());
                    retVal.append("\n");
                    retVal.append("v_i8=");
                    retVal.append(apiResponseData.getScExecRetVal().getV_i8());
                    retVal.append("\n");
                    retVal.append("v_i16=");
                    retVal.append(apiResponseData.getScExecRetVal().getV_i16());
                    retVal.append("\n");
                    retVal.append("v_i32=");
                    retVal.append(apiResponseData.getScExecRetVal().getV_i32());
                    retVal.append("\n");
                    retVal.append("v_i64=");
                    retVal.append(apiResponseData.getScExecRetVal().getV_i64());
                    retVal.append("\n");
                    retVal.append("v_double=");
                    retVal.append(apiResponseData.getScExecRetVal().getV_double());
                    retVal.append("\n");
                    if (apiResponseData.getScExecRetVal().getV_string() != null) {
                        retVal.append("v_string=");
                        retVal.append(apiResponseData.getScExecRetVal().getV_string());
                        retVal.append("\n");
                    }
                    responseMessage = "Smart-contract executed successfully; Returned value:\n" + retVal.toString();
                } else
                    responseMessage = "Smart-contract executed successfully";
            } else {
                responseMessage = apiResponseData.getMessage();
            }
            HttpResponse httpResponse = new HttpResponse(responseMessage);
            return new ResponseEntity<HttpResponse>(httpResponse, HttpStatus.OK);
        } catch (CreditsException e) {
            throw new WalletWebException(ERROR_CODE, WalletWebUtils.createWalletWebExceptionMessage(e));
        }
    }
}
