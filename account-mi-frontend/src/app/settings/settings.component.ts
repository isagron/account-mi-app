import {Component, OnDestroy, OnInit} from '@angular/core';
import {AccountService} from '../services/account.service';
import {Subscription} from 'rxjs';
import {IncomesService} from '../services/incomes.service';
import {ExpenseService} from '../services/expense.service';
import {ModalDismissReasons, NgbModal, NgbModalOptions} from '@ng-bootstrap/ng-bootstrap';
import {ModalAlertComponent} from '../shared/components/modal-alert/modal-alert.component';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit, OnDestroy {

  expenseCategories: string[] = [];
  incomeCategories: string[] = [];
  private accountSubscription: Subscription;
  isAccountSet = false;
  private incomeTypeSubscription: Subscription;
  private expenseCategoriesSubscription: Subscription;

  categoryToDelete: string = null;

  modalOptions: NgbModalOptions;


  constructor(private accountService: AccountService,
              private incomeService: IncomesService,
              private expenseService: ExpenseService,
              private modalService: NgbModal) {

    this.modalOptions = {
      backdrop: 'static',
      backdropClass: 'customBackdrop'
    };

  }

  openModelDeleteExpenseCategory(category: string) {
    const modalRef = this.modalService.open(ModalAlertComponent);
    modalRef.componentInstance.modalTitle = 'Delete Expense category';
    modalRef.componentInstance.modalContent = 'Deleting category will delete all related resource, are you sure?';
    modalRef.result.then((result) => {
      console.log(result);
      if (result === ModalAlertComponent.MODAL_APPROVE_CLOSE) {
        this.deleteExpenseCategory(category);
      }
    });

  }

  ngOnInit(): void {

    this.accountSubscription = this.accountService.accountSubject.subscribe(account => {
      if (account === null) {
        this.isAccountSet = false;
      } else {
        this.isAccountSet = true;
      }
    });

    this.incomeTypeSubscription = this.incomeService.incomeTypesSubject
      .subscribe(incomesType => {
        if (incomesType === null) {
          this.incomeCategories = [];
        } else {
          this.incomeCategories = incomesType.slice();
        }
      });

    this.expenseCategoriesSubscription = this.expenseService.expenseCategoriesSubject
      .subscribe(expenseCategories => {
        if (expenseCategories === null) {
          this.expenseCategories = [];
        } else {
          this.expenseCategories = expenseCategories.slice();
        }
      });

  }

  ngOnDestroy() {
    this.accountSubscription.unsubscribe();
    this.incomeTypeSubscription.unsubscribe();
    this.expenseCategoriesSubscription.unsubscribe();
  }

  addNewExpenseCategory(category: string) {
    this.accountService.addExpenseCategory(category);

  }

  deleteExpenseCategory(category: string) {
    this.accountService.deleteExpenseCategory(category);
  }

  addNewIncomeCategory(category: string) {
    this.accountService.addIncomeCategory(category);
  }

  deleteIncomeCategory(category: string) {
    this.accountService.deleteIncomeCategory(category);
    this.categoryToDelete = null;
  }

  setAccount() {
    this.accountService.init('/home');
  }

  cancelExpenseCategoryDelete() {
    this.categoryToDelete = null;
  }
}
