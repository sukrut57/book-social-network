import { Component } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent {
  constructor(private router:Router) {
  }

  logout() {
    localStorage.removeItem('token');
    window.location.reload();
    this.router.navigate(['/login']).then(
      r => console.log('navigated to login')
    ).catch(
      e => console.log('error navigating to login')
    );
  }
}
