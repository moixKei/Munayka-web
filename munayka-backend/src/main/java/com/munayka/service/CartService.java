package com.munayka.service;

import com.munayka.model.*;
import com.munayka.repository.CartRepository;
import com.munayka.repository.CartItemRepository;
import com.munayka.repository.ProductRepository;
import com.munayka.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class CartService {
    
	@Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    // Obtener carrito por usuario - SOLO LECTURA
    @Transactional(readOnly = true)
    public Cart getCartByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        return cartRepository.findByUserWithItems(user)
                .orElseGet(() -> {
                    Cart emptyCart = new Cart();
                    emptyCart.setUser(user);
                    emptyCart.setItems(new ArrayList<>());
                    return emptyCart;
                });
    }
    
    // Agregar producto al carrito - ESCRITURA
    public Cart addToCart(Long userId, Long productId, Integer quantity) {
        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        // Verificar stock
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getStock());
        }
        
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
        
        // Buscar si el producto ya está en el carrito
        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);
        
        if (existingItem.isPresent()) {
            // Actualizar cantidad
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            // Crear nuevo item
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cartItemRepository.save(newItem);
        }
        
        return cartRepository.findByUserWithItems(user)
                .orElseThrow(() -> new RuntimeException("Error retrieving cart"));
    }
    
    // Actualizar cantidad de un producto en el carrito
    public Cart updateCartItem(Long userId, Long productId, Integer quantity) {
        if (quantity < 0) {
            throw new RuntimeException("Quantity cannot be negative");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));
        
        if (quantity == 0) {
            // Eliminar item si cantidad es 0
            cartItemRepository.deleteByCartAndProduct(cart, product);
        } else {
            // Verificar stock
            if (product.getStock() < quantity) {
                throw new RuntimeException("Insufficient stock. Available: " + product.getStock());
            }
            
            // Actualizar cantidad
            CartItem item = cartItemRepository.findByCartAndProduct(cart, product)
                    .orElseThrow(() -> new RuntimeException("Product not found in cart"));
            
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
        
        return cartRepository.findByUserWithItems(user)
                .orElseThrow(() -> new RuntimeException("Error retrieving cart"));
    }
    
    // Eliminar producto del carrito
    public Cart removeFromCart(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));
        
        cartItemRepository.deleteByCartAndProduct(cart, product);
        
        return cartRepository.findByUserWithItems(user)
                .orElseThrow(() -> new RuntimeException("Error retrieving cart"));
    }
    
    // Vaciar carrito
    public void clearCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));
        
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
    }
}