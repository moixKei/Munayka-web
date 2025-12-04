// src/app/models/order.model.ts
export interface Order {
  id?: number;
  userId: number;
  items: OrderItem[];
  totalAmount: number;
  shippingAddress: string;
  phone?: string;
  email?: string;
  status: 'PENDING' | 'PROCESSING' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';
  createdAt?: Date;
}

export interface OrderItem {
  productId: number;
  productName: string;
  price: number;
  quantity: number;
}