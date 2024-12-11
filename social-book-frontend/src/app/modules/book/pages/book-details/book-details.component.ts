import {Component, OnInit} from '@angular/core';
import {BookResponse} from '../../../../services/models/book-response';
import {HttpClient} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {UriConstants} from '../../../../constants/uri-constants';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrl: './book-details.component.scss'
})
export class BookDetailsComponent implements OnInit{
  private bookResponse: BookResponse | undefined;
  errorMessage:Array<string> =[];

  get book(){
    return this.bookResponse;
  }

  constructor(private httpClient:HttpClient,
              private route:ActivatedRoute) {
  }

  processBytesImage(): string {
    if(this.bookResponse?.bookCover){
      return `data:image/jpeg;base64,${this.bookResponse.bookCover}`;
    }
    return 'no-book-cover.png';
  }


  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.getBookById(params['id']).subscribe({
        next: (response) => {
          this.bookResponse = response;
          console.log(JSON.stringify(this.bookResponse));
        },
        error: (err) => {
          this.errorMessage.push(err);
        }
      });
  });
  }


  getBookById(bookId: number):Observable<BookResponse>{
    return this.httpClient.get<BookResponse>(UriConstants.findAllBooksUri()+`/${bookId}`).pipe(
      catchError((error) => {
        this.errorMessage.push("failed to fetch books by bookId "+bookId);
        console.log("failed to fetch books : ",error);
        return throwError(() => new Error("failed to fetch books by bookId: " + bookId));
      }))
  }




}
