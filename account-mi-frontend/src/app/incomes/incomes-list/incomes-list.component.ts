import { Component, OnInit } from '@angular/core';
import {Income} from '../../models/income.mode';
import {Subscription} from 'rxjs';
import {IncomeFilter, IncomesService} from '../../services/incomes.service';
import {PaginationService} from '../../services/pagination.service';
import {Page} from '../../models/utils.model';

const ALL_TYPES = 'All';
const NO_YEAR = 'None';
const NO_MONTH = 'None';

@Component({
  selector: 'app-incomes-list',
  templateUrl: './incomes-list.component.html',
  styleUrls: ['./incomes-list.component.css']
})
export class IncomesListComponent implements OnInit {
  years: string[] = [NO_YEAR];
  months: Array<string> = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec', 'None'];


  incomeTypeOptions: string[];


  incomes: Income[];

  incomesSubscription: Subscription;


  // income list filter
  private selectedYear: number;
  private selectedMonth: number;
  selectedType: string;

  constructor(private incomeService: IncomesService, private paginationService: PaginationService) { }

  ngOnInit(): void {
    // set incomes list filter
    this.incomeService.incomeTypesSubject.subscribe(incomeTypes => {
      this.incomeTypeOptions = incomeTypes;
    });

    this.incomesSubscription = this.incomeService.incomesSubject.subscribe(
      incomes => {
        this.incomes = incomes;
      }
    );


    this.selectedType = ALL_TYPES;



    this.incomeService.availableYearsSubject.subscribe(years => {
      this.years = years;
      this.years.push(NO_YEAR);
    });

    // set income list
    this.incomeService.incomesPageSubject.subscribe(incomesPage => {
      if (incomesPage != null) {
        if (incomesPage.content == null) {
          this.incomes = [];
        } else {
          this.incomes = incomesPage.content;
        }
        this.paginationService.setPageInformation(incomesPage.pageNumber, incomesPage.pageSize, incomesPage.totalElements);
      }
    });

    this.incomeService.addNewIncomeSubject.subscribe(newIncome => {
      this.filterIncomes();
    });
  }

  selectType(type: string) {
    this.selectedType = type;
  }

  selectMonthEvent(month: string) {
    const index = this.months.findIndex(monthStr => monthStr === month);
    if (month === NO_MONTH) {
      this.selectedMonth = null;
    } else {
      this.selectedMonth = index + 1;
    }
    this.filterIncomes();

  }

  selectYearEvent(year: string) {
    if (year === NO_YEAR) {
      this.selectedYear = null;
    } else {
      this.selectedYear = +year;
    }
    this.filterIncomes();
  }

  filterIncomes() {
    let type = this.selectedType;
    if (type === ALL_TYPES) {
      type = null;
    }
    const filter = new IncomeFilter(this.selectedYear, this.selectedMonth, type);
    this.incomeService.fetchIncomes(filter, new Page(0, 10));
  }

  deleteIncome(income: Income) {
    this.incomeService.deleteIncome(income.id)
      .subscribe((result => this.filterIncomes()));
  }
}
