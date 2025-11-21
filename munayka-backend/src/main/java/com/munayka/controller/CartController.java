package com.munayka.controller;

import com.munayka.model.Cart;
import com.munayka.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Cart> getCartByUser(@PathVariable Long userId) {
        try {
            Cart cart = cartService.getCartByUser(userId);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/user/{userId}/add")
    public ResponseEntity<Cart> addToCart(
            @PathVariable Long userId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        try {
            Cart cart = cartService.addToCart(userId, productId, quantity);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/user/{userId}/update")
    public ResponseEntity<Cart> updateCartItem(
            @PathVariable Long userId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        try {
            Cart cart = cartService.updateCartItem(userId, productId, quantity);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/user/{userId}/remove")
    public ResponseEntity<Cart> removeFromCart(
            @PathVariable Long userId,
            @RequestParam Long productId) {
        try {
            Cart cart = cartService.removeFromCart(userId, productId);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/user/{userId}/clear")
    public ResponseEntity<?> clearCart(@PathVariable Long userId) {
        try {
            cartService.clearCart(userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}