import {NgModule} from '@angular/core';
import {ExpenseComponent} from './expense.component';
import {ExpenseListComponent} from './expense-list/expense-list.component';
import {NewExpenseItemComponent} from './new-expense-item/new-expense-item.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AppSharedModule} from '../shared/app-shared.module';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {ExpensesRoutingModule} from './expenses-routing.module';

@NgModule({
  declarations: [
    ExpenseComponent,
    ExpenseListComponent,
    NewExpenseItemComponent,
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgbModule,
    AppSharedModule,
    ExpensesRoutingModule
  ]
})
export class ExpenseModule {
}
