import { NgModule } from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';

import { BookRoutingModule } from './book-routing.module';
import { MainComponent } from './pages/main/main.component';
import { MenuComponent } from './components/menu/menu.component';
import { BookListComponent } from './pages/book-list/book-list.component';
import { BookCardComponent } from './components/book-card/book-card.component';
import {MatPaginatorModule} from '@angular/material/paginator';
import { MyBooksComponent } from './pages/my-books/my-books.component';
import { BookDetailsComponent } from './pages/book-details/book-details.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { StarRatingComponent } from './components/star-rating/star-rating.component';



@NgModule({
  declarations: [
    MainComponent,
    MenuComponent,
    BookListComponent,
    BookCardComponent,
    MyBooksComponent,
    BookDetailsComponent,
    StarRatingComponent,

  ],
  imports: [
    CommonModule,
    BookRoutingModule,
    NgOptimizedImage,
    MatPaginatorModule
  ]
})
export class BookModule { }
