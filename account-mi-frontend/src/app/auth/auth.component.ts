import {Component, OnInit} from '@angular/core';
import {NgForm} from '@angular/forms';
import {AuthService} from '../services/auth.service';
import {Router} from '@angular/router';
import {environment} from '../../environments/environment';
import {UnsecuredUserService} from '../services/unsecured-user.service';
import {catchError} from 'rxjs/operators';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent implements OnInit {
  isLoginMode = true;
  isLoading = false;
  error: string = null;
  security = true;

  constructor(private authService: AuthService, private userService: UnsecuredUserService, private router: Router) {
  }

  ngOnInit(): void {
    this.security = environment.enable_security;
  }


  onSubmit(form: NgForm) {
    const email = form.value.email;
    const password = form.value.password;

    if (this.security) {
      this.authService.signin(email, password)
        .subscribe(user => {
          this.error = null;
          console.log(user);
          this.isLoading = false;
          this.router.navigate(['/home']);

          },
          errorMessage => {
            console.log(errorMessage);
            this.error = errorMessage;
            this.isLoading = false;

          });
    } else {
      this.userService.setUser(email);
      this.router.navigate(['/home']);
    }
    form.reset();
  }


}
