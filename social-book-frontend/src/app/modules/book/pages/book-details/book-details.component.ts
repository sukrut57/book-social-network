import {Component, OnInit} from '@angular/core';
import {BookResponse} from '../../../../services/models/book-response';
import {HttpClient} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {UriConstants} from '../../../../constants/uri-constants';
import {ActivatedRoute} from '@angular/router';
import {PageResponseFeedbackResponse} from '../../../../services/models/page-response-feedback-response';
import {FeedbackResponse} from '../../../../services/models/feedback-response';

@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrl: './book-details.component.scss'
})
export class BookDetailsComponent implements OnInit{
  private bookResponse: BookResponse | undefined;

  private feedBackResponse: Array<FeedbackResponse> = [];

  errorMessage:Array<string> =[];

  review: string ='';
  rateBook: number = 1;

  get book(){
    return this.bookResponse;
  }

  get feedbacks(){
    return this.feedBackResponse;
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

          this.getAllFeedBacksByBookId(params['id']).subscribe({
            next: (response) => {
              if(response.content){
                this.feedBackResponse = response.content;
              }
            },
            error: (err) => {
              this.errorMessage.push(err);
            }
          });
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

  getAllFeedBacksByBookId(bookId: number):Observable<PageResponseFeedbackResponse> {
    return this.httpClient.get<PageResponseFeedbackResponse>(UriConstants.findAllFeedBacksByBookIdUri() + `/${bookId}`).pipe(
      catchError((error) => {
        this.errorMessage.push("failed to fetch feedbacks by bookId " + bookId);
        console.log("failed to fetch feedbacks : ", error);
        return throwError(() => new Error("failed to fetch feedbacks by bookId: " + bookId));
      }));
  }

  writeReview():Observable<number>{
    return this.httpClient.post<number>(UriConstants.createFeedback(),{
      comment: this.review,
      bookId: this.bookResponse?.id,
      note: this.rateBook
    });
  }

  SubmitReview() {
    this.writeReview().subscribe({
      next: (response) => {
        console.log("review added successfully: ", response);
      },
      error: (err) => {
        this.errorMessage.push(err);
      }
    });
    window.location.reload();
  }

}
