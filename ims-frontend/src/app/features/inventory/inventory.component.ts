import { Component, effect, inject, input, OnInit, output, signal, ViewChild } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { ProductService } from '../../services/product.service';
import { Product } from '../../model/product.model';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { StockStatus } from '../../model/stock-status.enum';
import { MatOption, MatSelect } from '@angular/material/select';
import { TableShellComponent } from '../../shared/layouts/table-shell-component/table-shell.component';

@Component({
  selector: 'app-inventory',
  imports: [
    TableShellComponent,
    MatTableModule,
    MatSortModule,
    MatFormField,
    MatLabel,
    MatInput,
    MatSelect,
    MatOption,
    FormsModule
  ],
  templateUrl: './inventory.component.html',
  styleUrl: './inventory.component.css',
})
export class InventoryComponent {
  productService = inject(ProductService);

  dataSource = new MatTableDataSource<Product>([]);
  isLoading = input.required<boolean>();
  productData = input.required<Product[]>();

  searchQuery = signal<string>('');
  readonly stockStatus = Object.values(StockStatus);
  selectedStatus = signal<string>('');

  @ViewChild(TableShellComponent) set tableShellComponentRef(tableShell: TableShellComponent | undefined) {
    if (tableShell && tableShell.paginatorRef) {
      this.dataSource.paginator = tableShell.paginatorRef;
    }
  }

  @ViewChild(MatSort) set sortRef(sort: MatSort | undefined) {
    if (sort) {
      this.dataSource.sort = sort;
    }
  }

  columnsToDisplay = ['id', 'name', 'description', 'quantityOnHand'];

  constructor() {
    this.createFilterPredicate();

    effect(() => {
      this.dataSource.data = this.productData();
    })
  }

  getRowReorderColor(product: Product): string {
    if (product.quantityOnHand == 0) {
      return '	#ffc2c2';
    } else if (product.quantityOnHand <= product.reorderPoint) {
      return '#FFFAA0';
    }

    return '';
  }

  filterTable() {
    const filterObject = {
      search: this.searchQuery().toLowerCase(),
      stockStatus: this.selectedStatus()
    };

    this.dataSource.filter = JSON.stringify(filterObject);
  }

  createFilterPredicate() {
    this.dataSource.filterPredicate = (product, filter) => {
      const action = JSON.parse(filter);

      const dataString = Object.values(product).join(' ').toLowerCase();
      const matchesSearch = dataString.includes(action.search);

      let matchesStock = true;
      if (action.stockStatus == StockStatus.Normal) matchesStock = product.quantityOnHand > product.reorderPoint;
      if (action.stockStatus == StockStatus.Low) matchesStock = product.quantityOnHand <= product.reorderPoint && product.quantityOnHand > 0;
      if (action.stockStatus == StockStatus.Empty) matchesStock = product.quantityOnHand === 0;

      return matchesSearch && matchesStock;
    }
  }


}
