import {Component, inject, OnInit} from '@angular/core';
import {PageResponseBookResponse} from '../../../../services/models/page-response-book-response';
import {catchError, Observable, throwError} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {UriConstants} from '../../../../constants/uri-constants';
import {CreateBookComponent} from '../../components/create-book/create-book.component';
import {MatDialog} from '@angular/material/dialog';

@Component({
  selector: 'app-my-books',
  templateUrl: './my-books.component.html',
  styleUrl: './my-books.component.scss'
})
export class MyBooksComponent implements OnInit{

  bookResponse: PageResponseBookResponse={};
  page: number = 0;
  size: number = 10;
  errorMessage:Array<string> =[];

  totalPages: number = 1;

  constructor(private httpClient:HttpClient) {

  }
  readonly dialog = inject(MatDialog);


  ngOnInit() {
    this.findAllBooksOwnedByUser();
  }


  onPageChange(event: any){
    this.page = event.pageIndex;
    this.size = event.pageSize;
    this.findAllBooksOwnedByUser();
  }

  private findAllBooksOwnedByUser() {
    this.findAllBooksOwnedByConnectedUser().subscribe({
      next: (response) => {
        this.bookResponse = response;
        console.log(JSON.stringify(this.bookResponse));
        if(this.bookResponse.totalPages && this.bookResponse.size){
          this.totalPages = this.bookResponse.totalPages;
        }
      },
      error: (err) => {
        this.errorMessage.push(err);
      }
    });
  }


  private findAllBooksOwnedByConnectedUser():Observable<PageResponseBookResponse>{
    return this.httpClient.get(UriConstants.findAllBooksOwnedByConnectedUserUri(), {
      params: {page: this.page, size: this.size}
    }).pipe(
      catchError((error) => {
        this.errorMessage.push("failed to fetch books owned by user");
        console.log("failed to fetch books owned by user: ",error);
        return throwError(() => new Error("failed to fetch books owned by user"));
      })
    )
  }

  openDialog() {
    const dialogRef =
      this.dialog.open(CreateBookComponent, {
      width: '50rem',
        height:'40rem'
      });

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }

}
