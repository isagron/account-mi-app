import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {AccountService} from '../services/account.service';

@Injectable()
export class AccountGuard implements CanActivate {

  constructor(private accountService: AccountService, private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
    Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (this.accountService.activeAccountId === null) {
      return this.router.createUrlTree(['/settings']);
    }
    return true;

  }
}
