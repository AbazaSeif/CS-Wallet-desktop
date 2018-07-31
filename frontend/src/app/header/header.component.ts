import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import {DataService} from "../data.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(public dataService: DataService) { }

  ngOnInit() {
  }

  @Output() changeStepEvent = new EventEmitter<number>();

  goToStep(step : number) {

    this.changeStepEvent.emit(step);

    this.dataService.changePrevStep(6);

    if(step == 65) {
      console.log("wallet = " + this.dataService.accountData.wallet);
      this.dataService.refreshSmartContractList(this.dataService.accountData.wallet);
    }
  }
}
