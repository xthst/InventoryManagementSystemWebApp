import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Product, ProductRequest } from '../model/product.model';
import { environment } from '../../environments/environment';
import { catchError, throwError } from 'rxjs';
import { ApiError } from '../model/apiError.model';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}`

  getProducts() {
    return this.http.get<Array<Product>>(this.API_URL + "/products");
  }

  addProduct(requestBody: ProductRequest) {
    return this.http.post<Product>(
      this.API_URL + "/product",
      requestBody,
    ).pipe(
      catchError((err: HttpErrorResponse) => {
        return throwError(() => err.error as ApiError);
      })
    )
  }

  editProduct(id: number, requestBody: ProductRequest) {
    return this.http.put<Product>(
      this.API_URL + "/product/" + id.toString(),
      requestBody
    ).pipe(
      catchError((err: HttpErrorResponse) => {
        return throwError(() => err.error as ApiError);
      })
    )
  }

  deleteProduct(id: number) {
    return this.http.delete<Product>(
      this.API_URL + "/product/" + id.toString()
    ).pipe(
      catchError((err: HttpErrorResponse) => {
        return throwError(() => err.error as ApiError);
      })
    )
  }
}
