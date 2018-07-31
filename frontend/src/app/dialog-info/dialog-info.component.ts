import { Component, OnInit, Inject } from '@angular/core';
import {MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {DialogData} from "../domain/dialog-data";

@Component({
  selector: 'app-dialog-info',
  templateUrl: './dialog-info.component.html',
  styleUrls: ['./dialog-info.component.css']
})
export class DialogInfoComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<DialogInfoComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData) { }

  ngOnInit() {
  }

  onCloseClick(): void {
    this.dialogRef.close();
  }

}
