import {Component, OnInit} from '@angular/core';
import {PageResponseBookResponse} from '../../../../services/models/page-response-book-response';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss'
})
export class BookListComponent implements OnInit{

  bookResponse: PageResponseBookResponse={};
  page: number = 0;
  size: number = 10;
  errorMessage:Array<string> =[];

  totalPages: number = 1;

  constructor(private httpClient:HttpClient) {

  }

  ngOnInit(): void {
    this.findAllBooks();
  }

  onPageChange(event: any){
    this.page = event.pageIndex;
    this.size = event.pageSize;
    this.findAllBooks();
  }

  private findAllBooks() {
    this.findAllBooksOwnedByConnectedUser().subscribe({
      next: (response) => {
        this.bookResponse = response;
        console.log(JSON.stringify(response));
        if(this.bookResponse.totalPages && this.bookResponse.size){
          this.totalPages = this.bookResponse.totalPages;
        }
      },
      error: (err) => {
        this.errorMessage.push(err);
      }
    });

  }

  //service calls the authenticate the user
  private findAllBooksOwnedByConnectedUser():Observable<PageResponseBookResponse>{
    return this.httpClient.get("http://localhost:8080/api/v1/books/owned-by-connected-user", {params: {page: this.page, size: this.size}});
    // return this.httpClient.post("http://localhost:8080/api/v1/auth/authenticate", this.authRequest);
  }

}
