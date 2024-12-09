import {Component, numberAttribute} from '@angular/core';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../services/services/authentication.service';

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrl: './activate-account.component.scss'
})
export class ActivateAccountComponent {
  message: string = '';
  isOkay: boolean = true;
  submitted: boolean = false;

  constructor(private router: Router, private authService: AuthenticationService) {

  }


  onCodeCompleted(token: string) {
    this.confirmedAccount(token);

  }

  onLogin() {
    this.router.navigate(['/login']).then(r => {
      console.log('navigated to login');
    }).catch(e => {
      console.log('error navigating to login');
    });
  }

  private confirmedAccount(token: string) {
    this.authService.activateAccount({
      token
    }).subscribe({
      next: () => {
        this.isOkay = true;
        this.submitted = true;
        this.message = 'Account activated successfully';
      },
      error: (err) => {
        this.isOkay = false;
        this.submitted = true;
        this.message = 'Token is invalid or expired';
      }

    });
  }

  onTryAgain() {
    window.location.reload();
  }
}
