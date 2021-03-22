import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AppSharedModule} from '../shared/app-shared.module';
import {AuthComponent} from './auth.component';
import {SingupComponent} from './singup/singup.component';
import {AuthRoutingModule} from './auth-routing.module';

@NgModule({
  declarations: [
    AuthComponent,
    SingupComponent,
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    AppSharedModule,
    AuthRoutingModule
  ]
})
export class AuthModule {
}
