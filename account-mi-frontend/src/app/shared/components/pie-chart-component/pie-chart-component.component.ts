import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-pie-chart-component',
  templateUrl: './pie-chart-component.component.html',
  styleUrls: ['./pie-chart-component.component.css']
})
export class PieChartComponentComponent implements OnInit {
  @Input()
  public pieChartLabels = ['Sales Q1', 'Sales Q2', 'Sales Q3', 'Sales Q4'];
  @Input()
  public pieChartData = [120, 150, 180, 90];
  @Input()
  public pieChartType = 'pie';
  constructor() { }
  ngOnInit() {
  }

}
