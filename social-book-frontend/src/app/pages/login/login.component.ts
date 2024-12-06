import {Component, inject} from '@angular/core';
import {AuthenticationRequest} from '../../services/models/authentication-request';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../services/services/authentication.service';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AuthenticationResponse} from '../../services/models/authentication-response';
import {TokenService} from '../../services/token/token.service';
import {UriConstants} from '../../constants/uri-constants';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  authRequest : AuthenticationRequest = { email: '', password: ''};
  errorMessage:Array<string> = [];

  httpClient= inject(HttpClient);

 constructor(
   private authService: AuthenticationService,
   private router: Router,
   private tokenService: TokenService) {
 }


  login() {
   this.errorMessage = [];
   this.authenticate(this.authRequest).subscribe({

     next: (response) => {

       //save the JWT token to localStorage
       this.tokenService.token = response.token as string;

       //navigate to books page
        this.router.navigate(['/books'])
          .then(r => {
            console.log('navigated to books');
        })
          .catch(e => {
            console.log('error navigating to books');
        })
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
   })
  }

  register(){

    this.router.navigate(['/register'])
      .then(r => {
        console.log('navigated to register');
    })
      .catch(e => {
        console.log('error navigating to register');
    })
  }

  //service calls the authenticate the user
  protected authenticate(authReq: AuthenticationRequest):Observable<AuthenticationResponse>{
    return this.httpClient.post(UriConstants.authenticateUri(), this.authRequest);
    // return this.httpClient.post("http://localhost:8080/api/v1/auth/authenticate", this.authRequest);
  }
}
