import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {BookService} from '../../../../services/services/book.service';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-create-book',
  templateUrl: './create-book.component.html',
  styleUrl: './create-book.component.scss'
})
export class CreateBookComponent {
  createBookForm: FormGroup;
  private _errorMessage: Array<string> = [];
  private _successMessage:string = '';

  constructor(private _formBuilder: FormBuilder,
              private _bookService: BookService,
              private httpClient: HttpClient) {

    this.createBookForm = this._formBuilder.group({
      title: ['', Validators.required],
      synopsis:['', Validators.required],
      isbn:['', Validators.required],
      bookCover:[''],
      sharable:['true', Validators.required],

    });
  }

  onSubmit() {
    console.log(JSON.stringify(this.createBookForm.value));
  }

  //service call to borrow a book
  private saveBook() {
    this.httpClient.post(`http://localhost:8080/api/v1/books`, {}).subscribe({
      next: (response) => {
        console.log(response);
        this._successMessage = 'Book borrowed successfully';
     },
      error: (err) => {
        console.error(err);
        if(err.error.businessErrorDescription){
          this._errorMessage.push(err.error.error);
        }
      }
    });
  }
}
