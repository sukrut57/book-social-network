export class UriConstants{

  private static readonly BASE_URL = 'http://localhost:8080';
  private static readonly API_VERSION = '/api/v1';
  private static readonly authenticate = '/auth/authenticate';
  private static readonly register = '/auth/register';

  static authenticateUri(): string{
    return this.BASE_URL + this.API_VERSION + this.authenticate;
  }

  static registerUri(): string{
    return this.BASE_URL + this.API_VERSION + this.register;
  }

}
