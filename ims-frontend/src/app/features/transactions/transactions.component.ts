import { Component, effect, inject, input, OnInit, output, signal, ViewChild } from '@angular/core';
import { Transaction, TransactionType } from '../../model/transaction.type';
import { TransactionService } from '../../services/transaction.service';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { DatePipe } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatFormField, MatLabel, MatSelectModule } from '@angular/material/select';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { TableShellComponent } from '../../shared/layouts/table-shell-component/table-shell.component';
import { MatButton, MatMiniFabButton } from '@angular/material/button';
import { MatInput } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { TransactionFormDialogComponent } from './transaction-form-dialog.component/transaction-form-dialog.component';
import { FormType } from '../../model/form-type.enum';
import { ConfirmDialogComponent } from '../../shared/layouts/confirm-dialog/confirm-dialog.component';
import { MatOptionModule } from '@angular/material/core';
import { ApiError } from '../../model/apiError.model';
import { Product } from '../../model/product.model';

@Component({
  selector: 'app-transactions',
  imports: [
    MatTableModule,
    DatePipe,
    TableShellComponent,
    MatTableModule,
    MatSortModule,
    MatFormField,
    MatSelectModule,
    MatOptionModule,
    MatLabel,
    MatIconModule,
    MatInput,
    MatButton,
    FormsModule,
    MatMiniFabButton],
  templateUrl: './transactions.component.html',
  styleUrl: './transactions.component.css',
})
export class TransactionsComponent {
  private readonly transactionService = inject(TransactionService);
  private readonly dialog = inject(MatDialog);
  readonly defaultErrMsg = "An error occurred"
  readonly transactionTypes = ['', ...Object.values(TransactionType)];

  dataSource = new MatTableDataSource<Transaction>([]);
  transactionData = input.required<Transaction[]>();
  productData = input.required<Product[]>();
  isLoading = input.required<boolean>();

  transactionsChanged = output<void>();

  searchQuery = signal<string>('');
  selectedType = signal<string>('');

  @ViewChild(MatSort) set sortRef(sort: MatSort | undefined) {
    if (sort) {
      this.dataSource.sort = sort;
    }
  }

  columnsToDisplay = ['id', 'product', 'transactionDate', 'transactionType', 'quantity', 'reference', 'actionButtons'];

  constructor() {
    this.createFilterPredicate();

    effect(() => {
      this.dataSource.data = this.transactionData();
    })
  }

  filterTable() {
    const filterObject = {
      search: this.searchQuery().toLowerCase(),
      typeFilter: this.selectedType()
    }

    this.dataSource.filter = JSON.stringify(filterObject);
  }

  createFilterPredicate() {
    this.dataSource.filterPredicate = (transaction, filter) => {
      const action = JSON.parse(filter);

      const dataString = ([
        transaction.id,
        transaction.product.name,
        transaction.transactionDate,
        transaction.transactionType,
        transaction.quantity,
        transaction.reference
      ]).join(' ').toLowerCase()

      const matchesSearch = dataString.includes(action.search);

      let matchesFilter = true;
      if (action.typeFilter == TransactionType.Inbound) matchesFilter = transaction.transactionType === TransactionType.Inbound
      if (action.typeFilter == TransactionType.Outbound) matchesFilter = transaction.transactionType === TransactionType.Outbound

      return matchesSearch && matchesFilter;
    }
  }

  addTransaction() {
    const dialogRef = this.dialog.open(TransactionFormDialogComponent, {
      data: {
        type: FormType.Create,
        product: this.productData()
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (!result) {
        return;
      }

      this.transactionService.addTransaction(result).subscribe({
        next: () => {
          this.transactionsChanged.emit();
        },
        error: (error: ApiError) => {
          let errorMessage = ''

          if (error.errors) {
            errorMessage = error.message + '. ' + Object.values(error.errors).join('\n');
          } else {
            errorMessage = error.message ?? "An error occurred"
          }

          this.dialog.open(ConfirmDialogComponent, {
            data: {
              dialogTitle: "Failed to add transaction",
              dialogMessage: errorMessage,
              okButtonOnly: true
            }
          })
        }
      })
    })
  }

  deleteTransaction(transaction: Transaction) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        dialogTitle: "Delete Transaction",
        dialogMessage: "Are you sure you want to delete this transaction?"
      }
    })

    dialogRef.afterClosed().subscribe(result => {
      if (!result) {
        return;
      }

      this.transactionService.deleteTransaction(transaction.id).subscribe({
        next: () => {
          this.dataSource.data = this.dataSource.data.filter(existingTransaction =>
            existingTransaction.id !== transaction.id
          );
          this.transactionsChanged.emit();
        },
        error: (error: ApiError) => {
          this.dialog.open(ConfirmDialogComponent, {
            data: {
              dialogTitle: "Failed to delete transaction",
              dialogMessage: error.message ?? "An error occurred",
              okButtonOnly: true
            }
          })
        },
      })
    })
  }
}
