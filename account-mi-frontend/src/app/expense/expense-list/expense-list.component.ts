import {Expense} from '../../models/expense.model';
import {Component, OnDestroy, OnInit} from '@angular/core';
import {ExpenseFilter, ExpenseService} from '../../services/expense.service';
import {Subscription} from 'rxjs';
import {Page} from '../../models/utils.model';
import {PaginationService} from '../../services/pagination.service';


const ALL_CATEGORY = 'All';
const NO_YEAR = 'None';
const NO_MONTH = 'None';

@Component({
  selector: 'app-expense-list',
  templateUrl: './expense-list.component.html',
  styleUrls: ['./expense-list.component.css'],
  providers: [PaginationService]
})
export class ExpenseListComponent implements OnInit, OnDestroy {

  years: Array<string> = [];
  months: Array<string> = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec', 'None'];

  expenses: Expense[];
  categories: string[];
  shops: Array<string>;

  private selectedYear: number;
  private selectedMonth: number;
  selectedCategory: string;
  private selectedShop: string;

  private expensesSubscription: Subscription;
  private categoriesSubscription: Subscription;
  private addNewExpenseSubscription: Subscription;

  constructor(public expenseService: ExpenseService, public paginationService: PaginationService) {
  }

  ngOnInit(): void {
    // set data filter
    this.expenseService.yearsSubject
      .subscribe(years => {
        if (years !== null) {
          this.years = years.map(y => y.toString()).slice();
          this.years.push(NO_YEAR);
        }
      });

    // set default
    this.selectedCategory = ALL_CATEGORY;


    // subscribe
    this.expensesSubscription = this.expenseService.expensesSubject.subscribe(expensePage => {
      if (expensePage != null) {
        if (expensePage.content != null) {
          this.expenses = expensePage.content;
        }
        this.paginationService.setPageInformation(expensePage.pageNumber, expensePage.pageSize, expensePage.totalElements);
      }
    });

    this.categoriesSubscription = this.expenseService.expenseCategoriesSubject.subscribe(categories => {
      if (categories != null) {
        this.categories = categories;
      } else {
        this.categories = [];
      }
      this.categories.push(ALL_CATEGORY);
    });

    this.expenseService.storesSubject.subscribe(stores => {
      this.shops = stores.slice();
    });

    this.addNewExpenseSubscription = this.expenseService.addNewExpenseSubject
      .subscribe(newExpense => {
        this.expenseService.fetchExpense(this.createFilter(), new Page(0, 10));
      });

  }

  ngOnDestroy(): void {
    if (this.addNewExpenseSubscription != null) {
      this.addNewExpenseSubscription.unsubscribe();
    }
    if (this.expensesSubscription != null) {
      this.expensesSubscription.unsubscribe();
    }
    if (this.categoriesSubscription != null) {
      this.categoriesSubscription.unsubscribe();
    }
  }


  filterExpenses() {

    this.expenseService.fetchExpense(this.createFilter(), new Page(0, 10));
  }

  private createFilter(): ExpenseFilter {
    let category = this.selectedCategory;
    if (category === ALL_CATEGORY) {
      category = null;
    }
    return new ExpenseFilter(this.selectedYear, this.selectedMonth, category, this.selectedShop);
  }

  selectCategory(category: string) {
    this.selectedCategory = category;
    this.filterExpenses();
  }

  selectMonthEvent(month: string) {
    const index = this.months.findIndex(monthStr => monthStr === month);
    if (month === NO_MONTH) {
      this.selectedMonth = null;
    } else {
      this.selectedMonth = index + 1;
    }
    this.filterExpenses();

  }

  selectYearEvent(year: string) {
    if (year === NO_YEAR) {
      this.selectedYear = null;
    } else {
      this.selectedYear = +year;
    }
    this.filterExpenses();
  }

  goToPage(pageNumberLabel: number) {
    this.expenseService.fetchExpense(this.createFilter(), new Page(pageNumberLabel, 10));
  }

  deleteExpense(expense: Expense) {
    this.expenseService.deleteExpense(expense)
      .subscribe(() => this.filterExpenses());
  }
}
