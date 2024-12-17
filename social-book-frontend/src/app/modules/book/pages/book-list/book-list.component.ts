import {Component, OnInit} from '@angular/core';
import {PageResponseBookResponse} from '../../../../services/models/page-response-book-response';
import {HttpClient} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {UriConstants} from '../../../../constants/uri-constants';

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
  successMessage:string = '';

  totalPages: number = 1;

  constructor(private httpClient:HttpClient) {

  }

  ngOnInit(): void {
    this.findAllNotBooksOwnedByUser();
  }

  onPageChange(event: any){
    this.page = event.pageIndex;
    this.size = event.pageSize;
    this.findAllNotBooksOwnedByUser();
  }


  private findAllNotBooksOwnedByUser() {
    this.findAllBooksNotOwnedByConnectedUser().subscribe({
      next: (response) => {
        this.bookResponse = response;
        console.log(JSON.stringify(response));
        if(this.bookResponse.totalPages && this.bookResponse.size){
          this.totalPages = this.bookResponse.totalPages;
        }
      },
      error: (err) => {
        console.error('Error fetching books:', err);
      }
    });
  }


  //service calls the authenticate the user
  private findAllBooksNotOwnedByConnectedUser(): Observable<PageResponseBookResponse> {
    return this.httpClient.get<PageResponseBookResponse>(UriConstants.findAllBooksNotOwnedByConnectedUserUri(), {
      params: { page: this.page, size: this.size }
    }).pipe(
      catchError((error: any) => {
        this.errorMessage.push('Failed to fetch books not owned by the connected user.');
        return throwError(() => new Error('Error fetching books'));
      })
    );
  }

  handleError($event: Array<string>) {
    this.errorMessage = [];
    this.errorMessage.push(...$event);

  }

  closeAlert() {
    this.errorMessage = [];
    this.successMessage='';
  }

  handleSuccess($event: string) {
    this.successMessage = $event;
  }
}
