import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import {DataService} from "../data.service";
import {Md5} from "ts-md5";
import {environment} from "../../environments/environment";
import {PlatformLocation} from "@angular/common";

@Component({
  selector: 'app-step8',
  templateUrl: './step8.component.html',
  styleUrls: []
})
export class Step8Component implements OnInit {

  constructor(public dataService: DataService,public platformLocation: PlatformLocation) { }

  toAddress : string; // куда шлем

  addressKey : string;

  transactionFeePer : number  = 0;

  step : number = 8;

  /*FEATURE/IMPROVEMENT

  link for the monitor on the basis of host and Protocol
   */

  ngOnInit() {

    this.transactionFeePer = (this.dataService.transactionFee*100)/this.dataService.amountInCs;

    this.transactionFeePer = parseFloat(this.transactionFeePer.toFixed(2));

    this.dataService.toAddressObservable.subscribe(
      toAddress =>
      {
        this.toAddress = toAddress
      }
    );

    this.dataService.addressKeyObservable.subscribe(
      addressKey =>
      {
        this.addressKey = addressKey
      }
    )

    console.log(new Date());

  }

  @Output() changeStepEvent = new EventEmitter<number>();

  goToStep(step : number) {

    this.changeStepEvent.emit(step);

    this.dataService.changePrevStep(8);

  }

}
