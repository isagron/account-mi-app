import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ExpenseComponent} from './expense.component';
import {AuthGuard} from '../auth/auth.guard';
import {AccountGuard} from '../settings/AccountGuard';

const routes: Routes = [
  {path: 'expenses', component: ExpenseComponent, canActivate: [AuthGuard, AccountGuard]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ExpensesRoutingModule {

}
