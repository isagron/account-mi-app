import { Directive, Input } from '@angular/core';
import { NG_VALIDATORS, Validator, FormControl } from '@angular/forms';

@Directive({
  selector: '[positive][formControlName], [positive][formControl], [positive][ngModel]',
  providers: [{provide: NG_VALIDATORS, useExisting: PositiveNumberDirective, multi: true}]
})
export class PositiveNumberDirective implements Validator {


  validate(c: FormControl): {[key: string]: any} {
    let v = c.value;
    return ( v < 0) ? {'positive': true} : null;
  }
}
