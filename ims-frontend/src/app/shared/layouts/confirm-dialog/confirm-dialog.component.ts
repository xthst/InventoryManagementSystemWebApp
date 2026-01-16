import { Component, inject, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogActions, MatDialogContent, MatDialogTitle, MatDialogClose, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-dialog',
  standalone: true,
  imports: [
    MatDialogActions,
    MatDialogContent,
    MatDialogTitle,
    MatButtonModule,
    MatDialogClose
],
  templateUrl: './confirm-dialog.component.html',
  styleUrl: './confirm-dialog.component.css',
})
export class ConfirmDialogComponent {
  private readonly dialogData = inject(MAT_DIALOG_DATA, {optional: true});

  dialogTitle = signal(this.dialogData?.dialogTitle ?? "Confirm Action");
  dialogMessage = signal(this.dialogData?.dialogMessage ?? "Are you sure?");
  okButtonOnly = signal(this.dialogData?.okButtonOnly ?? false);
}
