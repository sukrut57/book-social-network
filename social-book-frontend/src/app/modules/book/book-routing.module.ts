import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MainComponent} from './pages/main/main.component';
import {BookListComponent} from './pages/book-list/book-list.component';
import {MyBooksComponent} from './pages/my-books/my-books.component';
import {BookDetailsComponent} from './pages/book-details/book-details.component';

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
