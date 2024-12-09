import {ApplicationRef, Component, inject, OnInit} from '@angular/core';
import {BookService} from '../../../../services/services/book.service';
import {BookResponse} from '../../../../services/models/book-response';
import {PageResponseBookResponse} from '../../../../services/models/page-response-book-response';
import {HttpClient} from '@angular/common/http';
import {first, Observable} from 'rxjs';

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss'
})
export class BookListComponent implements OnInit{

  bookResponse: PageResponseBookResponse={};
  private page: number = 0;
  private size: number = 10;
  errorMessage:Array<string> =[];

  constructor(private httpClient:HttpClient) {

  }

  ngOnInit(): void {
    this.findAllBooks();
  }

  private findAllBooks() {
    this.findAllBooksOwnedByConnectedUser().subscribe({
      next: (response) => {
        this.bookResponse = response;
        console.log(JSON.stringify(response));
      },
      error: (err) => {
        this.errorMessage.push(err);
      }
    })
  }


  //service calls the authenticate the user
  protected findAllBooksOwnedByConnectedUser():Observable<PageResponseBookResponse>{
    return this.httpClient.get("http://localhost:8080/api/v1/books/owned-by-connected-user", {params: {page: this.page, size: this.size}});
    // return this.httpClient.post("http://localhost:8080/api/v1/auth/authenticate", this.authRequest);
  }
}
