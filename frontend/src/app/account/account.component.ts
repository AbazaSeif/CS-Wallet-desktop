import { Component, OnInit } from '@angular/core';
import {DataService} from "../data.service";
import {environment} from "../../environments/environment";
import {PlatformLocation} from "@angular/common";

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: []
})
export class AccountComponent implements OnInit {

  addressKey : string;

  constructor(public dataService: DataService) { }

  ngOnInit() {

    this.dataService.addressKeyObservable.subscribe(
      addressKey =>
      {
        this.addressKey = addressKey
      }
    );

    this.dataService.refreshCoins();
  }
}
