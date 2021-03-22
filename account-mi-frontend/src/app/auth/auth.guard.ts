import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {AuthService} from '../services/auth.service';
import {map, take} from 'rxjs/operators';
import {environment} from '../../environments/environment';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
    Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (environment.enable_security) {
      return this.authService.userDetails.pipe(
        take(1),
        map(user => {
          if (!!user) {
            return true;
          }
          return this.router.createUrlTree(['/']);
        }));
    } else {
      return true;
    }

  }
}
