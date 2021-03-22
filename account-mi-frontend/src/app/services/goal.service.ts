import {GoalStatus} from '../models/statistics.model';
import {GoalInterval, GoalSetting} from '../models/setting-models.model';
import {Subject} from 'rxjs';

export class GoalService {

  public goalFulfillmentSubject = new Subject<GoalStatus>();

  getGoalStatFor(category: string, month: number, year: number): GoalStatus[] {
    // request form the server
    return [
      new GoalStatus(null, 'Food', 100, 10, 120),
      new GoalStatus(null, 'Food', 100, 10, 100)
    ];
  }

  getGoalsStats(): GoalStatus[] {
    return [
      new GoalStatus(null, 'Food', 100, 10, 120),
      new GoalStatus(null, 'Farm', 120, 20, 100)
    ];
  }
}
