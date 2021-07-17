import {Component, OnDestroy, OnInit} from '@angular/core';
import {GoalStatus} from '../../models/statistics.model';
import {GoalService} from '../../services/goal.service';
import {StatisticService} from '../../services/statistic.service';
import {Subscription} from 'rxjs';
import {AccountService} from '../../services/account.service';

@Component({
  selector: 'app-goals-fulfillment',
  templateUrl: './goals-fulfillment.component.html',
  styleUrls: ['./goals-fulfillment.component.css']
})
export class GoalsFulfillmentComponent implements OnInit, OnDestroy {
  goalStats: GoalStatus[];

  goalStatSubscription = new Subscription();

  constructor(private accountService: AccountService, private statService: StatisticService) { }

  ngOnInit(): void {
    this.statService.goalStatusSubject.subscribe(goalsStatus => {
      if (goalsStatus === null) {
        this.goalStats = [];
      } else {
        this.goalStats = goalsStatus;
      }
    });
    this.accountService.accountSubject.subscribe(account => {
      if (account) {
        this.statService.fetchGoalStatus(null, null);
      }
    });

  }

  ngOnDestroy() {
    this.goalStatSubscription.unsubscribe();
  }

}
