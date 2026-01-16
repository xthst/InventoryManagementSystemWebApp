import { Component, effect, inject, input, OnInit, output, signal, ViewChild } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { Product } from '../../model/product.model';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatFormField, MatLabel } from '@angular/material/select';
import { TableShellComponent } from '../../shared/layouts/table-shell-component/table-shell.component';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatInput } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { MatButton, MatIconButton, MatMiniFabButton } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../shared/layouts/confirm-dialog/confirm-dialog.component';
import { ProductFormDialogComponent } from './product-create-dialog.component/product-form-dialog.component';
import { FormType } from '../../model/form-type.enum';
import { MatIconModule } from '@angular/material/icon';
import { ApiError } from '../../model/apiError.model';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [
    TableShellComponent,
    MatTableModule,
    MatSortModule,
    MatFormField,
    MatLabel,
    MatIconModule,
    MatButton,
    MatInput,
    MatButton,
    FormsModule,
    MatMiniFabButton
  ],
  templateUrl: './products.component.html',
  styleUrl: './products.component.css',
})
export class ProductsComponent {
  private readonly productService = inject(ProductService);
  private readonly dialog = inject(MatDialog);

  readonly defaultErrMsg = "An error occurred"

  isLoading = input.required<boolean>();
  productData = input.required<Product[]>();
  dataSource = new MatTableDataSource<Product>([]);

  productsChanged = output<void>();

  searchQuery = signal<string>('');

  @ViewChild(MatSort) set sortRef(sort: MatSort | undefined) {
    if (sort) {
      this.dataSource.sort = sort;
    }
  }

  columnsToDisplay = ['id', 'name', 'description', 'unitOfMeasure', 'reorderPoint', 'quantityOnHand', 'actionButtons'];

  constructor() {
    this.createFilterPredicate()

    effect(() => {
      this.dataSource.data = this.productData();
    })
  }

  filterTable() {
    this.dataSource.filter = this.searchQuery();
  }

  createFilterPredicate() {
    this.dataSource.filterPredicate = (product, filter) => {
      const dataString = Object.values(product).join(' ').toLowerCase();
      const matchesSearch = dataString.includes(filter.toLowerCase());

      return matchesSearch;
    }
  }

  addProduct() {
    const dialogRef = this.dialog.open(ProductFormDialogComponent, {
      data: {
        type: FormType.Create,
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (!result) {
        return;
      }

      this.productService.addProduct(result).subscribe({
        next: () => {
          this.productsChanged.emit()
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
              dialogTitle: "Failed to add product",
              dialogMessage: errorMessage,
              okButtonOnly: true
            }
          })
        }
      });
    });
  }

  editProduct(product: Product) {
    const dialogRef = this.dialog.open(ProductFormDialogComponent, {
      data: {
        type: FormType.Edit,
        formData: product
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (!result) {
        return;
      }

      this.productService.editProduct(product.id, result).subscribe({
        next: () => {
          this.productsChanged.emit()
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
              dialogTitle: "Failed to edit product",
              dialogMessage: errorMessage,
              okButtonOnly: true
            }
          })
        }
      });
    });
  }

  deleteProduct(product: Product) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        dialogTitle: "Delete Product",
        dialogMessage: "Are you sure you want to delete this product?"
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (!result) {
        return;
      }

      this.productService.deleteProduct(product.id).subscribe({
        next: () => {
          this.productsChanged.emit()
        },
        error: (error: ApiError) => {
          this.dialog.open(ConfirmDialogComponent, {
            data: {
              dialogTitle: "Failed to delete product",
              dialogMessage: error.message,
              okButtonOnly: true
            }
          })
        }
      });
    });
  }
}
