import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {AuthService} from './auth.service';
import {BehaviorSubject, from, Observable, throwError} from 'rxjs';
import {catchError, exhaustMap, filter, finalize, switchMap, take} from 'rxjs/operators';
import {AppUser} from '../models/user.model';
import {environment} from '../../environments/environment';
import {UnsecuredUserService} from './unsecured-user.service';


@Injectable()
export class AuthInterceptorService implements HttpInterceptor {

  private refreshTokenInProgress = false;
  private token: string;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(
    null
  );

  constructor(private authService: AuthService, private userService: UnsecuredUserService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (environment.enable_security) {
      return this.handleSecuredRequest(req, next);
    } else {
      return this.handleUnsecuredRequest(req, next);
    }
  }

  private handleSecuredRequest(req: HttpRequest<any>, next: HttpHandler) {
    return this.authService.userDetails.pipe(
      take(1),
      exhaustMap(user => {
        if (!user) {
          return next.handle(req);
        }

        return next.handle(this.addAuthenticationToken(req, user.token)).pipe(
          catchError((error: HttpErrorResponse) => {
            if (error && error.status === 401) {
              if (this.refreshTokenInProgress) {
                return this.refreshTokenSubject.pipe(
                  filter(token => token !== null),
                  take(1),
                  switchMap(() => next.handle(this.addAuthenticationToken(req, user.token)))
                );
              } else {
                this.refreshTokenInProgress = true;
                this.refreshTokenSubject.next(null);
                return from(this.authService.refreshAccessToken()).pipe(
                  switchMap((appUser: AppUser) => {
                    this.refreshTokenSubject.next(appUser.token);
                    return next.handle(this.addAuthenticationToken(req, appUser.token));
                  }),
                  finalize(() => this.refreshTokenInProgress = false)
                );
              }
            } else {
              return throwError(error);
            }
          })
        );
      })
    );
  }

  private addAuthenticationToken(req: HttpRequest<any>, token: string): HttpRequest<any> {
    return req.clone({
      params: req.params.append('authorization', token)
    });
  }

  private handleUnsecuredRequest(req: HttpRequest<any>, next: HttpHandler) {
    return this.userService.userSubject.pipe(
      take(1),
      exhaustMap(user => {
        if (!user) {
          return next.handle(req);
        }
        const newReq = req.clone({
          params: req.params.append('userId', user)
        });
        return next.handle(newReq);
      }));
  }
}
