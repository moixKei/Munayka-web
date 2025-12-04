// src/app/services/cart.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { CartItem } from '../models/cart-item.model';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cartItems: CartItem[] = [];
  private cartCount = new BehaviorSubject<number>(0);
  private cartTotal = new BehaviorSubject<number>(0);

  cartCount$ = this.cartCount.asObservable();
  cartTotal$ = this.cartTotal.asObservable();

  constructor() {
    // Cargar carrito desde localStorage si existe
    this.loadFromLocalStorage();
  }

  addToCart(product: any, quantity: number = 1) {
    const existingItem = this.cartItems.find(
      item => item.productId === product.id
    );

    if (existingItem) {
      existingItem.quantity += quantity;
    } else {
      const newItem: CartItem = {
        id: Date.now(), // ID temporal
        productId: product.id,
        productName: product.name,
        price: product.price,
        quantity: quantity,
        imageUrl: product.imageUrl
      };
      this.cartItems.push(newItem);
    }

    this.updateCart();
  }

  removeFromCart(itemId: number) {
    this.cartItems = this.cartItems.filter(item => item.id !== itemId);
    this.updateCart();
  }

  updateQuantity(itemId: number, quantity: number) {
    const item = this.cartItems.find(item => item.id === itemId);
    if (item) {
      item.quantity = quantity > 0 ? quantity : 1;
      this.updateCart();
    }
  }

  getCartItems(): CartItem[] {
    return [...this.cartItems];
  }

  getTotal(): number {
    return this.cartItems.reduce(
      (total, item) => total + (item.price * item.quantity), 0
    );
  }

  clearCart() {
    this.cartItems = [];
    this.updateCart();
  }

  private updateCart() {
    this.cartCount.next(this.cartItems.length);
    this.cartTotal.next(this.getTotal());
    this.saveToLocalStorage();
  }

  private saveToLocalStorage() {
    localStorage.setItem('munayka_cart', JSON.stringify(this.cartItems));
  }

  private loadFromLocalStorage() {
    const saved = localStorage.getItem('munayka_cart');
    if (saved) {
      this.cartItems = JSON.parse(saved);
      this.updateCart();
    }
  }
}