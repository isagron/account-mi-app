import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject} from 'rxjs';

@Injectable()
export class UnsecuredUserService {

  public userSubject = new BehaviorSubject<string>(null);

  constructor(private http: HttpClient) {
  }

  public setUser(name: string) {
    this.userSubject.next(name);
  }

}
