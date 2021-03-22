export class GoalSetting {
  constructor(public goalId: string,
              public category: string,
              public amount: number,
              public interval: GoalInterval,
              public mode: GoalSettingMode) {
  }

  isEditable() {
    return this.mode === GoalSettingMode.EDIT;
  }
}

export class GoalDto {
  constructor(public goalId: string,
              public category: string,
              public amount: number,
              public interval: GoalInterval) {
  }
}

export enum GoalInterval {
  MONTH = 'Month',
  YEAR = 'Year',
}

export enum GoalSettingMode {
  SAVE,
  EDIT,
}
