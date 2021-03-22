import {NgModule} from '@angular/core';
import {IncomesComponent} from '../incomes/incomes.component';
import {IncomesListComponent} from '../incomes/incomes-list/incomes-list.component';
import {NewIncomeComponent} from '../incomes/new-income/new-income.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {CommonModule} from '@angular/common';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatOptionModule, MatPseudoCheckboxModule} from '@angular/material/core';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {AppSharedModule} from '../shared/app-shared.module';
import {IncomesRoutingModule} from '../incomes/incomes-routing.module';
import {MatCardModule} from '@angular/material/card';
import {SettingsComponent} from './settings.component';
import {SettingGoalsComponent} from './setting-goal-list/setting-goals.component';
import {AddGoalItemComponent} from './setting-goal-list/add-goal-item/add-goal-item.component';
import {SettingsRoutingModule} from './settings-routing.module';

@NgModule({
  declarations: [
    SettingsComponent,
    SettingGoalsComponent,
    AddGoalItemComponent
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    AppSharedModule,
    SettingsRoutingModule
  ],
  exports: [
    SettingsComponent,
    SettingGoalsComponent,
    AddGoalItemComponent
  ]
})
export class SettingsModule {
}
