import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {DataService} from "../data.service";
import {AccountData} from "../accountdata";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: []
})
export class MainComponent implements OnInit {

  step : number = 0; // TODO step = 0

  constructor(public dataService: DataService) { }

  @Output() changeStepEvent = new EventEmitter<number>();

  ngOnInit() {
/*
    this.dataService.isWalletOpened = true; // TODO delete
    this.dataService.accountData = new AccountData();
    this.dataService.accountData.wallet = "8J7oZawJadpRFoMpQb42ffV3HN5zxtLjUJfNN8jpxUgp";
    // this.dataService.refreshSmartContractList(this.dataService.accountData.wallet);
*/
  }

  receiveStep($event) {
    this.step = $event
  }

  public goToStep(step : number) {
    this.changeStepEvent.emit(step);
    this.dataService.changePrevStep(0);
  }
}
