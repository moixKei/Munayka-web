package com.munayka.repository;

import com.munayka.model.Order;
import com.munayka.model.OrderStatus;
import com.munayka.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
    List<Order> findByStatusOrderByOrderDateDesc(OrderStatus status);
    List<Order> findByUserAndStatusOrderByOrderDateDesc(User user, OrderStatus status);
    long countByStatus(OrderStatus status);
    
    @Query("SELECT o FROM Order o JOIN FETCH o.user JOIN FETCH o.items i JOIN FETCH i.product WHERE o.id = :id")
    Optional<Order> findByIdWithDetails(@Param("id") Long id);
    
    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.items i JOIN FETCH i.product WHERE o.user.id = :userId")
    List<Order> findByUserWithItems(@Param("userId") Long userId);
}