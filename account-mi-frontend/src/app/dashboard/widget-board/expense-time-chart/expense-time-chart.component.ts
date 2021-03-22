import {Component, OnDestroy, OnInit} from '@angular/core';
import {StatisticService} from '../../../services/statistic.service';
import {AccountService} from '../../../services/account.service';
import {Subscription} from 'rxjs';
import {CommonValues} from '../../../models/utils.model';


const TOTAL_CATEGORY = 'Total';

@Component({
  selector: 'app-expense-time-chart',
  templateUrl: './expense-time-chart.component.html',
  styleUrls: ['./expense-time-chart.component.css']
})
export class ExpenseTimeChartComponent implements OnInit, OnDestroy {

  selectedCategory: string;
  public barChartOptions = {
    scaleShowVerticalLines: false,
    responsive: true
  };
  public barChartLabels = [];
  public barChartType = 'line';
  public barChartLegend = true;
  public barChartData = [];
  public categories = [];

  categoriesSubscription: Subscription;

  chartDataSub: Subscription;

  constructor(private statisticService: StatisticService, private accountService: AccountService) {
  }

  ngOnInit(): void {
    this.selectedCategory = TOTAL_CATEGORY;
    this.categoriesSubscription = this.accountService.expenseCategoriesSubject.subscribe(categories => {
      if (categories == null) {
        this.categories = [];
      } else {
        this.categories = categories.slice();
      }
      this.categories.push(TOTAL_CATEGORY);
    });

    this.chartDataSub = this.statisticService.expenseForMonthPerCategorySubject.subscribe(data => {
      const goalsDataArray = [];
      const barLabels = [];
      const values = [];
      if (data !== null) {

        data.sort((item1, item2) => {
          if (item1.year !== item2.year) {
            return item1.year - item2.year;
          }
          return item1.month - item2.month;
        }).forEach(monthData => {
          goalsDataArray.push(monthData.goal);
          barLabels.push(CommonValues.months[monthData.month - 1]);
          values.push(monthData.amount);
        });
      }

      this.barChartData = [
        {data: values, label: 'Expense', fill: false},
        {data: goalsDataArray, label: 'Goal', fill: false}
      ];
      this.barChartLabels = barLabels;

    });

    this.accountService.accountSubject.subscribe(account => {
      if (account) {
        this.statisticService.fetchTotalSpendForCateogryPerMonth(null);
      }
    });
  }

  ngOnDestroy(): void {
    if (this.categoriesSubscription !== null) {
      this.categoriesSubscription.unsubscribe();
    }
    if (this.chartDataSub !== null) {
      this.chartDataSub.unsubscribe();
    }
  }


  selectCategory(category: string) {
    this.selectedCategory = category;
    category = category === TOTAL_CATEGORY ? null : category;
    this.statisticService.fetchTotalSpendForCateogryPerMonth(category);
  }
}
