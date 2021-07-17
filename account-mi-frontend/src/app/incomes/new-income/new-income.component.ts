import { Component, OnInit } from '@angular/core';
import {Income} from '../../models/income.mode';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {IncomesService} from '../../services/incomes.service';
import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';
import {ThemePalette} from '@angular/material/core';



@Component({
  selector: 'app-new-income',
  templateUrl: './new-income.component.html',
  styleUrls: ['./new-income.component.css']
})
export class NewIncomeComponent implements OnInit {
  color: ThemePalette = 'accent';

  addNewIncomeForm: FormGroup;
  dateModel: NgbDateStruct;

  filteredOptions: Observable<string[]>;
  incomeTypeOptions: string[];

  constructor(private incomeService: IncomesService) { }

  ngOnInit(): void {

    this.addNewIncomeForm = new FormGroup({
      type: new FormControl(null, Validators.required),
      amount: new FormControl(null, [Validators.required, Validators.min(0)]),
      date: new FormControl(null, Validators.required),
      consistent: new FormControl({value: false, disabled: false})
    });

    this.filteredOptions = this.addNewIncomeForm.get('type').valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value))
    );

    // set incomes list filter
    this.incomeService.incomeTypesSubject.subscribe(incomeTypes => {
      this.incomeTypeOptions = incomeTypes;
    });
  }

  addNewIncome() {
    const date = new Date(this.dateModel.year, this.dateModel.month - 1, this.dateModel.day);
    const offsetInHours = date.getTimezoneOffset() / -60;
    date.setHours(offsetInHours);
    const income = new Income(this.addNewIncomeForm.value.type,
      this.addNewIncomeForm.value.amount, date,
      this.addNewIncomeForm.value.consistent);
    this.incomeService.createNewIncome(income);
    this.addNewIncomeForm.reset();
  }

  private _filter(value: string): string[] {
    if (this.incomeTypeOptions == null) {
      return [];
    }
    const filterValue = value == null ? '' : value.toLowerCase();

    return this.incomeTypeOptions.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
  }

}
