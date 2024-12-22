import {Component, EventEmitter, Input, Output} from '@angular/core';
import {BookResponse} from '../../../../services/models/book-response';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {UriConstants} from '../../../../constants/uri-constants';
import {BookWaitingListService} from '../../../../services/book-waiting-list.service';

@Component({
  selector: 'app-book-card',
  templateUrl: './book-card.component.html',
  styleUrl: './book-card.component.scss'
})
export class BookCardComponent {

  private _book:BookResponse = {};
  private _manage:boolean=false;
  private _errorMessage: Array<string> = [];
  private _successMessage:string = '';
  @Output() error: EventEmitter<Array<string>> = new EventEmitter<Array<string>>();
  @Output() success: EventEmitter<string> = new EventEmitter<string>();


  constructor(private router: Router,
              private httpClient: HttpClient,
              private bookWaitingListService: BookWaitingListService) {
  }

  get book(): BookResponse{
    return this._book;
  }

  get manage(): boolean{
    return this._manage;
  }

  @Input()
  set manage(value: boolean){
    this._manage = value;
  }

  @Input()
  set book(value: BookResponse){
    this._book = value;
  }


   publishErrorMessage(){
    if(this._errorMessage.length > 0){
      this.error.emit(this._errorMessage);
    }
  }

  publishSuccessMessage(){
    if(this._successMessage){
      this.success.emit(this._successMessage)
    }
  }

  processBytesImage(): string {
    if(this.book.bookCover){
      return `data:image/jpeg;base64,${this.book.bookCover}`;
    }
    return 'no-book-cover.png';
  }


  onShowDetails(book: BookResponse) {
    console.log(book.synopsis);
    this.router.navigate([`/books/${book.id}/details`]).then(
      () => {
        console.log('Navigation done');
      }
    ).catch(
      (reason) => {
        console.error('Navigation failed', reason);
      }
    );
  }


  onBorrow() {
    if(this.book.id) this.borrowBook(this.book.id);

  }

  onAddToWaitingList() {
    console.log('Adding to waiting list');
    this.bookWaitingListService.addToList(this.book);
  }

  onEdit() {

  }

  onShare() {

  }

  onDelete() {

  }

  //service call to borrow a book
  private borrowBook(bookId: number) {
    this.httpClient.post(UriConstants.borrowBookUri()+`/${bookId}`, {}).subscribe({
      next: (response) => {
        console.log(response);
        this._successMessage = 'Book borrowed successfully';
        this.publishSuccessMessage();
      },
      error: (err) => {
        console.error(err);
        if(err.error.businessErrorDescription){
          this._errorMessage.push(err.error.error);
        }
        this.publishErrorMessage();
      }
    });
  }


}
