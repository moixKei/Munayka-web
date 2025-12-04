// src/app/services/product.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, of } from 'rxjs';
import { Product } from '../models/product.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'http://localhost:8080/api/products';

  constructor(private http: HttpClient) {}

  // Obtener todos los productos
  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }

  // Obtener producto por ID
  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`).pipe(
      catchError(error => {
        console.error(`Error obteniendo producto ${id}:`, error);
        // Producto de ejemplo si falla
        return of(this.getSampleProduct(id));
      })
    );
  }
  // Producto de ejemplo para desarrollo
  private getSampleProduct(id: number): Product {
    const sampleProducts: {[key: number]: Product} = {
      1: {
        id: 1,
        name: 'Anillo de Oro 18k',
        description: 'Anillo elegante en oro amarillo 18 quilates con diseño artesanal. Perfecto para ocasiones especiales.',
        price: 450.00,
        stock: 10,
        imageUrl: 'https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=800',
        category: 'ANILLOS'
      },
      2: {
        id: 2,
        name: 'Collar de Plata 925',
        description: 'Collar con dije de corazón en plata esterlina. Incluye cadena ajustable de 45cm.',
        price: 280.50,
        stock: 15,
        imageUrl: 'https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=800',
        category: 'COLLARES'
      }
    };
    
    return sampleProducts[id] || {
      id: id,
      name: 'Producto de Ejemplo',
      description: 'Descripción del producto',
      price: 100.00,
      stock: 5,
      imageUrl: 'https://via.placeholder.com/800x600/667eea/ffffff?text=Producto+Ejemplo',
      category: 'TODOS'
    };
  }
  // Buscar por nombre
  searchProducts(name: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/search?name=${name}`);
  }

  // Por categoría
  getProductsByCategory(category: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/category/${category}`);
  }
}