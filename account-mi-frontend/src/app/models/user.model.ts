export class AppUser {
  constructor(
    public email: string,
    public id: string,
    public expirationTime: number,
    public refreshToken: string,
    private _token: string,
    public tokenExperationDate: Date) {
  }

  get token() {
    if (!this.tokenExperationDate || new Date() > this.tokenExperationDate) {
      return null;
    }
    return this._token;
  }

}

export class RefreshTokenResponseDto {
  constructor(
    public expiresIn: number,
    public refreshToken: string,
    public token: string,
    public userId: string,
  ) {
  }
}

export class AuthUser {
  constructor(
    public userId: string,
    public email: string,
    public token: string,
    public refreshToken: string,
    public expiresIn: number,
    public expirationTime: Date
  ) {
  }
}

export enum LoginMode {
  LOGIN,
  LOGOUT,
  SIGNUP
}
