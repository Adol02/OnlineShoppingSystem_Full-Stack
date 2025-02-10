import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-addproduct',
  templateUrl: './addproduct.component.html',
  styleUrls: ['./addproduct.component.css']
})
export class AddproductComponent {
  product = {
    productname: '',
    category: '',
    description: '',
    price: 0,
    quantity: 0
  };
  images: File[] = []; // To hold selected image files

  constructor(private http: HttpClient, private router: Router) {}

  onFileSelected(event: any) {
    this.images = Array.from(event.target.files);
  }

  onSubmit() {
    const formData = new FormData();

    // Add product data as a JSON string
    formData.append('product', JSON.stringify(this.product));

    // Add selected images
    for (let image of this.images) {
      formData.append('images', image);
    }

    // Send request to the backend API
    this.http.post('http://localhost:8080/admin/addSingleProductWithMultipleImages', formData, { responseType: 'text' })
  .subscribe({
    next: (response) => {
      alert(response);
      this.router.navigate(['/admin/home']);
       // Displays the success message from the server
    },
    error: (err) => {
      console.error(err);
      alert('Failed to add product.');
    }
  });
  }
}
