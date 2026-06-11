import { Injectable, inject, signal, effect } from '@angular/core';
import { DOCUMENT } from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  private document = inject(DOCUMENT);
  
  isDarkTheme = signal<boolean>(false);

  constructor() {
    const savedTheme = localStorage.getItem('theme');
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
    
    this.isDarkTheme.set(savedTheme === 'dark' || (!savedTheme && prefersDark));

    effect(() => {
      const isDark = this.isDarkTheme();
      const classList = this.document.documentElement.classList;

      if (isDark) {
        classList.add('dark-mode');
        localStorage.setItem('theme', 'dark');
      } else {
        classList.remove('dark-mode');
        localStorage.setItem('theme', 'light');
      }
    });
  }

  toggleTheme(): void {
    this.isDarkTheme.update(state => !state);
  }
}
