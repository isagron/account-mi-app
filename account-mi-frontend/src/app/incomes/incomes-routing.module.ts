import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthGuard} from '../auth/auth.guard';
import {AccountGuard} from '../settings/AccountGuard';
import {IncomesComponent} from './incomes.component';

const routes: Routes = [
  { path: 'incomes', component: IncomesComponent, canActivate: [AuthGuard, AccountGuard]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class IncomesRoutingModule {

}
