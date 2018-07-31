import {SourceCodeData} from "./SourceCodeData";
import {MethodData} from "./MethodData";

export class SmartContractData {
  address: string;
  sourceCode : {
    sourceCodeBody: string;
    methods: MethodData[];
  };
  hashState: string;
}
