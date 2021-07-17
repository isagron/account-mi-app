import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {BehaviorSubject, Observable, throwError} from 'rxjs';
import {Router} from '@angular/router';
import {AppUser, AuthUser, RefreshTokenResponseDto} from '../models/user.model';
import {catchError, exhaustMap, map, switchMap, take} from 'rxjs/operators';
import {environment} from '../../environments/environment';

export interface AuthResponseData {
  kind: string;
  idToken: string;
  email: string;
  refreshToken: string;
  expiresIn: string;
  localId: string;
  registered?: boolean;
}

@Injectable({providedIn: 'root'})
export class AuthService {

  public userDetails = new BehaviorSubject<AppUser>(null);

  private tokenExpirationTimer: any;

  private authUrl: string = environment.baseUrl + environment.rootApi + '/auth';


  constructor(private http: HttpClient, private router: Router) {
  }

  autoLogin() {
    console.log('user auto login');

    const userFromLocalStorage: {
      email: string,
      id: string,
      expirationTime: number,
      refreshToken: string,
      _token: string,
      tokenExperationDate: string
    } = JSON.parse(localStorage.getItem('user'));
    if (!userFromLocalStorage) {
      return;
    }
    const appUser = new AppUser(userFromLocalStorage.email, userFromLocalStorage.id, +userFromLocalStorage.expirationTime,
      userFromLocalStorage.refreshToken,
      userFromLocalStorage._token, new Date(userFromLocalStorage.tokenExperationDate));
    const expirationDuration = appUser.tokenExperationDate.getTime() - new Date().getTime();
    if (expirationDuration > 0) {
      this.autoLogout(expirationDuration);
      this.userDetails.next(appUser);
      this.router.navigate(['/home']);
    }
  }


  public signup(email: string, password: string, confirmPassword: string): Observable<AppUser> {
    // Promise<AppUser> {
    return this.http.post<AuthUser>(this.authUrl + '/signup', {
      email,
      password,
      confirmPassword
    }).pipe(
      catchError(error => this.handleError(error)),
      map(authUser => {
        const appUser = this.convertAuthUserToAppUser(authUser);
        localStorage.setItem('user', JSON.stringify(appUser));
        this.autoLogout(appUser.expirationTime * 1000);
        this.userDetails.next(appUser);
        return appUser;
      }));
    // return this.firebaseAuth.createUserWithEmailAndPassword(email, password)
    //   .then(userCredential => {
    //     console.log('user sign in');
    //     const appUser = this.convertFirebaseUserToAppUser(userCredential.user);
    //     localStorage.setItem('user', JSON.stringify(appUser));
    //     this.autoLogout(appUser.expirationTime);
    //     this.userDetails.next(appUser);
    //     return appUser;
    //   });
  }

  public signin(email: string, password: string): Observable<AppUser> {
    return this.http.post<AuthUser>(this.authUrl + '/signin', {
      email,
      password
    }).pipe(
      catchError(error => this.handleError(error)),
      map(authUser => {
        const appUser = this.convertAuthUserToAppUser(authUser);
        localStorage.setItem('user', JSON.stringify(appUser));
        this.autoLogout(appUser.expirationTime * 1000);
        this.userDetails.next(appUser);
        return appUser;
      }));
    // return this.firebaseAuth.signInWithEmailAndPassword(email, password)
    //   .then(userCredential => {
    //     console.log('user sign in');
    //     const appUser = this.convertFirebaseUserToAppUser(userCredential.user);
    //     localStorage.setItem('user', JSON.stringify(appUser));
    //     this.autoLogout(appUser.expirationTime);
    //     this.userDetails.next(appUser);
    //     return appUser;
    //   });
  }


  public async logout() {
    await this.http.post(this.authUrl + '/signout', {});
    // notify user logout - header change the state
    this.userDetails.next(null);

    // navigate to login page
    this.router.navigate(['/']);
    localStorage.removeItem('user');
    if (this.tokenExpirationTimer) {
      clearTimeout(this.tokenExpirationTimer);
    }
    this.tokenExpirationTimer = null;

  }

  autoLogout(expirationDuration: number) {
    this.tokenExpirationTimer = setTimeout(() => {
      this.logout();
    }, expirationDuration);
  }

  public convertAuthUserToAppUser(authUser: AuthUser): AppUser {
    return new AppUser(authUser.email,
      authUser.userId,
      authUser.expiresIn,
      authUser.refreshToken,
      authUser.token,
      authUser.expirationTime);
  }


  refreshAccessToken(): Observable<AppUser> {
    return this.userDetails.pipe(
      take(1),
      exhaustMap(appUser => {
        return this.http.post<RefreshTokenResponseDto>(
          this.authUrl + '/refresh-token',
          {
            token: appUser.refreshToken
          })
          .pipe(
            map(refreshTokenResponse => {
              const expirationDate = new Date(new Date().getTime() + (refreshTokenResponse.expiresIn * 1000));
              const refreshUser = new AppUser(appUser.email,
                appUser.id,
                refreshTokenResponse.expiresIn,
                refreshTokenResponse.refreshToken,
                refreshTokenResponse.token,
                expirationDate);
              localStorage.setItem('user', JSON.stringify(appUser));
              this.userDetails.next(refreshUser);
              return refreshUser;
            })
          );
      })
    );
  }

  private handleError(errorRes: HttpErrorResponse) {
    let errorMessage = 'An unkown error occurred!';
    if (errorRes.error && errorRes.error.error) {
      switch (errorRes.error.error.message) {
        case 'EMAIL_EXISTS':
          errorMessage = 'THis email exist already';
          break;
        case 'INVALID_PASSWORD':
          errorMessage = 'Email or password not correct';
      }
    }
    return throwError(errorMessage);
  }
}
