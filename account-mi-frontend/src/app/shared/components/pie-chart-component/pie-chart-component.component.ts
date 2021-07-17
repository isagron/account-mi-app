import {Component, Input, OnInit} from '@angular/core';
import {ChartOptions} from "chart.js";

@Component({
  selector: 'app-pie-chart-component',
  templateUrl: './pie-chart-component.component.html',
  styleUrls: ['./pie-chart-component.component.css']
})
export class PieChartComponentComponent implements OnInit {
  @Input()
  public pieChartLabels = [];
  @Input()
  public pieChartData = [];
  @Input()
  public pieChartType = 'pie';


  options: ChartOptions = {};

  constructor() {
  }

  ngOnInit() {
  }

}
