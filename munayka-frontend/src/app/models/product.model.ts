// src/app/models/product.model.ts
export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;  // Cambié de string/number según lo que devuelva BigDecimal
  stock: number;
  imageUrl: string;
  category: string;  // 'COLLARES' | 'PULSERAS' | 'ANILLOS' | 'ARETES' | 'TODOS'
}