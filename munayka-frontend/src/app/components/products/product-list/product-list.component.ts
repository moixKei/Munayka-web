// src/app/components/products/product-list/product-list.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../../services/product.service';
import { CartService } from '../../../services/cart.service';
import { Product } from '../../../models/product.model';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [
    CommonModule, 
    RouterModule, 
    FormsModule
  ],
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  searchTerm: string = '';
  
  constructor(
    private productService: ProductService,
    private cartService: CartService
  ) {}
  
  ngOnInit() {
    this.loadProducts();
  }
  
  loadProducts() {
    this.productService.getProducts().subscribe({
      next: (data) => {
        this.products = data;
      },
      error: (error) => {
        console.error('Error cargando productos:', error);
        // Datos de ejemplo
        this.products = this.getSampleProducts();
      }
    });
  }
  
  addToCart(product: Product) {
    this.cartService.addToCart(product);
    alert(`${product.name} agregado al carrito!`);
  }
  
  // Método para actualizar término de búsqueda
  updateSearchTerm(value: string) {
    this.searchTerm = value;
  }
  
  searchProducts() {
    console.log('Buscando:', this.searchTerm);
    // Implementar lógica de búsqueda aquí
  }
  
  private getSampleProducts(): Product[] {
    return [
      {
        id: 1,
        name: 'Anillo de Oro 18k',
        description: 'Anillo elegante en oro amarillo',
        price: 450.00,
        stock: 10,
        imageUrl: 'https://via.placeholder.com/300',
        category: 'ANILLOS'
      },
      {
        id: 2,
        name: 'Collar de Plata',
        description: 'Collar con dije de corazón',
        price: 280.50,
        stock: 15,
        imageUrl: 'https://via.placeholder.com/300',
        category: 'COLLARES'
      }
    ];  // ← CIERRA CORRECTAMENTE EL ARRAY
  }
}