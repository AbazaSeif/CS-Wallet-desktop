import {Component, EventEmitter, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-faqfull',
  templateUrl: './FAQfull.html',
  styleUrls: []
})
export class FAQfullComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  @Output() changeStepEvent = new EventEmitter<number>();

  goToStep(step : number) {
    this.changeStepEvent.emit(step)


  }

}
