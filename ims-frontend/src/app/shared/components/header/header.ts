import { Component, DOCUMENT, inject, signal, effect } from '@angular/core';
import { MatMiniFabButton } from '@angular/material/button';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { ThemeService } from '../../../services/theme.service';

@Component({
  selector: 'app-header',
  imports: [
    MatToolbarModule,
    MatIconModule,
    MatMiniFabButton
  ],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {
  protected themeService = inject(ThemeService);
}
