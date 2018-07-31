import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable, BehaviorSubject} from "rxjs/Rx";
import {AccountData} from "./domain/accountdata";
import {StringResponse} from "./domain/stringresponse";
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/map';
import {CustomEncoder} from "./utils/CustomEncoder";
import {environment} from "../environments/environment";
import {Transaction} from "./domain/Transaction";
import {SmartContractData} from "./domain/smartcontract/toweb/SmartContractData";
import {DialogInfoComponent} from "./dialog-info/dialog-info.component";
import {MatDialog} from '@angular/material/dialog';

@Injectable()
export class DataService {

  prevStep: number = 0;
  isWalletOpened: boolean = false;
  accountData: AccountData = {
    wallet: '',
    balance: 0,
    balanceCurrency: ''
  };
  transactionFee: number = 0;
  transactionFeeVal: number = 0;

  amountInCs: number = 0;

  offeredMaxFee: number = 0;

  currency : string ;

  transactionCode: string;

  toAdress : string = "";

  coinDataList : any;

  baseUrl: string = "";

  monitorAddress: string = "";

  transaction : Transaction = new Transaction();

  networkList : any;

  isProdNet: boolean = false;

  smartContractList: SmartContractData[];

  smartContractHashStates : string;

  currencyCode: number = 1; // CS constant

  constructor(
    private http: HttpClient,
    public dialog: MatDialog
  ) {

    this.networkList = environment.networks;
  }

  private privateKeySource = new BehaviorSubject<string>('');
  privateKeyObservable = this.privateKeySource.asObservable();

  private adressKeySource = new BehaviorSubject<string>('');
  addressKeyObservable = this.adressKeySource.asObservable();

  private isFileSource = new BehaviorSubject<number>(0);
  isFileObservable = this.isFileSource.asObservable();

  private privateKeyAr = new BehaviorSubject<number[]>([]);
  privateKeyArObservable = this.privateKeyAr.asObservable();

  private publicKeyAr = new BehaviorSubject<number[]>([]);
  publicKeyArObservable = this.publicKeyAr.asObservable();

  private toAddressSourse = new BehaviorSubject<string>('');
  toAddressObservable = this.toAddressSourse.asObservable();

  private prevStepSource = new BehaviorSubject<number>(0);
  prevStepObservable = this.prevStepSource.asObservable();

  privateKey = this.privateKeySource.asObservable();

  adressKey = this.adressKeySource.asObservable();

  toAddress = this.toAddressSourse.asObservable();

  prevStepOnly  = this.prevStepSource.asObservable();

  isFile = this.isFileSource.asObservable();

  postFileKeyPwd(fileKeyPwd:string): Observable<StringResponse> {

    return this.http.post<StringResponse>(this.baseUrl + 'fileKeyPwd', fileKeyPwd);
  }

  changePrivateKey(privateKey: string) {
    this.privateKeySource.next(privateKey);
  }

    changedAressKey(addressKey: string) {

      this.adressKeySource.next(addressKey);

      this.toAdress = addressKey;

  }

  changedIsFile(isFile: number) {

    this.isFileSource.next(isFile);
  }

  changedPrivateKeyAr(privateKeyAr: number[]) {

    this.privateKeyAr.next(privateKeyAr);

    //  this.toAdress = addressKey;

  }

  changedPublicKeyAr(publicKey: number[]) {

    this.publicKeyAr.next(publicKey);

  }

  changeToAddress(Address: string) {

    this.toAddressSourse.next(Address);


  }

  changePrevStep(prevStep: number) {

    this.prevStepSource.next(prevStep);

    this.prevStep = prevStep;

  }

  refreshCoins() {
    if (localStorage.getItem('cs')== null)
    {
      localStorage.setItem('cs','Credits')

   }

    this.coinDataList = [];

    for (let i=0; i < localStorage.length; i++) {

      this.coinDataList.push({code : localStorage.key(i),name : localStorage.getItem(localStorage.key(i))});
    }

  }

  refreshStep6Data() {

     this.http.get<number>(this.baseUrl + 'transactionFee').subscribe(
          data => {

            this.transactionFee = data;

          },
          err => {
            console.log(err);

      }
    );
  }

  generateTransaction(
    innerId: number,
    source: string,
    target: string,
    amount: number,
    balance: number,
    currencyCode: number,
    signature: string,
    offeredMaxFee: number
  ) {

    this.transaction.innerId = innerId;
    this.transaction.source  = source;
    this.transaction.target  = target;
    this.transaction.amount  = amount;
    this.transaction.balance = balance;
    this.transaction.currency = currencyCode;
    this.transaction.signature = signature;
    this.transaction.offeredMaxFee = offeredMaxFee;

    return this.http.post(this.baseUrl + 'transactionFlow', this.transaction)
               .map(response => {
                 console.log(response);
               })
  }

  refreshTransactionCode() {
      this.http.get<StringResponse>(this.baseUrl + 'transactionCode'
      ).subscribe(
        data => {
          this.transactionCode = data.response;
        },
        err => {
          console.log(err);
        }
      );

  }

  refreshBaseUrl(net) {
    this.baseUrl = net.serviceAddress + '/' +  environment.javaServiceEndpoint;
    console.log("this.baseUrl = " + this.baseUrl);
    this.monitorAddress = net.monitorAddress + '/' +  environment.monitorUrlPart;
  }

  refreshIsProdNet(isProdNet) {
    this.isProdNet = isProdNet;
  }

  refreshBalance(currencyCode : number) {

    let params = new HttpParams({encoder: new CustomEncoder()});
    params = params.append('address',(this.toAdress));
    params = params.append('currency', String(currencyCode));

    this.http.get<any>(this.baseUrl + 'balance', {params: params}
    ).subscribe(
      data => {
        this.accountData.balance = data;
      },
      err => {
        console.log(err);
      }
    );
  }

  refreshSmartContractList(address : string) {
    this.http.get<SmartContractData[]>(this.baseUrl + 'smartcontracts/' + address).subscribe(
      res => {
        this.smartContractList = res;
        this.smartContractHashStates = "";
        for (var smartContract of this.smartContractList) {
          this.smartContractHashStates += smartContract.hashState + "\n";
        }
      },
      err => {
        console.log(err);
      }
    );
  }

  openDialogInfo(message:string): void {
    this.dialog.open(DialogInfoComponent, {
      width: '350px',
      data: {message: message}
    });
  }

  goToMonitor() {
    window.open(this.monitorAddress + encodeURIComponent(this.accountData.wallet));
  }

  logout() {
    this.isWalletOpened = false;
    window.location.href='/';
  }
}
