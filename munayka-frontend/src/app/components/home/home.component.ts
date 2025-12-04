// src/app/components/home/home.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  featuredProducts: Product[] = [];  // ← DECLARA LA PROPIEDAD AQUÍ

  constructor(private productService: ProductService) {}

  ngOnInit() {
    this.loadFeaturedProducts();
  }

  loadFeaturedProducts() {
    this.productService.getProducts().subscribe({
      next: (products) => {
        // Tomar los primeros 4 productos como destacados
        this.featuredProducts = products.slice(0, 4);
      },
      error: (error) => {
        console.error('Error cargando productos:', error);
        // Productos de ejemplo si falla la conexión
        this.featuredProducts = this.getSampleFeaturedProducts();
      }
    });
  }

  // Método para obtener emoji según categoría (opcional)
  getCategoryEmoji(category: string): string {
    switch(category?.toUpperCase()) {
      case 'ANILLOS': return '💍';
      case 'COLLARES': return '📿';
      case 'ARETES': return '👂';
      case 'PULSERAS': return '📿';
      default: return '💎';
    }
  }

  private getSampleFeaturedProducts(): Product[] {
    return [
      {
        id: 1,
        name: 'Anillo Diamante Elite',
        description: 'Anillo con diamante central talla princesa',
        price: 1200.00,
        stock: 5,
        imageUrl: 'https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=400',
        category: 'ANILLOS'
      },
      {
        id: 2,
        name: 'Collar Esmeralda Real',
        description: 'Collar con esmeralda colombiana de 2 quilates',
        price: 850.00,
        stock: 3,
        imageUrl: 'https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=400',
        category: 'COLLARES'
      },
      {
        id: 3,
        name: 'Aretes Perla Cultivada',
        description: 'Aretes de oro blanco con perlas cultivadas',
        price: 450.00,
        stock: 8,
        imageUrl: 'https://images.unsplash.com/photo-1602173574767-37ac01994b2a?w=400',
        category: 'ARETES'
      },
      {
        id: 4,
        name: 'Pulsera Eslava Oro',
        description: 'Pulsera ajustable en oro de 18 quilates',
        price: 680.00,
        stock: 6,
        imageUrl: 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=400',
        category: 'PULSERAS'
      }
    ];
  }
}