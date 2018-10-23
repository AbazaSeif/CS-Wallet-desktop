package com.credits.leveldb.client.util;

import com.credits.common.exception.CreditsException;
import com.credits.common.utils.Const;
import com.credits.common.utils.Converter;
import com.credits.leveldb.client.data.*;
import com.credits.leveldb.client.exception.LevelDbClientException;
import com.credits.leveldb.client.thrift.*;
import com.credits.thrift.generated.Variant;
import org.apache.commons.codec.binary.Hex;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class LevelDbClientConverter {

    public static Double amountToDouble(Amount amount) {

        Integer integralPart = amount.getIntegral();
        Double fractionPart = amount.getFraction() / 1000000000000000000D;
        return Double.valueOf(integralPart) + fractionPart;
    }

    public static BigDecimal amountToBigDecimal(Amount amount) throws LevelDbClientException {

        Integer integralPart = amount.getIntegral();
        BigDecimal fractionPart =
                null;
        try {
            fractionPart = com.credits.common.utils.Converter.toBigDecimal(amount.getFraction())
                    .divide(com.credits.common.utils.Converter.toBigDecimal("1000000000000000000"));
        } catch (CreditsException e) {
            throw new LevelDbClientException(e);
        }

        integralPart += fractionPart.intValue();
        String integralPartAsString = com.credits.common.utils.Converter.toString(integralPart);
        String fractionPartAsString = com.credits.common.utils.Converter.toString(fractionPart);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Const.LOCALE);
        char sep = symbols.getDecimalSeparator();
        String separator = Character.toString(sep);

        if (fractionPartAsString.contains(separator)) {
            String[] valueDelimited = fractionPartAsString.split("\\" + separator);
            fractionPartAsString = valueDelimited[1];
        } else {
            fractionPartAsString = "0";
        }
        return new BigDecimal(String.format("%s.%s", integralPartAsString, fractionPartAsString));
    }

    public static Amount doubleToAmount(Double value) throws LevelDbClientException {

        if (value == null) {
            throw new LevelDbClientException("value is null");
        }
        String valueAsString = String.format(Locale.ROOT, "%.18f", value);
        String[] valueDelimited = valueAsString.split("\\.");
        Integer integral = Integer.valueOf(valueDelimited[0]);
        Long fraction = Long.valueOf(valueDelimited[1]);
        return new Amount(integral, fraction);
    }

    public static Amount bigDecimalToAmount(BigDecimal value) throws LevelDbClientException {

        if (value == null) {
            throw new LevelDbClientException("value is null");
        }

        Integer integral;

        Long fraction;

        String valueAsString = com.credits.common.utils.Converter.toString(value);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Const.LOCALE);
        char sep = symbols.getDecimalSeparator();
        String separator = Character.toString(sep);

        if (valueAsString.contains(separator)) {
            String[] valueDelimited = valueAsString.split("\\" + separator);
            integral = Integer.valueOf(valueDelimited[0]);
            String fractionAsString = String.format("%-18s", valueDelimited[1]).replace(' ', '0');
            fraction = Long.valueOf(fractionAsString);
        } else {
            integral = Integer.valueOf(valueAsString);
            fraction = 0L;
        }
        return new Amount(integral, fraction);
    }


    public static TransactionData transactionToTransactionData(SealedTransaction sealedTransaction) throws LevelDbClientException {

        Transaction trxn = sealedTransaction.getTrxn();
        TransactionData data = new TransactionData();
        String innerId = Hex.encodeHexString(sealedTransaction.id.poolHash.array())+"."+(sealedTransaction.id.index+1);
        data.setAmount(LevelDbClientConverter.amountToBigDecimal(trxn.getAmount()));
        data.setCurrency(trxn.getCurrency());
        data.setId(innerId);
        data.setSource(trxn.getSource());
        data.setTarget(trxn.getTarget());
        data.setBalance(LevelDbClientConverter.amountToBigDecimal(trxn.getBalance()));
        return data;
    }

    public static PoolData poolToPoolData(Pool pool) {

        PoolData data = new PoolData();

        data.setTransactionsCount(pool.getTransactionsCount());
        data.setHash(pool.getHash());
        data.setPrevHash(pool.getPrevHash());
        data.setTime(pool.getTime());
        data.setPoolNumber(pool.getPoolNumber());
        return data;
    }

    public static SmartContract smartContractDataToSmartContract(SmartContractData smartContractData) {

        SmartContract smartContract = new SmartContract();
        smartContract.setAddress(smartContractData.getAddress());
        smartContract.setDeployer(smartContractData.getDeployer());
        smartContract.setSourceCode(smartContractData.getSourceCode());
        smartContract.setByteCode(smartContractData.getByteCode());
        smartContract.setHashState(smartContractData.getHashState());
        return smartContract;
    }

    public static SmartContractData smartContractToSmartContractData(SmartContract smartContract) {

        SmartContractData smartContractData = new SmartContractData(
                smartContract.getAddress(),
                smartContract.getDeployer(),
                smartContract.getSourceCode(),
                smartContract.getByteCode(),
                smartContract.getHashState()
        );
        return smartContractData;
    }

    public static SmartContractInvocation smartContractInvocationDataToSmartContractInvocation(SmartContractInvocationData smartContractInvocationData) {

        SmartContractInvocation smartContractInvocation = new SmartContractInvocation(
                smartContractInvocationData.getSourceCode(),
                ByteBuffer.wrap(smartContractInvocationData.getByteCode()),
                smartContractInvocationData.getHashState(),
                smartContractInvocationData.getMethod(),
                smartContractInvocationData.getParams(),
                smartContractInvocationData.isForgetNewState()
        );

        return smartContractInvocation;
    }

    public static SmartContractInvocationData smartContractInvocationToSmartContractInvocationData(SmartContractInvocation smartContractInvocation) {

        SmartContractInvocationData smartContractInvocationData = new SmartContractInvocationData(
                smartContractInvocation.getSourceCode(),
                smartContractInvocation.getByteCode(),
                smartContractInvocation.getHashState(),
                smartContractInvocation.getMethod(),
                smartContractInvocation.getParams(),
                smartContractInvocation.isForgetNewState()
        );
        return smartContractInvocationData;
    }


    public static ApiResponseData apiResponseToApiResponseData(APIResponse apiResponse) {
        return new ApiResponseData(
                apiResponse.getCode(),
                apiResponse.getMessage(),
                null
        );
    }

    public static ApiResponseData apiResponseToApiResponseData(APIResponse apiResponse, Variant smartContractResult) {
        return new ApiResponseData(
                apiResponse.getCode(),
                apiResponse.getMessage(),
                smartContractResult
        );
    }

    public static TransactionId transactionIdDataToTransactionId(TransactionIdData transactionIdData) throws LevelDbClientException {
        TransactionId transactionId = new TransactionId();
        transactionId.setPoolHash(Converter.bytesToByteBuffer(transactionIdData.getPoolHash()));
        transactionId.setIndex(transactionIdData.getIndex());
        return transactionId;
    }

}
