import {NgModule} from '@angular/core';
import {ExpenseComponent} from '../expense/expense.component';
import {ExpenseListComponent} from '../expense/expense-list/expense-list.component';
import {NewExpenseItemComponent} from '../expense/new-expense-item/new-expense-item.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {CommonModule} from '@angular/common';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatOptionModule} from '@angular/material/core';
import {AppSharedModule} from '../shared/app-shared.module';
import {ExpensesRoutingModule} from '../expense/expenses-routing.module';
import {AccountBalanceComponent} from './account-balance/account-balance.component';
import {ComparePrevVsCurComponent} from './compare-prev-vs-cur/compare-prev-vs-cur.component';
import {GoalStatProgressbarComponent} from './goal-stat-progressbar/goal-stat-progressbar.component';
import {GoalsFulfillmentComponent} from './goals-fulfillment/goals-fulfillment.component';
import {WidgetBoardComponent} from './widget-board/widget-board.component';
import {ExpenseCategoryMonthComponent} from './widget-board/expense-category-month/expense-category-month.component';
import {ExpenseTimeChartComponent} from './widget-board/expense-time-chart/expense-time-chart.component';
import {DashboardComponent} from './dashboard.component';
import {DashboardRoutingModule} from './dashboard-routing.module';
import {PieChartComponentComponent} from '../shared/components/pie-chart-component/pie-chart-component.component';
import {ChartsModule} from 'ng2-charts';

@NgModule({
  declarations: [
    AccountBalanceComponent,
    ComparePrevVsCurComponent,
    GoalStatProgressbarComponent,
    GoalsFulfillmentComponent,
    WidgetBoardComponent,
    ExpenseCategoryMonthComponent,
    ExpenseTimeChartComponent,
    PieChartComponentComponent,
    DashboardComponent,
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgbModule,
    AppSharedModule,
    DashboardRoutingModule,
    ChartsModule
  ]
})
export class DashboardModule {
}
