import { Component, input, signal, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-table-shell',
  standalone: true,
  imports: [MatPaginator, MatProgressSpinner],
  templateUrl: './table-shell.component.html',
  styleUrl: './table-shell.component.css',
})

export class TableShellComponent {
  dataSource = input.required<MatTableDataSource<any>>();
  isLoading = input.required<boolean>();
  
  @ViewChild(MatPaginator) set paginatorRef(paginator: MatPaginator | undefined) {
    if (paginator) {
      this.dataSource().paginator = paginator;
    }
  }
} 
