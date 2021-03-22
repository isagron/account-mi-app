import {Component, OnInit} from '@angular/core';
import {AuthService} from './services/auth.service';
import {environment} from '../environments/environment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'accout-mi';

  constructor(private authService: AuthService) {
  }

  ngOnInit() {
    console.log(environment.baseUrl);
    if (environment.enable_security) {
      this.authService.autoLogin();
    }
  }
}
