import {Component, OnInit, Output, EventEmitter, AfterViewInit} from '@angular/core';
import {DataService} from "../data.service";

import {FormControl} from "@angular/forms";


@Component({
  selector: 'app-step5',
  templateUrl: './step5.component.html',
  styleUrls: ['./step5.component.css']
})
export class Step5Component implements OnInit {

  privateKey:  string = "";

  addressKey: string = "";

  keyControl = new FormControl();

  privateControl = new  FormControl();

  hidden : boolean = false;

  value : string = "";

  step : number = 5;

  isFile : number ;

  constructor(public dataService: DataService) { }

  ngOnInit() {
    this.dataService.isFileObservable.subscribe(
      isFile => {
        this.isFile = isFile;
        if (isFile == 1) {

          this.dataService.addressKeyObservable.subscribe(
            addressKey => {
              this.addressKey = addressKey
            }
          );

          this.dataService.privateKeyObservable.subscribe(
            privateKey => {
              this.privateKey = privateKey
            }
          )
        }
      }
    );

    /*
     CS 122 Generating public, private keys for wallets and signing transactions using EdDSA (Ed25519) algorithm in Wallet-Web
    a library is used to generate keys https://github.com/dchest/tweetnacl-js

    Validation of public and private key
    The keys are compared using the nacl method.sign.keyPair.fromSecretKey


     */

    let nacl = require('tweetnacl');

    /* FEATURE/IMPROVEMENT

     The transition from base64 to base 58

   */

    let  Base58 = require('base-58');

    this.keyControl.valueChanges.subscribe(value =>
    {
      try {

        let pKatob;

        //let ar = [];

        let pubKatob = 'pubKatob';

        let pubKnacl = 'pubKnacl';

        if (this.addressKey.length > 0 && this.privateKey.length > 0) {


          pKatob = Base58.decode(this.privateKey);

          pubKatob = Base58.decode(this.addressKey);

          if (pKatob.length == 64) {

            pubKnacl = nacl.sign.keyPair.fromSecretKey(pKatob).publicKey.toString('base64');

          }

        }

        if (pubKatob == pubKnacl) {

          this.hidden = true;


          //    document.getElementById("password1").setAttribute('readonly',"readonly");

          //    document.getElementById("password2").setAttribute('readonly',"readonly");

        }

        if (pubKatob != pubKnacl) {

          this.hidden = false;


          //    document.getElementById("password1").setAttribute('readonly',"readonly");

          //    document.getElementById("password2").setAttribute('readonly',"readonly");

        }
      }

      catch(e)
      {
        this.hidden = false;
      }

    })

    this.privateControl.valueChanges.subscribe(value =>
    {
      try {

        let pKatob;

        // let ar = [];

        let pubKatob = 'pubKatob';

        let pubKnacl = 'pubKnacl';

        if (this.addressKey.length > 0 && this.privateKey.length > 0) {


          //ar = atob(this.privateKey).split(',');

          pKatob = Base58.decode(this.privateKey);

          pubKatob = Base58.decode(this.addressKey);


          if (pKatob.length == 64) {
            pubKnacl = nacl.sign.keyPair.fromSecretKey(pKatob).publicKey.toString('base64');
          }


        }

        if (pubKatob == pubKnacl) {

          this.hidden = true;

        }

        if (pubKatob != pubKnacl) {

          this.hidden = false;

        }

      }

      catch(e)
      {
        this.hidden = false;
      }

    })

  }


  @Output() changeStepEvent = new EventEmitter<number>();

  goToStep(step : number) {

    this.dataService.changePrivateKey(this.privateKey);

    this.dataService.changedAressKey(this.addressKey);

    this.dataService.prevStep = 5;

    if (step==6) {
      this.dataService.isWalletOpened = true;
      this.dataService.accountData.wallet = this.addressKey;
    }

    this.changeStepEvent.emit(step);


  }

  goToPrevStep() {
    this.changeStepEvent.emit(this.dataService.prevStep);
  }
}
