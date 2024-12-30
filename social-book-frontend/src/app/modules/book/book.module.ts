import {NgModule} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';

import {BookRoutingModule} from './book-routing.module';
import {MainComponent} from './pages/main/main.component';
import {MenuComponent} from './components/menu/menu.component';
import {BookListComponent} from './pages/book-list/book-list.component';
import {BookCardComponent} from './components/book-card/book-card.component';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MyBooksComponent} from './pages/my-books/my-books.component';
import {BookDetailsComponent} from './pages/book-details/book-details.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {StarRatingComponent} from './components/star-rating/star-rating.component';
import {MatButton} from "@angular/material/button";
import {MatFormFieldModule, MatLabel} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import {MatOptionModule} from '@angular/material/core';
import {MatTooltipModule} from "@angular/material/tooltip";
import {NgbAlert} from '@ng-bootstrap/ng-bootstrap';
import { MyBorrowedBooksComponent } from './pages/my-borrowed-books/my-borrowed-books.component';
import { MyWaitingListComponent } from './pages/my-waiting-list/my-waiting-list.component';
import { CreateBookComponent } from './components/create-book/create-book.component';
import {MatDialogModule} from '@angular/material/dialog';
import {MatInput} from '@angular/material/input';
import {MatRadioButton, MatRadioGroup} from '@angular/material/radio';


@NgModule({
  declarations: [
    MainComponent,
    MenuComponent,
    BookListComponent,
    BookCardComponent,
    MyBooksComponent,
    BookDetailsComponent,
    StarRatingComponent,
    MyBorrowedBooksComponent,
    MyWaitingListComponent,
    CreateBookComponent,

  ],
  imports: [
    CommonModule,
    BookRoutingModule,
    NgOptimizedImage,
    MatPaginatorModule,
    MatButton,
    MatLabel,
    FormsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatOptionModule,
    MatTooltipModule,
    NgbAlert,
    MatDialogModule,
    ReactiveFormsModule,
    MatInput,
    MatRadioButton,
    MatRadioGroup
  ]
})
export class BookModule { }
