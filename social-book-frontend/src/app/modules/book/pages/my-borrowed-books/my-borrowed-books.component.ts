import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {UriConstants} from '../../../../constants/uri-constants';
import {PageResponseBorrowedBookResponse} from '../../../../services/models/page-response-borrowed-book-response';
import {BorrowedBookResponse} from '../../../../services/models/borrowed-book-response';
import {BookResponse} from '../../../../services/models/book-response';
import {PageResponseBookResponse} from '../../../../services/models/page-response-book-response';

@Component({
  selector: 'app-my-borrowed-books',
  templateUrl: './my-borrowed-books.component.html',
  styleUrl: './my-borrowed-books.component.scss'
})
export class MyBorrowedBooksComponent implements OnInit {
  page: number = 0;
  size: number = 10;
  borrowedBookResponse: PageResponseBorrowedBookResponse={};

  currentBorrowedBooks: Array<BorrowedBookResponse> = [];
  currentBooksInfo: Array<BookResponse> = [];

  pastBorrowedBooks: Array<BorrowedBookResponse> = [];
  pastBooksInfo: Array<BookResponse> = [];


  errorMessage:Array<string> =[];


  totalPages: number = 1;

  constructor(private httpClient:HttpClient) {

  }
  ngOnInit(): void {
    this.fetchBorrowedBooks();
  }


  onPageChange(event: any){
    this.page = event.pageIndex;
    this.size = event.pageSize;
    this.fetchBorrowedBooks();
  }

  handleError($event: Array<string>) {
    this.errorMessage = [];
    this.errorMessage.push(...$event);

  }


  closeAlert() {
    this.errorMessage = [];
  }

  private fetchBorrowedBooks() {
    this.findAllBorrowedBooksBConnectedUser().subscribe({
      next: (response) => {
        this.borrowedBookResponse = response;
        this.filterCurrentBorrowedBooks();
        this.filterPastBorrowedBooks();
        console.log('!!!!!!!')
        console.log(JSON.stringify(this.currentBorrowedBooks));
        console.log('!!!!!!!')

        console.log(JSON.stringify(this.pastBorrowedBooks));
        // if(this.borrowedBookResponse.totalPages && this.borrowedBookResponse.size){
        //   this.totalPages = this.borrowedBookResponse.totalPages;
        // }
        this.retrieveAllBooksInfoForCurrentBorrowedBooks();
        this.retrieveAllBooksInfoForPastBorrowedBooks();

      },
      error: (err) => {
        console.error('Error fetching books:', err);
      },
    });

  }


  private findAllBorrowedBooksBConnectedUser(): Observable<PageResponseBorrowedBookResponse> {
    return this.httpClient.get<PageResponseBorrowedBookResponse>(UriConstants.findAllBorrowedBooksByConnectedUserUri(), {
      params: { page: this.page, size: this.size }
    }).pipe(
      catchError((error: any) => {
        this.errorMessage.push('Failed to fetch books borrowed by the connected user.');
        return throwError(() => new Error('Error fetching books'));
      })
    )
  }

  private filterPastBorrowedBooks(){
    if(this.borrowedBookResponse.content){
      this.pastBorrowedBooks = this.borrowedBookResponse.content.filter((borrowedBook) => borrowedBook.returned == true);
    }
  }

  private filterCurrentBorrowedBooks(){
    if(this.borrowedBookResponse.content){
      this.currentBorrowedBooks = this.borrowedBookResponse.content.filter((borrowedBook) => borrowedBook.returned == false);
    }
  }

  retrieveAllBooksInfoForCurrentBorrowedBooks(){
    if(this.currentBorrowedBooks.length>0){
      for(let borrowedBook of this.currentBorrowedBooks){
       this.retrieveCurrentBookInfo(borrowedBook.bookId);

      }
    }
  }

  retrieveAllBooksInfoForPastBorrowedBooks(){
    if(this.pastBorrowedBooks.length>0){
      for(let borrowedBook of this.pastBorrowedBooks){
        this.retrievePastBookInfo(borrowedBook.bookId);
      }
    }
  }

  private retrievePastBookInfo(bookId: number | undefined) {
    if(bookId){
      this.getBooksDetailsByBookId(bookId).subscribe({
        next: (response) => {
          this.pastBooksInfo.push(response);
        },
        error: (err) => {
          console.error('Error fetching books:', err);
          throwError(() => new Error('Error fetching books'));
        }
      });
    }
  }

  private retrieveCurrentBookInfo(bookId: number | undefined) {
    if(bookId){
      this.getBooksDetailsByBookId(bookId).subscribe({
        next: (response) => {
          this.currentBooksInfo.push(response);
        },
        error: (err) => {
          console.error('Error fetching books:', err);
          throwError(() => new Error('Error fetching books'));
        }
      });
    }
  }

  //service calls the authenticate the user
  getBooksDetailsByBookId(bookId: number):Observable<BookResponse>{
    return this.httpClient.get<BookResponse>(UriConstants.findAllBooksUri()+`/${bookId}`).pipe(
      catchError((error) => {
        this.errorMessage.push("failed to fetch books by bookId "+bookId);
        console.log("failed to fetch books : ",error);
        return throwError(() => new Error("failed to fetch books by bookId: " + bookId));
      }))
  }
}
