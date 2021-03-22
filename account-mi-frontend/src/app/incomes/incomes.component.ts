import {Component, OnInit} from '@angular/core';
import {IncomesService} from '../services/incomes.service';
import {PaginationService} from '../services/pagination.service';


@Component({
  selector: 'app-incomes',
  templateUrl: './incomes.component.html',
  styleUrls: ['./incomes.component.css'],
  providers: [PaginationService]
})
export class IncomesComponent implements OnInit {



  constructor(private incomeService: IncomesService, private paginationService: PaginationService) {
  }

  ngOnInit(): void {
    // set create new income form



  }







}
