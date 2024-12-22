import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MainComponent} from './pages/main/main.component';
import {BookListComponent} from './pages/book-list/book-list.component';
import {MyBooksComponent} from './pages/my-books/my-books.component';
import {BookDetailsComponent} from './pages/book-details/book-details.component';
import {MyBorrowedBooksComponent} from './pages/my-borrowed-books/my-borrowed-books.component';
import {MyWaitingListComponent} from './pages/my-waiting-list/my-waiting-list.component';

const routes: Routes = [
  {
    path: '',
    component: MainComponent,
    children:[

      {
        path: '',
        component:BookListComponent
      },
      {
        path: 'my-books',
        component:MyBooksComponent
      },

      {
        path: ':id/details',
        component:BookDetailsComponent
      },
      {
        path:'my-borrowed-books',
        component:MyBorrowedBooksComponent
      },
      {
        path:'my-waiting-list',
        component:MyWaitingListComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BookRoutingModule {

}
