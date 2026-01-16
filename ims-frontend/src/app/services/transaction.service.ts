import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Transaction, TransactionRequest } from '../model/transaction.type';
import { catchError, throwError } from 'rxjs';
import { ApiError } from '../model/apiError.model';

@Injectable({
  providedIn: 'root',
})
export class TransactionService {
  http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}`

  getTransactions() {
    return this.http.get<Array<Transaction>>(this.API_URL + '/transactions')
  }

  addTransaction(requestBody: TransactionRequest) {
    return this.http.post<Transaction>(
      this.API_URL + "/transaction",
      requestBody,
    ).pipe(
      catchError((err: HttpErrorResponse) => {
        return throwError(() => err.error as ApiError);
      })
    );
  }

  deleteTransaction(id: number) {
    return this.http.delete<Transaction>(
      this.API_URL + "/transaction/" + id.toString()
    ).pipe(
      catchError((err: HttpErrorResponse) => {
        return throwError(() => err.error as ApiError);
      })
    )
  }
}
