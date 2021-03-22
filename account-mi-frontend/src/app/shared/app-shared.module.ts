import {NgModule} from '@angular/core';
import {PageTitleComponent} from './components/page-title/page-title.component';
import {InputTagsComponent} from './components/input-tags/input-tags.component';
import {LoadingSpinnerComponent} from './components/loading-spinner/loading-spinner.component';
import {ModalAlertComponent} from './components/modal-alert/modal-alert.component';
import {PageinationComponent} from './components/pageination/pageination.component';
import {SelectionTabComponent} from './components/selection-tab/selection-tab.component';
import {CommonModule} from '@angular/common';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatOptionModule, MatPseudoCheckboxModule} from '@angular/material/core';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {DropdownDirective} from './dropdown.directive';
import {PositiveNumberDirective} from './positive-num-validator.directive';

@NgModule({
  declarations: [
    InputTagsComponent,
    LoadingSpinnerComponent,
    ModalAlertComponent,
    PageTitleComponent,
    PageinationComponent,
    SelectionTabComponent,
    DropdownDirective,
    PositiveNumberDirective
  ],
  imports: [
    CommonModule,
    MatAutocompleteModule,
    MatOptionModule,
    MatFormFieldModule,
    MatSlideToggleModule,
    MatPseudoCheckboxModule
  ],
  exports: [
    InputTagsComponent,
    LoadingSpinnerComponent,
    ModalAlertComponent,
    PageTitleComponent,
    PageinationComponent,
    SelectionTabComponent,
    CommonModule,
    MatAutocompleteModule,
    MatOptionModule,
    MatFormFieldModule,
    MatSlideToggleModule,
    MatPseudoCheckboxModule,
    DropdownDirective,
    PositiveNumberDirective
  ]
})
export class AppSharedModule {
}
