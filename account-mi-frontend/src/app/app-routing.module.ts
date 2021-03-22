import {RouterModule, Routes} from '@angular/router';
import {SettingsComponent} from './settings/settings.component';
import {NgModule} from '@angular/core';
import {AuthComponent} from './auth/auth.component';
import {AuthGuard} from './auth/auth.guard';
import {SingupComponent} from './auth/singup/singup.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {AccountGuard} from './settings/AccountGuard';

const appRoutes: Routes = [
  { path: '', component: AuthComponent},
  { path: 'signup', component: SingupComponent},
];

@NgModule({
  imports: [
    RouterModule.forRoot(appRoutes),
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {}
