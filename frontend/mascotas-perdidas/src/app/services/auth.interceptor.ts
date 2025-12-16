import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('jwt');
  
  console.log('Interceptor ejecutándose');
  console.log('URL:', req.url);
  console.log('Token encontrado:', token ? 'SÍ' : 'NO');
  
  if (token) {
    const cloned = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    console.log('Header Authorization agregado');
    return next(cloned);
  }
  
  console.log('No se agregó Authorization header');
  return next(req);
};