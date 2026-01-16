import { Component, input } from '@angular/core';
import { MatDialogActions, MatDialogContent, MatDialogModule } from '@angular/material/dialog';
import { MatButton } from '@angular/material/button';

@Component({
  selector: 'app-form-dialog',
  imports: [
    MatDialogActions,
    MatDialogModule,
    MatButton,
    MatDialogContent
  ],
  templateUrl: './form-dialog.component.html',
  styleUrl: './form-dialog.component.css',
})
export class FormDialogComponent {

  dialogTitle = input.required<string>();
  confirmFunction = input.required<() => void>();
}
