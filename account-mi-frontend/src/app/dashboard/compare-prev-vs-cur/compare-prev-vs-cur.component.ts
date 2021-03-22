import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-compare-prev-vs-cur',
  templateUrl: './compare-prev-vs-cur.component.html',
  styleUrls: ['./compare-prev-vs-cur.component.css']
})
export class ComparePrevVsCurComponent implements OnInit {

  @Input() title: string;
  @Input() current: number;
  @Input() prev: number;
  @Input() total: number;

  constructor() { }

  ngOnInit(): void {
  }

}
