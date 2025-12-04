
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CartItem } from '../../models/cart-item.model';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent implements OnInit {
  cartItems: CartItem[] = [];  // ← Tipo específico
  
  // Datos de ejemplo para probar
  ngOnInit() {
    this.cartItems = [
      {
        id: 1,
        productId: 101,
        productName: 'Anillo de Oro 18k',
        price: 450.00,
        quantity: 1,
        imageUrl: '/assets/images/ring.jpg'
      },
      {
        id: 2,
        productId: 102,
        productName: 'Collar de Plata',
        price: 280.50,
        quantity: 2,
        imageUrl: '/assets/images/necklace.jpg'
      }
    ];
  }
  
  // Métodos para manipular el carrito
  removeItem(itemId: number) {
    this.cartItems = this.cartItems.filter(item => item.id !== itemId);
  }
  
  updateQuantity(itemId: number, newQuantity: number) {
    const item = this.cartItems.find(item => item.id === itemId);
    if (item && newQuantity > 0) {
      item.quantity = newQuantity;
    }
  }
  
  getTotal(): number {
    return this.cartItems.reduce((total, item) => 
      total + (item.price * item.quantity), 0);
  }
}