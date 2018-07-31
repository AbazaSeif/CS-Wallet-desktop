import {Component, EventEmitter, OnInit, Output} from '@angular/core';



@Component({
  selector: 'app-support',
  templateUrl: './support.component.html',
  styleUrls: []
})
export class SupportComponent implements OnInit {

  constructor() { }


  @Output() changeStepEvent = new EventEmitter<number>();

  ngOnInit() {





  }





}
