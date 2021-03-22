import {GoalDto, GoalSetting} from './setting-models.model';

export class Account {

  constructor(public accountId: string,
              public userId: string,
              public accountBalance: number,
              public expenseCategories: string[],
              public incomeCategories: string[],
              public goals: GoalDto[]) {

  }

}

export class CreateAccountRequest {

  constructor(private userId: string,
              private accountBalance: number,
              private expenseCategories: string[],
              private incomeCategories: string[]) {
  }
}

export class AccountData {
  constructor(public activeAccountId: string) {
  }
}
