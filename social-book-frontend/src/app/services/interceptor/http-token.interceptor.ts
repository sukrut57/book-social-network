import {HttpHeaders, HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';
import {TokenService} from '../token/token.service';

export const httpTokenInterceptor: HttpInterceptorFn = (request, next) => {
  const tokenService = inject(TokenService);
  const getToken = tokenService.token;
  if(getToken){
    const authRequest = request.clone({
      headers: new HttpHeaders({
        'Authorization': `Bearer ${getToken}`
      })
    });
    return next(authRequest);
  }
  return next(request);
};

// @Injectable()
// export class HttpTokenInterceptor implements HttpInterceptor {
//   constructor(private tokenService: TokenService) {}
//
//   intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
//     const getToken = localStorage.getItem('token');
//     if (getToken) {
//       const authRequest = request.clone({
//         headers: new HttpHeaders({
//           'Authorization': `Bearer ${getToken}`
//         })
//       });
//       return next.handle(authRequest);
//     }
//     return next.handle(request);
//   }
// }
