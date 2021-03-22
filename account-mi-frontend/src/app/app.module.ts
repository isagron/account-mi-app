import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormBuilder} from '@angular/forms';

import {AppComponent} from './app.component';
import {HeaderComponent} from './header/header.component';
import {GoalService} from './services/goal.service';
import {ExpenseService} from './services/expense.service';
import {IncomesService} from './services/incomes.service';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {AppRoutingModule} from './app-routing.module';
import {StatisticService} from './services/statistic.service';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatCardModule} from '@angular/material/card';
import {SettingsService} from './services/settings.service';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {AuthInterceptorService} from './services/auth-interceptor.service';
import {AuthGuard} from './auth/auth.guard';
import {AccountService} from './services/account.service';
import {AccountGuard} from './settings/AccountGuard';
import {UnsecuredUserService} from './services/unsecured-user.service';
import {ExpenseModule} from './expense/expenses.module';
import {AppSharedModule} from './shared/app-shared.module';
import {IncomesModule} from './incomes/incomes.module';
import {DashboardModule} from './dashboard/dashboard.module';
import {SettingsModule} from './settings/settings.module';
import {AuthModule} from './auth/auth.module';


@NgModule({
  exports: [
    MatCardModule,
  ],
  declarations: [
    AppComponent,
    HeaderComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    NgbModule,
    ExpenseModule,
    IncomesModule,
    DashboardModule,
    SettingsModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    AppSharedModule,
    AuthModule,
  ],
  providers: [
    GoalService,
    ExpenseService,
    IncomesService,
    StatisticService,
    SettingsService,
    AccountService,
    UnsecuredUserService,
    AuthGuard,
    AccountGuard,
    FormBuilder,
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true},
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
