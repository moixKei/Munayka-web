// src/app/models/user.model.ts
export interface User {
  id?: number;
  name: string;
  email: string;
  password?: string;
  phone?: string;
  address?: string;
  role?: string;  // 'USER', 'ADMIN'
  createdAt?: Date;
}