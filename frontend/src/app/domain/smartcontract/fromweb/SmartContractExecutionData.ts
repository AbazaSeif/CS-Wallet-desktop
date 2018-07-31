export class SmartContractExecutionData {
  smartContractHashState: string;
  smartContractAddress: string;
  executionMethod: string;
  executionMethodParamsVals: string[];
  transactionInnerId: number;
  transactionSource: string;
  signatureBase58: string;
}
