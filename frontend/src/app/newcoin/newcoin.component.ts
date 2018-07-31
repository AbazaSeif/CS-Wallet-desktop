import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import {DataService} from "../data.service";

@Component({
  selector: 'newcoin',
  templateUrl: './newcoin.component.html',
  styleUrls: ['./newcoin.component.css']
})
export class NewcoinComponent implements OnInit {

  smartContractToken : string = '';

  coin : string = '';

  hidden : boolean = false;

  constructor(public service : DataService) { }

  ngOnInit() {
  }

  @Output() changeStepEvent = new EventEmitter<number>();

  goToStep(step : number) {
    this.changeStepEvent.emit(step);

    this.service.prevStep = 6;


  }

  /* CS 120

  The possibility of adding new tokens is implemented

   */

  saveCoin() {

    this.hidden = false;

    for (let i = 0; i < localStorage.length; i++) {


      if (localStorage.getItem(localStorage.key(i)) == this.smartContractToken)
      {

        this.hidden = true
        break
      }
/*
      else {
        this.hidden = false
      }
      */


    }



       if (localStorage.getItem(this.coin) != null || localStorage.getItem(this.coin) == this.smartContractToken) {

        this.hidden = true
      }




      if (this.hidden == false && this.smartContractToken.length > 0 && this.coin.length > 0)
        {

        localStorage.setItem(this.smartContractToken, this.coin)

        this.goToPrevStep()

          this.smartContractToken = '';

          this.coin = '';


      }


  }

   goToPrevStep() {

    this.changeStepEvent.emit(this.service.prevStep);


     this.smartContractToken = '';

     this.coin = '';

    this.service.refreshCoins()
  }

}
