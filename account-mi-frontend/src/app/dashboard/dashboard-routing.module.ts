import {RouterModule, Routes} from '@angular/router';
import {AuthGuard} from '../auth/auth.guard';
import {AccountGuard} from '../settings/AccountGuard';
import {NgModule} from '@angular/core';
import {DashboardComponent} from './dashboard.component';

const routes: Routes = [
  {path: 'home', component: DashboardComponent, canActivate: [AuthGuard, AccountGuard]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule {

}
