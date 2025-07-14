import { HttpInterceptorFn } from '@angular/common/http';


export const httpInter: HttpInterceptorFn = (req, next) => {
  // Ne pas ajouter le header Authorization pour register et login
  if (
    req.url.endsWith('/auth/register') ||
    req.url.endsWith('/auth/login')
  ) {
    return next(req);
  }

  const credentials = localStorage.getItem('authCredentials');
  if (credentials) {
    const cloned = req.clone({
      setHeaders: {
        Authorization: `Basic ${credentials}`
      }
    });
    return next(cloned);
  }
  return next(req);
};
