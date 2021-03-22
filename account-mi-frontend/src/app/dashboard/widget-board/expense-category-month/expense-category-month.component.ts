import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {StatisticService} from '../../../services/statistic.service';
import {CommonValues} from '../../../models/utils.model';
import {Subscription} from 'rxjs';
import {Color} from 'ng2-charts';
import {AccountService} from '../../../services/account.service';

@Component({
  selector: 'app-expense-category-month',
  templateUrl: './expense-category-month.component.html',
  styleUrls: ['./expense-category-month.component.css']
})
export class ExpenseCategoryMonthComponent implements OnInit, OnDestroy  {

  months = [];
  selectedMonth: string;
  public pieChartLabels = [];
  public pieChartData = [];
  colors: Color[] = [];

  dataSubscription: Subscription;

  error = false;

  isLoading = false;

  constructor(private accountService: AccountService, private statisticsService: StatisticService) { }

  ngOnInit(): void {
    this.months = this.getMonths();
    this.selectedMonth = this.getCurrentMonth();
    console.log('current month:' + this.selectedMonth);
    this.dataSubscription = this.statisticsService.categoryDivisionPerMonthSubject.subscribe(response => {
      this.isLoading = false;
      if (response !== null) {
        if (response.length === 0) {
          this.error = true;
        }
        console.log(response);
        this.pieChartLabels = response.map(item => item.category);
        const backgroundColors = response.map(item => {
          return  this.getRandomColor();
        });
        this.colors = [{backgroundColor: backgroundColors}];
        this.pieChartData = response.map(item => item.amount);
      }
    });

    this.accountService.accountSubject.subscribe(account => {
      if (account) {
        this.statisticsService.fetchCategoryDivisionForCurrentMonth();
      }
    });
  }

  ngOnDestroy(): void {
    this.dataSubscription.unsubscribe();
  }

  getMonths(): string[] {
    const months = CommonValues.months.slice();
    months.push('Total');
    return months;
  }

  selectMonth(month: string) {
    this.isLoading = true;
    this.error = false;
    this.selectedMonth = month;
    const monthInt = month === 'Total' ? null : CommonValues.months.indexOf(month);
    const year = new Date().getFullYear();
    this.statisticsService.fetchCategoryDivisionForMonth(monthInt, year);
  }

  private getCurrentMonth() {
    return CommonValues.months[new Date().getMonth()];
  }

  private getRandomColor() {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }
}
