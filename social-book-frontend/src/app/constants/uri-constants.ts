export class UriConstants{

  private static readonly BASE_URL = 'http://localhost:8080';
  private static readonly API_VERSION = '/api/v1';
  private static readonly authenticate = '/auth/authenticate';
  private static readonly register = '/auth/register';
  private static readonly books = '/books';
  private static readonly feedback = '/feedback';


  static authenticateUri(): string{
    return this.BASE_URL + this.API_VERSION + this.authenticate;
  }

  static registerUri(): string{
    return this.BASE_URL + this.API_VERSION + this.register;
  }

  static findAllBooksNotOwnedByConnectedUserUri(): string{
    return this.BASE_URL + this.API_VERSION + this.books + '/not-owned-by-connected-user';
  }

  static findAllBooksOwnedByConnectedUserUri(): string{
    return this.BASE_URL + this.API_VERSION + this.books + '/owned-by-connected-user';
  }

  static findAllBooksUri(): string{
    return this.BASE_URL + this.API_VERSION + this.books;
  }

  static findAllFeedBacksByBookIdUri(): string{
    return this.BASE_URL + this.API_VERSION + this.feedback + `/book`;
  }

  static createFeedback(): string{
    return this.BASE_URL + this.API_VERSION + this.feedback;
  }

  static borrowBookUri(): string{
    return this.BASE_URL + this.API_VERSION + this.books + '/borrow';
  }

}
