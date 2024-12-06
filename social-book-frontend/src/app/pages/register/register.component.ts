import { Component } from '@angular/core';
import {RegistrationRequest} from '../../services/models/registration-request';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../services/services/authentication.service';
import {response} from 'express';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  registerRequest : RegistrationRequest={
    dateOfBirth: '',
    email: '',
    firstName: '',
    lastName: '',
    password: ''
  }
  errorMessage: Array<string> =[];

  constructor(
    private router:Router,
    private authService: AuthenticationService) {
  }

  login() {
    this.router.navigate(['/login']).then(
      r => console.log('navigated back to login')
    ).catch(
      e => console.log('error navigating')
    );
  }

  register(){
    this.authService.register({
      body: this.registerRequest
    }).subscribe({
      next:(response) => {
        this.router.navigate(['/login']).then(
          r => console.log('navigate to login')
        ).catch(
          e => console.log('error navigating')
        );
      },
      error: (err) => {
        console.log(err)
        if(err.error.validationErrors){
          this.errorMessage = err.error.validationErrors;
        }
        else{
          this.errorMessage.push(err.error.businessErrorDescription);
          this.errorMessage.push(err.error.error);
        }
      }
    });
  }
}

