import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {SettingsService} from '../../../services/settings.service';
import {GoalDto, GoalInterval, GoalSetting} from '../../../models/setting-models.model';
import {Observable} from 'rxjs';
import {AccountService} from '../../../services/account.service';
import {map, startWith} from 'rxjs/operators';

@Component({
  selector: 'app-add-goal-item',
  templateUrl: './add-goal-item.component.html',
  styleUrls: ['./add-goal-item.component.css']
})
export class AddGoalItemComponent implements OnInit {

  addNewGoalSettingForm: FormGroup;

  // interval
  intervalOptions: string[] = [GoalInterval.MONTH, GoalInterval.YEAR];

  // category autocomplete
  filteredCategoryOptions: Observable<string[]>;

  private categoryOptions: string[] = [];

  constructor(private settingsService: SettingsService, private accountService: AccountService) { }

  ngOnInit(): void {
    this.addNewGoalSettingForm = new FormGroup({
      category: new FormControl('', Validators.required),
      amount: new FormControl(0, [Validators.required, Validators.min(0)]),
      interval: new FormControl(GoalInterval.MONTH, Validators.required)
    });
    this.accountService.expenseCategoriesSubject
      .subscribe(categories => {
        if (categories == null) {
          this.categoryOptions = [];
        } else {
          this.categoryOptions = categories;
        }
      });
    this.filteredCategoryOptions = this.addNewGoalSettingForm.controls['category'].valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value))
    );
  }

  addNewGoalSetting() {
    const category = this.addNewGoalSettingForm.value.category;
    const amount = this.addNewGoalSettingForm.value.amount;
    const interval = this.addNewGoalSettingForm.value.interval;
    const intervalValue = interval as GoalInterval;
    this.accountService.addNewGoal(new GoalDto(null, category, amount, interval as GoalInterval));
  }

  private _filter(value: string): string[] {
    const filterValue = value == null ? '' : value.toLowerCase();
    return this.categoryOptions.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
  }
}
