
export class GoalStatus {

  constructor(public goalId: string,
              public category: string,
              public threshold: number,
              public currentStatus: number,
              public  avg: number) {
  }

}

export class CategoryPerMonthData {
  constructor(public category: string, public goal: number, public data: { label: string, data: number }[]) {
  }
}

export class CategoryExpenseForMonth {
  constructor(public category: string, public month: number, public year: number, public amount: number, public goal: number) {
  }
}
