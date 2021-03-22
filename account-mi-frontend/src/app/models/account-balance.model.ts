export class AccountBalanceModel {
  incomes: BalanceParameterShortSummary;
  outcomes: BalanceParameterShortSummary;
  balance: BalanceParameterShortSummary;

  constructor(incomes: BalanceParameterShortSummary, outs: BalanceParameterShortSummary, balance: BalanceParameterShortSummary) {
    this.incomes = incomes;
    this.outcomes = outs;
    this.balance = balance;
  }
}

export class BalanceParameterShortSummary {
  constructor(public currentMonth: number, public previousMonth: number, public totalThisYear: number) {
  }
}
