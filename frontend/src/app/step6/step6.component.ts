import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import {DataService} from "../data.service";
import {FormControl} from "@angular/forms";

@Component({
  selector: 'app-step6',
  templateUrl: './step6.component.html',
  styleUrls: []
})
export class Step6Component implements OnInit {

  keyControl = new FormControl();

  csKontrol =  new FormControl();

  feeControl =  new FormControl();


  createPaymentIsHidden : boolean = false;

  value : string = "";

  numSym : number = 0;

  coinsControl : boolean = true;

  feeDigitCount : number = 0;

  feeValueIsValid : boolean = true;

  addressKey : string; // кошелек

  toAddress : string; // куда шлем

  step : number = 6;

  constructor(public dataService: DataService) { }

  ngOnInit() {

    this.dataService.addressKeyObservable.subscribe( // получили кошелек откуда  будем  слать
      addressKey => {
        this.addressKey = addressKey
      }
    );

    this.keyControl.valueChanges.subscribe(value => {

      if(value == null || value == undefined || value == '') {
        this.createPaymentIsHidden = false;
      } else {
        if (this.addressKey == value) {
          this.createPaymentIsHidden = false
        }
      }
    });

    this.csKontrol.valueChanges.subscribe(value => {
      // TODO Refactor code after previous developer working

      if(value == null) {
        this.coinsControl = true;
        this.createPaymentIsHidden = false;
      }

      if (value != null) { // проверим на валидность  по балансу

        this.value = (value + "").toString();

        if (this.value.indexOf('e') > 0) {

          this.numSym =  parseInt(this.value.substring(this.value.indexOf('e') + 2)) + this.value.substring(0,this.value.indexOf('e')).length - 1;
        } else {
          this.numSym = this.value.substring(this.value.indexOf('.') + 1).length;
        }

        this.dataService.transactionFeeVal = (this.dataService.amountInCs * this.dataService.transactionFee) / 100;

        if ((this.dataService.amountInCs + this.dataService.transactionFeeVal) > this.dataService.accountData.balance || this.dataService.amountInCs == 0
          || this.numSym >= 16) {

          this.coinsControl = true;
        }

        if ((this.dataService.amountInCs + this.dataService.transactionFeeVal) < this.dataService.accountData.balance && this.dataService.amountInCs != 0 && this.numSym < 16) {

          this.coinsControl = false;
        }
      }
      if (value != null && this.toAddress != null) { // проверим на валидность  по балансу и по  заполненности адреса

        if (this.dataService.amountInCs != 0 && this.dataService.accountData.balance != 0 && this.addressKey != this.toAddress && this.numSym < 16 ) {
          this.createPaymentIsHidden = true
        }

        if (this.dataService.amountInCs == 0 || this.dataService.amountInCs < 0 || (this.dataService.amountInCs + this.dataService.transactionFeeVal) > this.dataService.accountData.balance
          || this.addressKey == this.toAddress || this.dataService.amountInCs == null || this.numSym > 15 ) {

          this.createPaymentIsHidden = false
        }
      }
    });


    this.feeControl.valueChanges.subscribe(value => {
      // TODO Refactor code after previous developer working
      if(value == null) {
        this.feeValueIsValid = true;
        this.createPaymentIsHidden = false;
      }

      if (value != null) { // проверим на валидность  по балансу

        this.value = (value + "").toString();

        if (this.value.indexOf('e') > 0) {
          this.feeDigitCount =  parseInt(this.value.substring(this.value.indexOf('e') + 2)) + this.value.substring(0,this.value.indexOf('e')).length - 1;
        } else {
          this.feeDigitCount = this.value.substring(this.value.indexOf('.') + 1).length;
        }

        this.dataService.transactionFeeVal = (this.dataService.amountInCs * this.dataService.transactionFee) / 100;

        if ((this.dataService.amountInCs + this.dataService.transactionFeeVal) > this.dataService.accountData.balance || this.dataService.amountInCs == 0
          || this.numSym >= 16) {

          this.feeValueIsValid = true;
        }

        if ((this.dataService.amountInCs + this.dataService.transactionFeeVal) < this.dataService.accountData.balance && this.dataService.amountInCs != 0 && this.numSym < 16) {

          this.feeValueIsValid = false;
        }
      }
      if (value != null && this.toAddress != null) { // проверим на валидность  по балансу и по  заполненности адреса

        if (this.dataService.offeredMaxFee != 0 && this.dataService.accountData.balance != 0 && this.addressKey != this.toAddress && this.feeDigitCount < 16 ) {
          this.createPaymentIsHidden = true
        }

        if (this.dataService.amountInCs == 0 || this.dataService.amountInCs < 0 || (this.dataService.amountInCs + this.dataService.transactionFeeVal) > this.dataService.accountData.balance
          || this.addressKey == this.toAddress || this.dataService.amountInCs == null || this.numSym > 15 ) {

          this.createPaymentIsHidden = false
        }
      }
    });
  }

  @Output() changeStepEvent = new EventEmitter<number>();

  public goToStep(step : number) {

    this.changeStepEvent.emit(step);

    this.dataService.changePrevStep(6);

    this.dataService.changeToAddress(this.toAddress);

    if(step == 65) {
      this.dataService.refreshSmartContractList(this.dataService.accountData.wallet);
    }
  }

  goToPrevStep() {
    this.changeStepEvent.emit(this.dataService.prevStep);
  }

  onChange($event, coinValue) {
    this.dataService.refreshBalance(coinValue.toString().substring(coinValue.toString().indexOf('(')+1,coinValue.length-1));
  }
}
