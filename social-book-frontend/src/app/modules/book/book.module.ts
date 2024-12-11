import { NgModule } from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';

import { BookRoutingModule } from './book-routing.module';
import { MainComponent } from './pages/main/main.component';
import { MenuComponent } from './components/menu/menu.component';
import { BookListComponent } from './pages/book-list/book-list.component';
import { BookCardComponent } from './components/book-card/book-card.component';
import {MatPaginatorModule} from '@angular/material/paginator';



@NgModule({
  declarations: [
    MainComponent,
    MenuComponent,
    BookListComponent,
    BookCardComponent
  ],
  imports: [
    CommonModule,
    BookRoutingModule,
    NgOptimizedImage,
    MatPaginatorModule
  ]
})
export class BookModule { }
