import {RouterModule, Routes} from '@angular/router';
import {IncomesComponent} from '../incomes/incomes.component';
import {AuthGuard} from '../auth/auth.guard';
import {AccountGuard} from './AccountGuard';
import {NgModule} from '@angular/core';
import {SettingsComponent} from './settings.component';

const routes: Routes = [
  { path: 'settings', component: SettingsComponent, canActivate: [AuthGuard]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SettingsRoutingModule {

}
