package com.munayka.service;

import com.munayka.model.*;
import com.munayka.repository.OrderRepository;
import com.munayka.repository.OrderItemRepository;
import com.munayka.repository.ProductRepository;
import com.munayka.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CartService cartService;
    
    // Crear orden desde el carrito
    public Order createOrderFromCart(Long userId, String shippingAddress, String phone, String email) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        // Obtener carrito del usuario
        Cart cart = cartService.getCartByUser(userId);
        
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        
        // Verificar stock de todos los productos
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName() + 
                                          ". Available: " + product.getStock());
            }
        }
        
        // Crear nueva orden
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setPhone(phone);
        order.setEmail(email != null ? email : user.getEmail());
        order.setStatus(OrderStatus.PENDIENTE);
        
        // Crear items de la orden desde el carrito
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice()); // Guardar precio actual
            
            order.getItems().add(orderItem);
            
            // Actualizar stock del producto
            Product product = cartItem.getProduct();
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }
        
        // Calcular total
        order.calculateTotal();
        
        // Guardar orden
        Order savedOrder = orderRepository.save(order);
        
        // Vaciar carrito
        cartService.clearCart(userId);
        
        return savedOrder;
    }
    
    // Obtener todas las órdenes
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    // Obtener orden por ID
    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
    
    // Obtener órdenes por usuario
    @Transactional(readOnly = true)
    public List<Order> getOrdersByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }
    
    // Obtener órdenes por estado
    @Transactional(readOnly = true)
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatusOrderByOrderDateDesc(status);
    }
    
    // Actualizar estado de la orden
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
    
    // Cancelar orden (solo si está pendiente)
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        if (order.getStatus() != OrderStatus.PENDIENTE) {
            throw new RuntimeException("Only pending orders can be cancelled");
        }
        
        // Restaurar stock
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }
        
        order.setStatus(OrderStatus.CANCELADO);
        return orderRepository.save(order);
    }
    
    // Obtener estadísticas
    @Transactional(readOnly = true)
    public OrderStatistics getOrderStatistics() {
        OrderStatistics stats = new OrderStatistics();
        stats.setTotalOrders(orderRepository.count());
        stats.setPendingOrders(orderRepository.countByStatus(OrderStatus.PENDIENTE));
        stats.setConfirmedOrders(orderRepository.countByStatus(OrderStatus.CONFIRMADO));
        stats.setDeliveredOrders(orderRepository.countByStatus(OrderStatus.ENTREGADO));
        
        // Calcular ingresos totales (solo órdenes entregadas)
        List<Order> deliveredOrders = orderRepository.findByStatusOrderByOrderDateDesc(OrderStatus.ENTREGADO);
        BigDecimal totalRevenue = deliveredOrders.stream()
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.setTotalRevenue(totalRevenue);
        
        return stats;
    }
    
    // Clase interna para estadísticas
    public static class OrderStatistics {
        private long totalOrders;
        private long pendingOrders;
        private long confirmedOrders;
        private long deliveredOrders;
        private BigDecimal totalRevenue;
        
        // Getters y Setters
        public long getTotalOrders() { return totalOrders; }
        public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }
        
        public long getPendingOrders() { return pendingOrders; }
        public void setPendingOrders(long pendingOrders) { this.pendingOrders = pendingOrders; }
        
        public long getConfirmedOrders() { return confirmedOrders; }
        public void setConfirmedOrders(long confirmedOrders) { this.confirmedOrders = confirmedOrders; }
        
        public long getDeliveredOrders() { return deliveredOrders; }
        public void setDeliveredOrders(long deliveredOrders) { this.deliveredOrders = deliveredOrders; }
        
        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    }
}