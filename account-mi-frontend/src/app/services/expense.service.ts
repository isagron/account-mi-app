import {Expense, ExpensePage} from '../models/expense.model';
import {BehaviorSubject, Observable, Subject} from 'rxjs';
import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {AccountService} from './account.service';
import {environment} from '../../environments/environment';
import {Page} from '../models/utils.model';

@Injectable()
export class ExpenseService {


  public expensesSubject: BehaviorSubject<ExpensePage> = new BehaviorSubject<ExpensePage>(null);


  public expenseCategoriesSubject: BehaviorSubject<string[]> = new BehaviorSubject(null);
  public yearsSubject: BehaviorSubject<number[]> = new BehaviorSubject(null);
  public storesSubject: BehaviorSubject<string[]> = new BehaviorSubject(null);
  private stores: string[];
  private expenseCategories: string[] = [];
  private filter: ExpenseFilter;
  private expensesUrl: string = environment.baseUrl + environment.rootApi + '/expenses';

  public addNewExpenseSubject: Subject<Expense> = new Subject<Expense>();

  public static defaultPage(): Page {
    return new Page(0, 10);
  }

  constructor(private  http: HttpClient, private accountService: AccountService) {
    this.init();
  }

  init(): void {
    this.accountService.accountSubject.subscribe(account => {
      if (account != null) {
        this.fetchExpense(ExpenseFilter.emptyFilter(), ExpenseService.defaultPage());
        this.getStores();
        this.getAvailableYears();
      }
    });
    this.accountService.expenseCategoriesSubject.subscribe(expenseCategories => {
      if (expenseCategories == null) {
        this.expenseCategories = [];
      } else {
        this.expenseCategories = expenseCategories;
      }
      this.expenseCategoriesSubject.next(this.expenseCategories.slice());
    });
  }

  public getStores() {
    this.http.get<string[]>(this.expensesUrl + '/stores', {
      params: new HttpParams().append('accountId', this.accountService.activeAccountId)
    }).subscribe(stores => {
      if (stores == null) {
        this.stores = [];
      } else {
        this.stores = stores;
      }
      this.storesSubject.next(this.stores.slice());
    });
  }


  getAvailableYears() {
    let params: HttpParams = new HttpParams();
    params = params.append('accountId', this.accountService.activeAccountId);
    return this.http.get<number[]>(this.expensesUrl + '/years', {
      params
    }).subscribe(years => this.yearsSubject.next(years));
  }

  addNewExpense(expense: Expense) {
    expense.accountId = this.accountService.activeAccountId;
    return this.http.post<Expense>(this.expensesUrl, expense)
      .subscribe(expenseResponse => {
        this.addNewExpenseSubject.next(expenseResponse);
        // notify on new category only if succeed
        if (expense.category != null && this.expenseCategories.indexOf(expense.category) === -1) {
          this.expenseCategories.push(expense.category);
          this.expenseCategoriesSubject.next(this.expenseCategories.slice());
        }

        // notify on new store only if succeed
        if (expense.store != null && this.stores.indexOf(expense.store) === -1) {
          this.stores.push(expense.store);
          this.storesSubject.next(this.stores.slice());
        }
      });
  }


  public fetchExpense(expenseFilter: ExpenseFilter, page: Page) {
    // build request param
    let params: HttpParams = new HttpParams();
    params = params.append('accountId', this.accountService.activeAccountId);

    if (expenseFilter.category != null) {
      params = params.append('category', expenseFilter.category);
    }
    if (expenseFilter.shop != null) {
      params = params.append('shop', expenseFilter.shop);
    }
    if (expenseFilter.month != null) {
      params = params.append('month', '' + expenseFilter.month);
    }
    if (expenseFilter.year != null) {
      params = params.append('year', '' + expenseFilter.year);
    }
    if (page.page != null) {
      params = params.append('page', '' + page.page);
    }
    if (page.size != null) {
      params = params.append('size', '' + page.size);
    }
    const url = this.expensesUrl + '/page';
    this.http.get<ExpensePage>(url,
      {
        headers: {},
        params
      }
    ).subscribe(expensePage => {
      this.expensesSubject.next(expensePage);
    }, error => {
      console.log('Http response error');

      console.log(error);
    });
  }

  deleteExpense(expense: Expense): Observable<any> {
    return this.http.delete<any>(this.expensesUrl + '/' + expense.id);
  }
}

export class ExpenseFilter {
  public year: number;
  public month: number;
  public category: string;
  public shop: string;


  constructor(year: number, month: number, category: string, shop: string) {
    this.year = year;
    this.month = month;
    this.category = category;
    this.shop = shop;
  }

  static emptyFilter(): ExpenseFilter {
    return new ExpenseFilter(null, null, null, null);
  }
}

