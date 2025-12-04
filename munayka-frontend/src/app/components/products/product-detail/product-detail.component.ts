// src/app/components/products/product-detail/product-detail.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { ProductService } from '../../../services/product.service';
import { CartService } from '../../../services/cart.service';
import { Product } from '../../../models/product.model';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {
  product!: Product;
  quantity: number = 1;
  isLoading: boolean = true;
  relatedProducts: Product[] = [];
  
  // Mapeo de emojis por categoría
  categoryEmojis: {[key: string]: string} = {
    'ANILLOS': '💍',
    'COLLARES': '📿', 
    'PULSERAS': '📿',
    'ARETES': '👂',
    'TODOS': '💎'
  };

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private cartService: CartService
  ) {}

  ngOnInit() {
    // Obtener el ID de la ruta
    this.route.params.subscribe(params => {
      const id = +params['id']; // El + convierte a número
      this.loadProduct(id);
    });
  }

  loadProduct(id: number) {
    this.isLoading = true;
    
    this.productService.getProductById(id).subscribe({
      next: (product) => {
        this.product = product;
        this.isLoading = false;
        console.log('Producto cargado:', product);
        
        // Cargar productos relacionados (misma categoría)
        this.loadRelatedProducts(product.category, product.id);
      },
      error: (error) => {
        console.error('Error cargando producto:', error);
        this.isLoading = false;
      }
    });
  }

  loadRelatedProducts(category: string, excludeId: number) {
    this.productService.getProducts().subscribe({
      next: (products) => {
        // Filtrar productos de la misma categoría, excluyendo el actual
        this.relatedProducts = products
          .filter(p => p.category === category && p.id !== excludeId)
          .slice(0, 4); // Máximo 4 productos relacionados
      }
    });
  }

  increaseQuantity() {
    if (this.quantity < this.product.stock) {
      this.quantity++;
    }
  }

  decreaseQuantity() {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  addToCart() {
    if (this.product.stock > 0) {
      this.cartService.addToCart(this.product, this.quantity);
      alert(`✅ ${this.quantity} x ${this.product.name} agregado al carrito!`);
    } else {
      alert('❌ Producto agotado');
    }
  }

  getCategoryEmoji(category: string): string {
    return this.categoryEmojis[category] || '💎';
  }

  // En product-detail.component.ts, agrega estos métodos:

getCategoryColor(category: string): string {
  const colors: {[key: string]: string} = {
    'ANILLOS': '#9d4edd',
    'COLLARES': '#4361ee',
    'PULSERAS': '#f72585',
    'ARETES': '#4cc9f0',
    'TODOS': '#d4af37'
  };
  return colors[category] || '#d4af37';
}

getMaterialByCategory(category: string): string {
  const materials: {[key: string]: string} = {
    'ANILLOS': 'Oro 18k / Plata 925',
    'COLLARES': 'Plata esterlina / Oro',
    'PULSERAS': 'Cuero genuino / Metales nobles',
    'ARETES': 'Oro hipoalergénico',
    'TODOS': 'Materiales premium'
  };
  return materials[category] || 'Materiales de alta calidad';
}
}