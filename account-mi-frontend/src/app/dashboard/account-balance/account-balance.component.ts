import {Component, OnDestroy, OnInit} from '@angular/core';
import {StatisticService} from '../../services/statistic.service';
import {AccountBalanceModel, BalanceParameterShortSummary} from '../../models/account-balance.model';
import {Subscription} from 'rxjs';
import {AccountService} from '../../services/account.service';

@Component({
  selector: 'app-account-balance',
  templateUrl: './account-balance.component.html',
  styleUrls: ['./account-balance.component.css']
})
export class AccountBalanceComponent implements OnInit, OnDestroy {
  accountBalance: AccountBalanceModel = new AccountBalanceModel(
    new BalanceParameterShortSummary(0, 0, 0),
    new BalanceParameterShortSummary(0, 0, 0),
    new BalanceParameterShortSummary(0, 0, 0)
  );
  private accountBalanceSubscription: Subscription;

  constructor(private accountService: AccountService, public statisticService: StatisticService) {
  }

  ngOnInit(): void {
    this.accountBalanceSubscription = this.statisticService.accountBalanceSubject.subscribe(accountBalance => {
      this.accountBalance = accountBalance;
    });

    this.accountService.accountSubject.subscribe(account => {
      if (account) {
        this.statisticService.fetchAccountBalanceInfo();
      }
    });
  }

  ngOnDestroy() {
    this.accountBalanceSubscription.unsubscribe();
  }

}
