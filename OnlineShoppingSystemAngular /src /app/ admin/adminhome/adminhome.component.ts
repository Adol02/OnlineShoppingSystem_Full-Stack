import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-adminhome',
  templateUrl: './adminhome.component.html',
  styleUrls: ['./adminhome.component.css']
})
export class AdminhomeComponent {
  constructor(private router: Router) {}

  logout() {
    // Clear user session and redirect to login page
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
