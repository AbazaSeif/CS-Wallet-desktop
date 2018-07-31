import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import {DataService} from "../data.service";

@Component({
  selector: 'app-step45',
  templateUrl: './step45.component.html',
  styleUrls: ['./step45.component.css']
})
export class Step45Component implements OnInit {

  step : number = 45;

  constructor(public dataService : DataService) { }

  ngOnInit() {
  }

  @Output() changeStepEvent = new EventEmitter<number>();

  goToStep(step : number) {

    this.changeStepEvent.emit(step);

    this.dataService.changePrevStep(45);


  }


  /* FEATURE/IMPROVEMENT

  implemented the ability to read keys from the selected file


   */

   public onChange($event): void {

    this.readThis($event.target);

    this.dataService.changedIsFile(1);
  }

  readThis(inputValue : any) {

    let file : File = inputValue.files[0];

    let myReader: FileReader = new FileReader();

    myReader.onloadend = (e) => {

      let json =  JSON.parse(myReader.result);


      this.dataService.changedAressKey(json.key.public);

      this.dataService.changePrivateKey(json.key.private);

    }

    myReader.readAsText(file)


    this.goToStep(5)
  }


}
