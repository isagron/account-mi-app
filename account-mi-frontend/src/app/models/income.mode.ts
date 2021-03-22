import {Expense} from './expense.model';

export class Income {

  public id: string;
  public type: string;
  public amount: number;
  public date: Date;
  public consistent: boolean;
  public accountId: string;


  constructor(type: string, amount: number, date: Date, consistent: boolean) {
    this.type = type;
    this.amount = amount;
    this.date = date;
    this.consistent = consistent;
  }

}

export class IncomePage {

  constructor(public content: Income[], public pageNumber: number, public pageSize: number, public totalElements: number) {
  }
}
