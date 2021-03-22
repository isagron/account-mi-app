import {AfterViewInit, ChangeDetectorRef, Component, ElementRef, EventEmitter, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {NgForm, NgModel} from '@angular/forms';
import {ExpenseService} from '../../services/expense.service';
import {Expense} from '../../models/expense.model';
import {Observable, Subscription} from 'rxjs';
import {map, startWith} from 'rxjs/operators';


@Component({
  selector: 'app-new-expense-item',
  templateUrl: './new-expense-item.component.html',
  styleUrls: ['./new-expense-item.component.css']
})
export class NewExpenseItemComponent implements OnInit, AfterViewInit, OnDestroy {

  @ViewChild('f') addNewExpenseForm: NgForm;

  @ViewChild('amount')
  amount: NgModel;

  @ViewChild('category')
  categoryControl: NgModel;

  @ViewChild('store')
  storeControl: NgModel;

  @ViewChild('date')
  dateModel: NgbDateStruct;

  @ViewChild('rootInput')
  rootInput: ElementRef;

  selectToday: NgModel;

  dateKeepSelection: NgModel;

  categoryKeepSelection: NgModel;

  datePlaceholder = 'yyyy-mm-dd';

  categoryOptions: string[] = [];
  categoryFilteredOptions: Observable<string[]>;
  expenseCategoriesSubscription: Subscription;

  private selectedDate: Date;

  storeOptions: string[] = [];
  storeFilterOptions: Observable<string[]>;
  storesSubscription: Subscription;


  constructor(private expenseService: ExpenseService, private cdr: ChangeDetectorRef) {
  }

  ngOnInit(): void {
    this.expenseCategoriesSubscription = this.expenseService.expenseCategoriesSubject
      .subscribe(expenseCategories => {
        if (expenseCategories == null) {
          this.categoryOptions = [];
        } else {
          this.categoryOptions = expenseCategories;
        }
      });

    this.storesSubscription = this.expenseService.storesSubject.subscribe(stores => {
      if (stores != null) {
        this.storeOptions = stores;
      } else {
        this.storeOptions = [];
      }
    });
  }

  ngAfterViewInit() {
    this.categoryFilteredOptions = this.categoryControl.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value, this.categoryOptions))
    );

    this.storeFilterOptions = this.storeControl.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value, this.storeOptions))
    );
    this.cdr.detectChanges();
  }

  ngOnDestroy() {
    this.storesSubscription.unsubscribe();
    this.expenseCategoriesSubscription.unsubscribe();
  }

  onSubmit(addNewExpenseForm: NgForm) {
    if (addNewExpenseForm.valid) {
      const date = this.selectToday ?
          new Date() :
          new Date(addNewExpenseForm.value.date.year, addNewExpenseForm.value.date.month - 1, addNewExpenseForm.value.date.day);
      const offsetInHours = date.getTimezoneOffset() / -60;
      date.setHours(offsetInHours);
      const expense = new Expense(addNewExpenseForm.value.category, addNewExpenseForm.value.store, addNewExpenseForm.value.amount, date);
      this.expenseService.addNewExpense(expense);
      if (!this.dateKeepSelection) {
        this.addNewExpenseForm.controls['date'].reset();
      }
      if (!this.categoryKeepSelection) {
        this.addNewExpenseForm.controls['category'].reset();
      }
      this.addNewExpenseForm.controls['store'].reset();
      this.addNewExpenseForm.controls['amount'].reset();
      this.rootInput.nativeElement.focus();
    }
  }

  private _filter(value: string, options: string[]): string[] {
    console.log(value);
    const filterValue = value == null ? '' : value.toLowerCase();
    return options.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
  }


  selectTodayDate() {
    console.log(this.selectToday);
    if (this.selectToday) {
      this.datePlaceholder = new Date().toISOString().slice(0, 10);
    } else {
      this.datePlaceholder = 'yyyy-mm-dd';
    }


  }
}
