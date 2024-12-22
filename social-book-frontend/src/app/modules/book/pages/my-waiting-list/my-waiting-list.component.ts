import {Component, OnInit} from '@angular/core';
import {BookWaitingListService} from '../../../../services/book-waiting-list.service';
import {BookResponse} from '../../../../services/models/book-response';

@Component({
  selector: 'app-my-waiting-list',
  templateUrl: './my-waiting-list.component.html',
  styleUrl: './my-waiting-list.component.scss'
})
export class MyWaitingListComponent implements OnInit {

  bookItems: BookResponse[] = [];
  errorMessage: Array<string>=[];


  constructor(private bookWaitingListService: BookWaitingListService) { }

  ngOnInit(): void {
        this.getBookItems();
  }

  getBookItems() {
    return this.bookWaitingListService.publishWaitingListItem.subscribe({
      next: (response) => {
        this.bookItems = response;
        console.log(`retrieving my waiting list: `+JSON.stringify(this.bookItems));
      }
    });
  }

  handleError($event: Array<string>) {
    this.errorMessage = [];
    this.errorMessage.push(...$event);

  }
  closeAlert() {
    this.errorMessage = [];
  }

}
