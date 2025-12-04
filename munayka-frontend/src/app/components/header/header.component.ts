// header.component.ts (actualizado)
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  cartItemCount = 0;
  
  constructor(private cartService: CartService) {}
  
  ngOnInit() {
    // Suscribirse al contador del carrito
    this.cartService.cartCount$.subscribe(count => {
      this.cartItemCount = count;
    });
  }
}