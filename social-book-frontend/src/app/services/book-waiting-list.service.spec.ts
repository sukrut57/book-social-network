import { TestBed } from '@angular/core/testing';

import { BookWaitingListService } from './book-waiting-list.service';

describe('BookWaitingListService', () => {
  let service: BookWaitingListService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BookWaitingListService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
