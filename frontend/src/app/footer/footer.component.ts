import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import {DataService} from "../data.service";


@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {

  constructor(public dataService: DataService) { }

  ngOnInit() {
  }

  @Output() changeStepEvent = new EventEmitter<number>();

  goToStep(step : number) {

    this.changeStepEvent.emit(step);

    this.dataService.changePrevStep(0);

    if(step == 65) {
      this.dataService.refreshSmartContractList(this.dataService.accountData.wallet);
    }
  }
}
