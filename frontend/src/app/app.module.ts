import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { MainComponent } from './main/main.component';
import { Step0Component } from './step0/step0.component';
import { Step1Component } from './step1/step1.component';
import { FaqComponent } from './faq/faq.component';
import {DataService} from "./data.service";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import { Step4Component } from './step4/step4.component';
import { Step5Component } from './step5/step5.component';
import { Step6Component } from './step6/step6.component';
import { Step7Component } from './step7/step7.component';
import {Step45Component} from "./step45/step45.component";
import {NewcoinComponent} from "./newcoin/newcoin.component";
import {Step8Component} from "./step8/step8.component";
import {FAQfullComponent} from "./FAQfull/FAQfull";
import {SupportComponent} from "./support/support.component";
import { AccountComponent } from './account/account.component';
import  {GuidGenerator} from "./utils/GuidGenerator";
import {ReactiveFormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";
import {SmartComponent} from "./smart/smart.component";
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { HeaderComponent } from './header/header.component';
import { DialogInfoComponent } from './dialog-info/dialog-info.component';
import {MatDialogModule} from '@angular/material/dialog';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { FooterComponent } from './footer/footer.component';
import {Utils} from "./utils/Utils";

@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    Step0Component,
    Step1Component,
    FaqComponent,
    Step4Component,
    Step45Component,
    Step5Component,
    Step6Component,
    SmartComponent,
    NewcoinComponent,
    Step7Component,
    Step8Component,
    FAQfullComponent,
    SupportComponent,
    AccountComponent,
    HeaderComponent,
    DialogInfoComponent,
    FooterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    HttpModule,
    FormsModule,
    ReactiveFormsModule,
    MatDialogModule,
    BrowserAnimationsModule
  ],
  providers: [DataService, Step1Component, GuidGenerator, Utils],
  bootstrap: [MainComponent],
  schemas: [NO_ERRORS_SCHEMA, CUSTOM_ELEMENTS_SCHEMA],
  entryComponents: [ DialogInfoComponent ]
})
export class AppModule { }

