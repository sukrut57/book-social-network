import {Component, Input} from '@angular/core';
import {BookResponse} from '../../../../services/models/book-response';

@Component({
  selector: 'app-book-card',
  templateUrl: './book-card.component.html',
  styleUrl: './book-card.component.scss'
})
export class BookCardComponent {

  private _book:BookResponse = {};

  get book(): BookResponse{
    return this._book;
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

}
