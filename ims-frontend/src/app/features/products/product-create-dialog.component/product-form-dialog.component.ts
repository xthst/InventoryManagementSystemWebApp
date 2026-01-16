import { Component, inject, signal } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { FormType } from '../../../model/form-type.enum';
import { Product } from '../../../model/product.model';
import { MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormDialogComponent } from '../../../shared/layouts/form-dialog.component/form-dialog.component';

@Component({
  selector: 'app-product-form-dialog',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormDialogComponent,
    MatFormFieldModule,
    MatInputModule,
    MatLabel
  ],
  templateUrl: './product-create-dialog.component.html',
  styleUrl: './product-create-dialog.component.css',
})
export class ProductFormDialogComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly dialogRef = inject(MatDialogRef<ProductFormDialogComponent>);
  private readonly data = inject<{
    type: FormType.Create | FormType.Edit;
    formData?: Product;
  }>(MAT_DIALOG_DATA);

  dialogTitle = signal(this.data.type === FormType.Edit ? "Edit Product" : "Add Product");

  form = this.formBuilder.group({
    name: [this.data.formData?.name ?? '', Validators.required],
    description: [this.data.formData?.description ?? ''],
    unitOfMeasure: [this.data.formData?.unitOfMeasure ?? '', Validators.required],
    reorderPoint: [this.data.formData?.reorderPoint ?? 0, [Validators.required, Validators.min(0)]],
    quantityOnHand: [this.data.formData?.quantityOnHand ?? 0, [Validators.required, Validators.min(0)]],
  });

  save = () => {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.dialogRef.close(this.form.value);
  }
}
