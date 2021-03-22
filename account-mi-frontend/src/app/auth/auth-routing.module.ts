import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {AuthComponent} from './auth.component';
import {SingupComponent} from './singup/singup.component';

const routes: Routes = [
  { path: '', component: AuthComponent},
  { path: 'signup', component: SingupComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule {

}
