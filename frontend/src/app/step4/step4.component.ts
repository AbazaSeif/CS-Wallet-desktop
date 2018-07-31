import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import {DataService} from "../data.service";
import {sprintf} from "sprintf-js";
import {GuidGenerator} from "../utils/GuidGenerator";




@Component({
  selector: 'app-step4',
  templateUrl: './step4.component.html',
  styleUrls: ['./step4.component.css']
})
export class Step4Component implements OnInit {

  privateKey: string = "";

  secretKey : number[] = [];

  publicKey : number[] = [];

  filename : string = new Date().toString();

  public  continuebutton : boolean = true;

  addressKey: string = "";


  body : string;

  hash : string;

  innerId : string;

  step : number = 4;



  constructor(public dataService: DataService, private generateUID : GuidGenerator) { }

  ngOnInit() {
    this.dataService.privateKeyObservable.subscribe(

      privateKey =>
      {

        this.privateKey = privateKey;

      });

    this.dataService.addressKeyObservable.subscribe(

      addresskey =>
      {

        this.addressKey = addresskey;

      }

    );

    this.dataService.privateKeyArObservable.subscribe(

      secretKey =>
      {

        this.secretKey = secretKey;

      }

    );

    this.dataService.publicKeyArObservable.subscribe(

      publicKey =>
      {

        this.publicKey = publicKey;

      }

    );


    this.hash = this.generateUID.generateUUID();

    this.innerId= this.generateUID.generateUUID();

  }

  @Output() changeStepEvent = new EventEmitter<number>();

   goToStep(step : number) {

  this.changeStepEvent.emit(step);

    this.dataService.changePrevStep(4);

    this.dataService.changedAressKey(this.addressKey);

    this.dataService.changePrivateKey(this.privateKey);


    this.download();


  //   this.dataService.systemTransaction(this.adressKey) //убираем системную  транзакцию

  }

  private goToPrevStep() {

    this.changeStepEvent.emit(this.dataService.prevStep);

  }

  public  download() {

    this.body = '{\n' +
                 '"key":{"public":"'  +  this.addressKey + '","private":"'  + this.privateKey + '"}' +
                 '}';


    var file = new Blob([this.body], {type: 'text/plain'});

    this.continuebutton = false;

    if (window.navigator.msSaveOrOpenBlob) // IE10+
    {

      window.navigator.msSaveOrOpenBlob(file, this.filename);



    }

    else { // Others
      var a = document.createElement("a"),

        url = URL.createObjectURL(file);

      a.href = url;

      a.download = this.filename;

      document.body.appendChild(a);

      a.click();

      setTimeout(function() {

        document.body.removeChild(a);

        window.URL.revokeObjectURL(url);


      }, 0);
    }
  }


}
