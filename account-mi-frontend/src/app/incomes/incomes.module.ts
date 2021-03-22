import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {AppSharedModule} from '../shared/app-shared.module';
import {IncomesRoutingModule} from './incomes-routing.module';
import {IncomesComponent} from './incomes.component';
import {IncomesListComponent} from './incomes-list/incomes-list.component';
import {NewIncomeComponent} from './new-income/new-income.component';
import {MatCardModule} from '@angular/material/card';

@NgModule({
  declarations: [
    IncomesComponent,
    IncomesListComponent,
    NewIncomeComponent,
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgbModule,
    AppSharedModule,
    IncomesRoutingModule
  ],
  exports: [
    MatCardModule
  ]
})
export class IncomesModule {
}
