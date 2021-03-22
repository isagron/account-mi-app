export class Expense {

  public id: string;
  public accountId;
  public category: string;
  public store: string;
  public amount: number;
  public date: Date;

  constructor(category: string, store: string, amount: number, date: Date) {
    this.category = category;
    this.store = store;
    this.amount = amount;
    this.date = date;
  }

  public set setId(id: string) {
    this.id = id;
  }

  public set setAccountId(accountId: string) {
    this.accountId = accountId;
  }

}

export class ExpensePage {

  constructor(public content: Expense[], public pageNumber: number, public pageSize: number, public totalElements: number) {
  }
}
