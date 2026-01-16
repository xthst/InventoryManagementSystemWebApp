import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideMomentDateAdapter } from '@angular/material-moment-adapter';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideHttpClient } from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(),
    provideBrowserGlobalErrorListeners(),
    provideMomentDateAdapter(),
    provideRouter(routes)
  ]
};

