import {Injectable} from '@angular/core';
import {Account} from '../models/Account.model';
import {HttpClient, HttpParams} from '@angular/common/http';
import {BehaviorSubject, Observable, Subject} from 'rxjs';
import {environment} from '../../environments/environment';
import {GoalDto, GoalSetting} from '../models/setting-models.model';
import {AuthService} from './auth.service';
import {Router} from '@angular/router';
import {UnsecuredUserService} from './unsecured-user.service';

@Injectable()
export class AccountService {

  public GET_OR_CREATE = 'get-or-create';

  activeAccountId: string;

  private accountsUrl: string;
  private accountsUrlAnnotation = '/accounts';

  public goalSubject = new BehaviorSubject<GoalDto[]>(null);
  public expenseCategoriesSubject = new BehaviorSubject<string[]>(null);
  public incomeCategoriesSubject = new BehaviorSubject<string[]>(null);
  public accountSubject = new BehaviorSubject<Account>(null);


  constructor(private http: HttpClient,
              private auth: AuthService,
              private router: Router,
              private userService: UnsecuredUserService) {
    this.accountsUrl = environment.baseUrl + environment.rootApi + this.accountsUrlAnnotation;
    if (environment.enable_security) {
      this.auth.userDetails.subscribe(user => {
        if (user !== null) {
          console.log('start to initialize account');
          this.init('/home');
        } else {
          console.log('no user found');
        }
      });
    } else {
      this.userService.userSubject.subscribe(user => {
        if (user !== null) {
          console.log('start to initialize account');
          this.init('/home');
        } else {
          console.log('no user found');
        }
      });
    }
  }

  public init(redirectUrl: string) {
    this.getOrCreateEmptyAccount()
      .subscribe(account => {
        this.setWorkingAccount(account);
        if (redirectUrl !== null) {
          this.router.navigate([redirectUrl]);
        }
      });
  }


  private handleAccountUpdates(account: Account) {
    this.activeAccountId = account.accountId;
    this.incomeCategoriesSubject.next(account.incomeCategories);
    this.expenseCategoriesSubject.next(account.expenseCategories);
    this.goalSubject.next(account.goals);
    this.accountSubject.next(account);
  }


  addExpenseCategory(category: string) {
    this.http.post<Account>(this.accountsUrl + '/' + this.activeAccountId + '/expense-categories', {categoryName: category})
      .subscribe(account => {
        this.expenseCategoriesSubject.next(account.expenseCategories);
      });
  }

  addIncomeCategory(category: string) {
    this.http.post<Account>(this.accountsUrl + '/' + this.activeAccountId + '/income-categories', {categoryName: category})
      .subscribe(account => {
        this.incomeCategoriesSubject.next(account.incomeCategories);
      });
  }

  deleteExpenseCategory(category: string) {
    this.http.delete<Account>(this.accountsUrl + '/' + this.activeAccountId + '/expense-categories/' + category)
      .subscribe(account => {
        this.expenseCategoriesSubject.next(account.expenseCategories);
      });
  }

  deleteIncomeCategory(category: string) {
    this.http.delete<Account>(this.accountsUrl + '/' + this.activeAccountId + '/income-categories/' + category)
      .subscribe(account => {
        this.incomeCategoriesSubject.next(account.incomeCategories);
      });
  }

  addNewGoal(goal: GoalDto) {
    this.http.post<Account>(this.accountsUrl + '/' + this.activeAccountId + '/goals', goal)
      .subscribe(account => {
        this.goalSubject.next(account.goals);
      });
  }

  public getOrCreateEmptyAccount(): Observable<Account> {
    return this.http.get<Account>(this.accountsUrl + '/' + this.GET_OR_CREATE);
  }

  public setWorkingAccount(account: Account) {
    this.handleAccountUpdates(account);
  }

  deleteGoal(goalId: string) {
    this.http.delete<Account>(this.accountsUrl + '/' + this.activeAccountId + '/goals/' + goalId)
      .subscribe(account => {
        this.goalSubject.next(account.goals);
      });
  }

  updateGoal(gs: GoalSetting) {
    this.http.put<Account>(this.accountsUrl + '/' + this.activeAccountId + '/goals/' + gs.goalId,
      this.convertToGoalDto(gs))
      .subscribe(account => {
        this.goalSubject.next(account.goals);
      });
  }

  private convertToGoalDto(gs: GoalSetting) {
    return new GoalDto(gs.goalId, gs.category, gs.amount, gs.interval);
  }
}
