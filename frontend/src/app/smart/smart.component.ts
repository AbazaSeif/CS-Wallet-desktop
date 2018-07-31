import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import {DataService} from "../data.service";
import {HttpClient} from "@angular/common/http";
import {SmartContractExecutionData} from "../domain/smartcontract/fromweb/SmartContractExecutionData";
import {GuidGenerator} from "../utils/GuidGenerator";
import {HttpResponseData} from "../domain/http-response";
import {SmartContractData} from "../domain/smartcontract/toweb/SmartContractData";
import {MethodData} from "../domain/smartcontract/toweb/MethodData";
import {Utils} from "../utils/Utils";

@Component({
  selector: 'app-smart',
  templateUrl: './smart.component.html',
  styleUrls: ['./smart.component.css']
})
export class SmartComponent implements OnInit {

  step : number = 65;

  smartContractExecution: SmartContractExecutionData;

  currentSmartContract : SmartContractData;

  currentMethod: MethodData;

  currentSmartContractBytesBase58: string;

  signatureBase58: string;

  smartContractAddress: string;

  privateKey:  string = "";

  tweetnacl = require('tweetnacl');

  base58 = require('base-58');

  bufferToUint8array = require('buffer-to-uint8array');

  constructor(
    public dataService : DataService,
    private http: HttpClient,
    private guidGenerator : GuidGenerator,
    private utils: Utils
  ) {}

  ngOnInit() {
    this.dataService.privateKeyObservable.subscribe(
      privateKey => {
        this.privateKey = privateKey
      }
    )
  }

  @Output() changeStepEvent = new EventEmitter<number>();

  goToStep(step : number) {
    this.changeStepEvent.emit(step);
    this.dataService.changePrevStep(6);
  }

  goToPrevStep() {
    this.changeStepEvent.emit(this.dataService.prevStep);
  }

  searchSmartContract() {
    this.refreshSmartContract(this.smartContractAddress);
  }

  onMethodChange(methodValue) {
    this.currentMethod = methodValue;
  }

  createSignature(thisComponent: SmartComponent) {

    var innerId = Number(Date.now());
    var tranSource = thisComponent.dataService.accountData.wallet;
    var tranTarget = thisComponent.currentSmartContract.address;
    var tranAmountInt = 0;
    var tranAmountFrac = 0;
    var tranOfferedMaxFeeInt = 0;
    var tranOfferedMaxFeeFraction = 0;
    var currencyCode = 0;
    var userFieldCount = 1;

    var buffer = this.utils.createTransactionBuffer(
      innerId,
      tranSource,
      tranTarget,
      tranAmountInt,
      tranAmountFrac,
      tranOfferedMaxFeeInt,
      tranOfferedMaxFeeFraction,
      currencyCode,
      userFieldCount,
      thisComponent.currentSmartContractBytesBase58
    );

    this.privateKey = "3rUevsW5xfob6qDxWMDFwwTQCq39SYhzstuyfUGSDvF2QHBRyPD8fSk49wFXaPk3GztfxtuU85QHfMV3ozfqa7rN";
    // console.log(this.base58.decode(this.privateKey));
    let signedMessage = this.tweetnacl.sign.detached(thisComponent.bufferToUint8array(buffer), thisComponent.base58.decode(thisComponent.privateKey));
    // console.log("signedMessage = " + signedMessage);
    thisComponent.signatureBase58 = thisComponent.base58.encode(signedMessage);
    // console.log("thisComponent.signatureBase58 = " + thisComponent.signatureBase58);
    thisComponent.smartContractExecution = new SmartContractExecutionData();
    thisComponent.smartContractExecution.smartContractAddress = thisComponent.currentSmartContract.address;
    thisComponent.smartContractExecution.smartContractHashState = thisComponent.currentSmartContract.hashState;
    thisComponent.smartContractExecution.executionMethod = thisComponent.currentMethod.name;
    thisComponent.smartContractExecution.executionMethodParamsVals = [];
    for(let param of thisComponent.currentMethod.params) {
      thisComponent.smartContractExecution.executionMethodParamsVals.push(param.value);
    }
    thisComponent.smartContractExecution.transactionInnerId = innerId;
    thisComponent.smartContractExecution.transactionSource = tranSource;
    thisComponent.smartContractExecution.signatureBase58 = thisComponent.signatureBase58;

    thisComponent.executeSmartContract(thisComponent.smartContractExecution).subscribe(
      data =>  {
        thisComponent.dataService.openDialogInfo((data as HttpResponseData).value);
      },
      err => {
        thisComponent.dataService.openDialogInfo(err.message);
      }
    );
  }

  onExecute() {
    this.getSmartContractBytesBase58(this.currentSmartContract.address, this.createSignature);
  }

  refreshSmartContract(address : string) {
    this.http.get<SmartContractData>(this.dataService.baseUrl + 'smartcontract/' + address).subscribe(
      res => {
        this.currentSmartContract = res;
        if (this.currentSmartContract.sourceCode.methods && this.currentSmartContract.sourceCode.methods.length>0) {
          this.currentMethod = this.currentSmartContract.sourceCode.methods[0];
        }
      },
      err => {
        this.dataService.openDialogInfo(err.message);
      }
    );
  }

  getSmartContractBytesBase58(
    address: string,
    callback: (component: SmartComponent) => any
  ): void {
    this.http.get<HttpResponseData>(this.dataService.baseUrl + 'smartcontract/bytes/' + address).subscribe(
      res => {
        this.currentSmartContractBytesBase58 = res.value;
        callback(this);
      },
      err => {
        this.dataService.openDialogInfo(err.message);
      }
    );
  }

  executeSmartContract(
    smartContractExecutionData: SmartContractExecutionData
  ) {
    return this.http.post(this.dataService.baseUrl + 'smartcontract/execute', smartContractExecutionData)
      .map(response => response)
  }
}
