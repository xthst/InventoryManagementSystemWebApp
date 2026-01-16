import { Component, computed, effect, inject, Injector, signal, WritableSignal } from '@angular/core';
import { FormDialogComponent } from "../../../shared/layouts/form-dialog.component/form-dialog.component";
import { FormBuilder, FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Transaction, TransactionRequest, TransactionType } from '../../../model/transaction.type';
import { FormType } from '../../../model/form-type.enum';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatRadioModule } from '@angular/material/radio';
import { MatInputModule } from '@angular/material/input';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { Product } from '../../../model/product.model';
import { toSignal } from '@angular/core/rxjs-interop';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-transaction-form-dialog',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormDialogComponent,
    MatFormFieldModule,
    MatInputModule,
    MatFormFieldModule,
    MatAutocompleteModule,
    MatDatepickerModule,
    MatRadioModule,
    MatSelectModule
  ],
  templateUrl: './transaction-form-dialog.component.html',
  styleUrl: './transaction-form-dialog.component.css',
})
export class TransactionFormDialogComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly dialogRef = inject(MatDialogRef<TransactionFormDialogComponent>);
  private readonly data = inject<{
    type: FormType.Create
    formData?: Transaction
    product?: Product[]
  }>(MAT_DIALOG_DATA);

  readonly TransactionType = TransactionType;
  readonly transactionTypes = signal(Object.values(TransactionType));
  readonly dialogTitle = signal("Add Transaction");

  readonly MAX_INT_VALUE = 2147483647;

  minQuantity = signal(1);
  maxQuantity = signal(this.MAX_INT_VALUE);
  allProducts = signal<Product[]>(this.data.product ?? [])

  readonly hours = Array.from({ length: 12 }, (_, i) => i + 1);
  readonly periods = ['AM', 'PM'];

  // Initialize time values
  private readonly initialDate = this.data.formData?.transactionDate ?? new Date();
  private readonly initialHour = this.initialDate.getHours() % 12 || 12;
  private readonly initialMinute = this.initialDate.getMinutes();
  private readonly initialPeriod = this.initialDate.getHours() >= 12 ? 'PM' : 'AM';

  form = this.formBuilder.nonNullable.group({
    product: [this.data.formData?.product ?? '', Validators.required],
    transactionDate: [this.data.formData?.transactionDate ?? new Date(), Validators.required],
    timeHour: [this.initialHour, Validators.required],
    timeMinute: [this.initialMinute, [Validators.required, Validators.min(0), Validators.max(59)]],
    timePeriod: [this.initialPeriod, Validators.required],
    transactionType: [this.data.formData?.transactionType ?? TransactionType.Inbound, Validators.required],
    quantity: [this.data.formData?.quantity ?? 0, [Validators.required, Validators.min(this.minQuantity()), Validators.max(this.maxQuantity())]],
    reference: [this.data.formData?.reference ?? '',]
  });

  get product() { return this.form.controls.product; }
  get transactionDate() { return this.form.controls.transactionDate; }
  get timeHour() { return this.form.controls.timeHour; }
  get timeMinute() { return this.form.controls.timeMinute; }
  get timePeriod() { return this.form.controls.timePeriod; }
  get transactionType() { return this.form.controls.transactionType; }
  get quantity() { return this.form.controls.quantity; }
  get reference() { return this.form.controls.reference; }

  currentStock = signal(0);
  selectedProduct = toSignal(this.product.valueChanges, { initialValue: this.data.formData?.product ?? '' });
  selectedTransactionType = toSignal(this.transactionType.valueChanges, { initialValue: this.data.formData?.transactionType ?? TransactionType.Inbound });

  constructor() {
    effect(() => {
      const product = this.selectedProduct();
      const type = this.selectedTransactionType();

      const hasProduct = product && typeof product === 'object';

      if (hasProduct) {
        this.quantity.enable({ emitEvent: false });
        this.transactionDate.enable({ emitEvent: false });
        this.timeHour.enable({ emitEvent: false });
        this.timeMinute.enable({ emitEvent: false });
        this.timePeriod.enable({ emitEvent: false });
        this.reference.enable({ emitEvent: false });

        this.currentStock.set(product.quantityOnHand);

        if (type === TransactionType.Outbound) {
          this.maxQuantity.set(product.quantityOnHand);
        } else {
          this.maxQuantity.set(this.MAX_INT_VALUE - product.quantityOnHand);
        }
      } else {
        this.quantity.disable({ emitEvent: false });
        this.transactionDate.disable({ emitEvent: false });
        this.timeHour.disable({ emitEvent: false });
        this.timeMinute.disable({ emitEvent: false });
        this.timePeriod.disable({ emitEvent: false });
        this.reference.disable({ emitEvent: false });
        this.maxQuantity.set(0);
      }

      this.quantity.setValidators([
        Validators.required,
        Validators.min(this.minQuantity()),
        Validators.max(this.maxQuantity())
      ]);
      this.quantity.updateValueAndValidity();
    });
  }

  searchTerm = signal<string>('');

  filteredProducts = computed(() => {
    const term = this.searchTerm().toLowerCase();

    if (!term) {
      return this.allProducts();
    }

    return this.allProducts().filter(product =>
      product.name.toLowerCase().includes(term) ||
      product.id.toString().includes(term)
    );
  })

  updateSearch(event: Event | string) {
    const value = typeof event === 'string'
      ? event
      : (event.target as HTMLInputElement).value;

    this.searchTerm.set(value);
  }

  productDisplayFn(product: Product): string {
    return product && product.name && product.id
      ? `${product.name}`
      : ''
  }

  save = () => {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const formValue = this.form.getRawValue();
    const productInput = formValue.product as Product;

    const date = new Date(formValue.transactionDate);

    let hours = formValue.timeHour;
    const minutes = formValue.timeMinute;
    const period = formValue.timePeriod;

    if (period === 'PM' && hours < 12) hours += 12;
    if (period === 'AM' && hours === 12) hours = 0;

    date.setHours(hours);
    date.setMinutes(minutes);

    const transactionRequest: TransactionRequest = {
      productId: productInput.id,
      transactionDate: date,
      transactionType: formValue.transactionType,
      quantity: formValue.quantity,
      reference: formValue.reference,
    }

    this.dialogRef.close(transactionRequest);
  }
}
