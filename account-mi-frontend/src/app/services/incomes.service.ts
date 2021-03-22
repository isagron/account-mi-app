import {Income, IncomePage} from '../models/income.mode';
import {BehaviorSubject, Observable, Subject} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {AccountService} from './account.service';
import {Page} from '../models/utils.model';

@Injectable()
export class IncomesService {


  private incomeType: string[] = [];
  private years: string[] = [];

  public incomesSubject: Subject<Income[]> = new Subject();

  public incomesPageSubject: BehaviorSubject<IncomePage> = new BehaviorSubject<IncomePage>(null);

  public incomeTypesSubject: BehaviorSubject<string[]> = new BehaviorSubject<string[]>(null);

  public addNewIncomeSubject: Subject<Income> = new Subject();

  public availableYearsSubject: Subject<string[]> = new Subject();


  // url info
  private incomeUrl: string = environment.baseUrl + environment.rootApi + '/incomes';

  public static defaultPage(): Page {
    return new Page(0, 10);
  }

  constructor(private http: HttpClient, private accountService: AccountService) {
    this.init();
  }

  init() {
    this.accountService.accountSubject.subscribe(account => {
      if (account != null) {
        this.fetchIncomes(IncomeFilter.emptyFilter(), IncomesService.defaultPage());
        this.fetchIncomeTypes();
        this.getAvailableYears();
      }
    });
  }

  public fetchIncomeTypes() {
    this.http.get<string[]>(this.incomeUrl + '/types',
      {params: new HttpParams().append('accountId', this.accountService.activeAccountId)})
      .subscribe(incomeTypes => {
        if (incomeTypes != null) {
          this.incomeType = incomeTypes;
        }
        this.incomeTypesSubject.next(this.incomeType.slice());
      });
  }

  public getAvailableYears() {
    this.http.get<number[]>(this.incomeUrl + '/years',
      {params: new HttpParams().append('accountId', this.accountService.activeAccountId)})
      .subscribe(years => {
        if (years != null) {
          this.years = years.map(y => '' + y);
        } else {
          this.years = [];
        }
        this.availableYearsSubject.next(this.years.slice());
      });
  }


  public createNewIncome(income: Income) {
    income.accountId = this.accountService.activeAccountId;
    this.http.post<Income>(this.incomeUrl, income)
      .subscribe(incomeResponse => {
        this.addNewIncomeSubject.next(incomeResponse);

        // notify on new type only if succeed
        if (income.type != null && this.incomeType.indexOf(income.type) === -1) {
          this.incomeType.push(income.type);
          this.incomeTypesSubject.next(this.incomeType.slice());
        }

        const year: string = '' + income.date.getUTCFullYear();
        if (income.date != null && this.years.indexOf(year) < 0) {
          this.years.push(year);
          this.availableYearsSubject.next(this.years.slice());
        }
      });
  }

  public fetchIncomes(incomeFilter: IncomeFilter, page: Page) {
    // build request param
    let params: HttpParams = new HttpParams();
    params = params.append('accountId', this.accountService.activeAccountId);

    if (incomeFilter.type != null) {
      params = params.append('type', incomeFilter.type);
    }
    if (incomeFilter.month != null) {
      params = params.append('month', '' + incomeFilter.month);
    }
    if (incomeFilter.year != null) {
      params = params.append('year', '' + incomeFilter.year);
    }
    if (page.page != null) {
      params = params.append('page', '' + page.page);
    }
    if (page.size != null) {
      params = params.append('size', '' + page.size);
    }
    const url = this.incomeUrl + '/page';
    this.http.get<IncomePage>(url,
      {
        headers: {},
        params
      }
    ).subscribe(incomesPage => {
      this.incomesPageSubject.next(incomesPage);
    }, error => {
      console.log('Http response error');

      console.log(error);
    });
  }

  deleteIncome(id: string): Observable<any> {
    return this.http.delete<any>(this.incomeUrl + '/' + id);
  }
}

export class IncomeFilter {

  constructor(public year: number, public month: number, public type: string) {
  }

  static emptyFilter(): IncomeFilter {
    return new IncomeFilter(null, null, null);
  }
}
