import { Component, inject, OnInit, Signal, signal } from '@angular/core';
import { InventoryComponent } from '../inventory/inventory.component';
import { TransactionsComponent } from '../transactions/transactions.component';
import { ProductsComponent } from '../products/products.component';
import { MatTabsModule } from '@angular/material/tabs';
import { ProductService } from '../../services/product.service';
import { TransactionService } from '../../services/transaction.service';
import { Product } from '../../model/product.model';
import { catchError } from 'rxjs';
import { Transaction } from '../../model/transaction.type';
import { MatSidenavModule } from '@angular/material/sidenav';

@Component({
  selector: 'app-dashboard',
  imports: [InventoryComponent, TransactionsComponent, ProductsComponent, MatTabsModule, MatSidenavModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit {
  private readonly productService = inject(ProductService);
  private readonly transactionService = inject(TransactionService);

  opened = signal<boolean>(false);

  productData = signal<Product[]>([]);
  transactionData = signal<Transaction[]>([]);
  areProductsLoading = signal<boolean>(true);
  areTransactionsLoading = signal<boolean>(true);

  ngOnInit(): void {
    this.fetchData()
  }

  fetchData() {
    this.areProductsLoading.set(true)
    this.productService.getProducts()
      .pipe(
        catchError((err) => {
          console.log(err);
          this.areProductsLoading.set(false);
          throw err;
        })
      )
      .subscribe((products) => {
        this.productData.set(products);
        this.areProductsLoading.set(false);
      })

    this.areTransactionsLoading.set(true)
    this.transactionService.getTransactions()
      .pipe(
        catchError((err) => {
          console.log(err);
          this.areTransactionsLoading.set(false);
          throw err;
        })
      )
      .subscribe((transactions) => {
        this.transactionData.set(transactions);
        this.areTransactionsLoading.set(false);
      })
  }
}
