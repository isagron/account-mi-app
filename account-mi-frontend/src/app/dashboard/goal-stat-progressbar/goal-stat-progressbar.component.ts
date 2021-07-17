import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-goal-stat-progressbar',
  templateUrl: './goal-stat-progressbar.component.html',
  styleUrls: ['./goal-stat-progressbar.component.css']
})
export class GoalStatProgressbarComponent implements OnInit {

  @Input() title: string;
  @Input() actual: number;
  @Input() goal: number;

  constructor() { }

  ngOnInit(): void {
    console.log(this.title);
    console.log(this.actual);
    console.log('total:' + this.goal);
  }


  isAboveExpected() {
    return this.goal < this.actual;
  }

  getActualNormalizeProgress() {
    if (this.isAboveExpected()) {
      return 100 - ((this.goal / this.actual) * 100);
    } else {
      return (this.actual / this.goal) * 100;
    }
  }

  getGoalNormalizeProgress() {
    if (this.isAboveExpected()) {
      return (this.goal / this.actual) * 100;
    } else {
      return 100;
    }
  }
}
