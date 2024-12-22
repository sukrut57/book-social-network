import { Injectable } from '@angular/core';
import {BookResponse} from './models/book-response';
import {BehaviorSubject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BookWaitingListService {

  private bookItems: BookResponse[]= [];
  publishWaitingListItem: BehaviorSubject<BookResponse[]> = new BehaviorSubject<BookResponse[]>([]);

  constructor() { }

  addToList(book: BookResponse){
    if(this.findBookIem(book) !==undefined){
      return;
    }
    this.bookItems.push(book);
    this.publishWaitingListItem.next(this.bookItems);
  }

  private findBookIem(bookItem:BookResponse){
    return this.bookItems.find(item => item.id === bookItem.id);
  }
}
