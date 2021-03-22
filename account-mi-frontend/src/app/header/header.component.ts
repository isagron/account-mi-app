import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthService} from '../services/auth.service';
import {Subscription} from 'rxjs';
import {LoginMode} from '../models/user.model';
import {Router} from '@angular/router';
import {environment} from '../../environments/environment';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {

  isAuthenticated = false;
  mode: LoginMode = LoginMode.LOGOUT;
  private userSub: Subscription;
  isSecured = true;


  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    if (environment.enable_security) {
      this.userSub = this.authService.userDetails.subscribe(user => {
        this.isAuthenticated = !!user;
      });
    } else {
      this.isAuthenticated = true;
      this.isSecured = false;
    }
  }

  ngOnDestroy() {
    this.userSub.unsubscribe();
  }

  onSignup() {
    this.mode = LoginMode.SIGNUP;
    this.router.navigate(['/signup']);
  }

  onLogout() {
    if (this.isSecured) {
      this.authService.logout();
      this.isAuthenticated = false;
    } else {
      this.router.navigate(['/']);
    }
  }

  onLogin() {
    this.mode = LoginMode.LOGIN;
    this.router.navigate(['/']);
  }
}
