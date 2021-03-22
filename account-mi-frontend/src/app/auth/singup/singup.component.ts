import {Component, OnInit, ViewChild} from '@angular/core';
import {FormArray, FormControl, FormGroup, NgForm, NgModel, ValidatorFn, Validators} from '@angular/forms';
import {Observable} from 'rxjs';
import {AuthResponseData, AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';
import {AccountService} from '../../services/account.service';
import {CreateAccountRequest} from '../../models/Account.model';
import {catchError} from 'rxjs/operators';

@Component({
  selector: 'app-singup',
  templateUrl: './singup.component.html',
  styleUrls: ['./singup.component.css']
})
export class SingupComponent implements OnInit {

  isLoading = true;

  signupForm: FormGroup;
  expenseCategories: string[] = [];
  incomeCategories: string[] = [];

  constructor(private authService: AuthService, private router: Router, private accountService: AccountService) {
  }

  ngOnInit(): void {
    this.signupForm = new FormGroup({
      name: new FormControl(null, [Validators.required]),
      email: new FormControl(null, [Validators.required, Validators.email]),
      passwords: new FormGroup({
        password: new FormControl(null, [Validators.required]),
        confirmPassword: new FormControl(null, [Validators.required]),
      }, [this.validateConfirmPassword.bind(this)]),

      currentBalance: new FormControl(0, [Validators.required]),
    });
  }


  validateConfirmPassword(control: FormControl): { [key: string]: boolean } {
    console.log(this.signupForm);
    console.log(control.get('password').value);
    console.log(control.get('confirmPassword').value);
    if (control.get('password').value !== control.get('confirmPassword').value) {
      console.log('valid');
      return {'passwordMustBeMatch': true};
    }
    return null;
  }


  onSubmit() {
    const email = this.signupForm.value.email;
    const passwords = this.signupForm.value.passwords;

    this.isLoading = true;

    this.authService.signup(email, passwords.password, passwords.confirmPassword)
      .subscribe(user => {
          console.log(user);
          // this.accountService.createAccount(new CreateAccountRequest(resData.localId, null, [], []));
          this.isLoading = false;
          this.router.navigate(['/home']);
        },
        errorMessage => {
          this.isLoading = false;
        });
    this.signupForm.reset();
  }


}
