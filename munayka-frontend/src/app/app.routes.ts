import { Routes } from '@angular/router';

export const routes: Routes = [
  { 
    path: '', 
    loadComponent: () => import('./components/home/home.component')
      .then(m => m.HomeComponent) 
  },
  { 
    path: 'products', 
    loadComponent: () => import('./components/products/product-list/product-list.component')
      .then(m => m.ProductListComponent) 
  },
  { 
    path: 'cart', 
    loadComponent: () => import('./components/cart/cart.component')
      .then(m => m.CartComponent) 
  },
  { path: '**', redirectTo: '' }
];