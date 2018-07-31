import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import {DataService} from "../data.service";

@Component({
  selector: 'app-step0',
  templateUrl: './step0.component.html',
  styleUrls: []
})
export class Step0Component implements OnInit {

  isNetChoosen : boolean = true;

  selectedNetObj : any;

  constructor(public dataService: DataService) { }

  ngOnInit() {

    for (let network of this.dataService.networkList) {

      if (network.name == 'Test net') {
        this.netListChange(network);
        this.selectedNetObj = network;
      }
    }
  }

  @Output() changeStepEvent = new EventEmitter<number>();

  goToStep(step : number) {

    this.changeStepEvent.emit(step);

    this.dataService.changePrevStep(0);

  }

  netListChange(netValue) {

    if (netValue) {
      this.isNetChoosen = true;
    }

    this.dataService.refreshBaseUrl(netValue);

    this.dataService.refreshIsProdNet(netValue.isProd);
  }
}
