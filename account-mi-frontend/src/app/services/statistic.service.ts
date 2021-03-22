import {AccountBalanceModel, BalanceParameterShortSummary} from '../models/account-balance.model';
import {BehaviorSubject, Subject} from 'rxjs';
import {CategoryExpenseForMonth, CategoryPerMonthData, GoalStatus} from '../models/statistics.model';
import {CommonValues} from '../models/utils.model';
import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpParams} from '@angular/common/http';
import {AccountService} from './account.service';


@Injectable()
export class StatisticService {

  public accountBalanceSubject = new Subject<AccountBalanceModel>();
  public goalStatusSubject = new BehaviorSubject<GoalStatus[]>(null);
  public categoryDivisionPerMonthSubject = new BehaviorSubject<CategoryExpenseForMonth[]>(null);
  public expenseForMonthPerCategorySubject = new BehaviorSubject<CategoryExpenseForMonth[]>(null);

  // url info
  private statisticsUrl: string = environment.baseUrl + environment.rootApi + '/statistics';


  constructor(private http: HttpClient, private accountService: AccountService) {
  }


  public fetchWidjetsData() {
    this.fetchCategoryDivisionForCurrentMonth();
    this.fetchTotalSpendForCateogryPerMonth(null);
  }

  public fetchTotalSpendForCateogryPerMonth(category: string) {
    const params = new HttpParams().append('accountId', this.accountService.activeAccountId);
    if (category !== null) {
      params.append('category', category);
    }
    this.http.get<CategoryExpenseForMonth[]>(this.statisticsUrl + '/category-expense-per-month', {
      params
    }).subscribe(data => {
      if (data == null) {
        this.expenseForMonthPerCategorySubject.next([]);
      } else {
        this.expenseForMonthPerCategorySubject.next(data);
      }
    });
  }

  public fetchCategoryDivisionForCurrentMonth() {
    const month = new Date().getMonth();
    const year = new Date().getFullYear();
    this.fetchCategoryDivisionForMonth(month, year);
  }

  public fetchCategoryDivisionForMonth(month: number, year: number) {
    let params: HttpParams = new HttpParams();
    params = params.append('accountId', this.accountService.activeAccountId);
    if (month !== null) {
      params = params.append('month', String(month));
    }

    if (year == null) {
      year = new Date().getFullYear();
    }
    params = params.append('year', String(year));

    this.http.get<CategoryExpenseForMonth[]>(this.statisticsUrl + '/category-division-per-month', {
      params
    }).subscribe(data => {
      if (data == null) {
        this.categoryDivisionPerMonthSubject.next([]);
      } else {
        this.categoryDivisionPerMonthSubject.next(data);
      }
    });

  }

  public fetchGoalStatus(month: number, year: number) {
    if (month == null) {
      month = new Date().getMonth();
    }

    if (year == null) {
      year = new Date().getFullYear();
    }
    this.http.get<GoalStatus[]>(this.statisticsUrl + '/goal-status', {
      params: new HttpParams().append('accountId', this.accountService.activeAccountId)
        .append('month', String(month))
        .append('year', String(year))
    }).subscribe(goalsStatus => {
      if (goalsStatus == null) {
        this.goalStatusSubject.next([]);
      } else {
        this.goalStatusSubject.next(goalsStatus);
      }
    });
  }

  public fetchAccountBalanceInfo() {
    this.http.get<AccountBalanceModel>(this.statisticsUrl + '/account-balance',
      {
        params: new HttpParams().append('accountId', this.accountService.activeAccountId)
      })
      .subscribe(accountBalance => {
        if (accountBalance != null) {
          this.accountBalanceSubject.next(accountBalance);
        }
      });
  }


}
