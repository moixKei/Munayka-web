package com.munayka.repository;

import com.munayka.model.Order;
import com.munayka.model.OrderStatus;
import com.munayka.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Buscar órdenes por usuario
    List<Order> findByUserOrderByOrderDateDesc(User user);
    
    // Buscar órdenes por estado
    List<Order> findByStatusOrderByOrderDateDesc(OrderStatus status);
    
    // Buscar órdenes por usuario y estado
    List<Order> findByUserAndStatusOrderByOrderDateDesc(User user, OrderStatus status);
    
    // Consulta personalizada para órdenes recientes
    @Query("SELECT o FROM Order o WHERE o.orderDate >= :sinceDate ORDER BY o.orderDate DESC")
    List<Order> findRecentOrders(@Param("sinceDate") java.time.LocalDateTime sinceDate);
    
    // Contar órdenes por estado
    long countByStatus(OrderStatus status);
}