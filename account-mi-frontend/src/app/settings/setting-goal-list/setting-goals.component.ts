import {GoalInterval, GoalSetting, GoalSettingMode} from '../../models/setting-models.model';
import {Component, OnDestroy, OnInit} from '@angular/core';
import {AccountService} from '../../services/account.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-setting-goals',
  templateUrl: './setting-goals.component.html',
  styleUrls: ['./setting-goals.component.css']
})
export class SettingGoalsComponent implements OnInit, OnDestroy {
  goalSettings: GoalSetting[] = [];
  private goalSubscription: Subscription;
  public disabled = false;
  totalGoalValue = 0;

  intervalOptions: string[] = [GoalInterval.MONTH, GoalInterval.YEAR];

  constructor(private accountService: AccountService) {
  }

  ngOnInit(): void {
    this.goalSubscription = this.accountService.goalSubject.subscribe(goals => {
      this.totalGoalValue = 0;
      this.goalSettings = goals.map(goal => {
        this.totalGoalValue = this.totalGoalValue + goal.amount;
        return new GoalSetting(goal.goalId, goal.category, goal.amount, goal.interval, GoalSettingMode.SAVE);
      });

    });

  }

  ngOnDestroy() {
    this.goalSubscription.unsubscribe();
  }

  switchToEditMode(gs: GoalSetting) {
    gs.mode = GoalSettingMode.EDIT;
    this.disabled = true;
  }

  saveGoal(trGoalItem: HTMLTableRowElement, gs: GoalSetting, goalIndex: number) {
    gs.category = trGoalItem.cells.namedItem("category-cell").innerHTML;
    gs.amount = +trGoalItem.cells.namedItem("amount-cell").innerHTML;
    this.accountService.updateGoal(gs);
    gs.mode = GoalSettingMode.SAVE;
    this.disabled = false;
  }

  delete(i: number, gs: GoalSetting) {
    this.accountService.deleteGoal(gs.goalId);
  }

  ignoreEdit(trGoalItem: HTMLTableRowElement, gs: GoalSetting, goalIndex: number) {
    trGoalItem.cells.namedItem("category-cell").innerHTML = gs.category;
    trGoalItem.cells.namedItem("amount-cell").innerHTML = '' + gs.amount;
    gs.mode = GoalSettingMode.SAVE;
    this.disabled = false;
  }
}
