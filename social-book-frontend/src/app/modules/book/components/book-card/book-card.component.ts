import {Component, Input} from '@angular/core';
import {BookResponse} from '../../../../services/models/book-response';
import {Router} from '@angular/router';

@Component({
  selector: 'app-book-card',
  templateUrl: './book-card.component.html',
  styleUrl: './book-card.component.scss'
})
export class BookCardComponent {

  private _book:BookResponse = {};
  private _manage:boolean=false;

  constructor(private router: Router) {
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

  }

  onAddToWaitingList() {

  }

  onEdit() {

  }

  onShare() {

  }

  onDelete() {

  }
}
