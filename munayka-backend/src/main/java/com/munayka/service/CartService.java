package com.munayka.service;

import com.munayka.model.*;
import com.munayka.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class CartService {
    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;
    
    @Transactional(readOnly = true)
    public Cart getCartByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepository.findByUserWithItems(user)
                .orElseGet(() -> {
                    Cart emptyCart = new Cart();
                    emptyCart.setUser(user);
                    emptyCart.setItems(new ArrayList<>());
                    return emptyCart;
                });
    }
    
    public Cart addToCart(Long userId, Long productId, Integer quantity) {
        if (quantity <= 0) throw new RuntimeException("Quantity must be greater than 0");
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }
        
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
        
        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);
        
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cartItemRepository.save(newItem);
        }
        
        return cartRepository.findByUserWithItems(user)
                .orElseThrow(() -> new RuntimeException("Error retrieving cart"));
    }
    
    public Cart updateCartItem(Long userId, Long productId, Integer quantity) {
        if (quantity < 0) throw new RuntimeException("Quantity cannot be negative");
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        if (quantity == 0) {
            cartItemRepository.deleteByCartAndProduct(cart, product);
        } else {
            if (product.getStock() < quantity) {
                throw new RuntimeException("Insufficient stock");
            }
            CartItem item = cartItemRepository.findByCartAndProduct(cart, product)
                    .orElseThrow(() -> new RuntimeException("Product not found in cart"));
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
        
        return cartRepository.findByUserWithItems(user)
                .orElseThrow(() -> new RuntimeException("Error retrieving cart"));
    }
    
    public Cart removeFromCart(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        cartItemRepository.deleteByCartAndProduct(cart, product);
        
        return cartRepository.findByUserWithItems(user)
                .orElseThrow(() -> new RuntimeException("Error retrieving cart"));
    }
    
    public void clearCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
    }
}