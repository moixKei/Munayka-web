package com.munayka.service;

import com.munayka.model.Product;
import com.munayka.model.Category;
import com.munayka.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {
    @Autowired private ProductRepository productRepository;
    
    @Transactional(readOnly = true)
    public List<Product> getAll() { return productRepository.findAll(); }
    
    @Transactional(readOnly = true)
    public Optional<Product> getById(Long id) { return productRepository.findById(id); }
    
    public Product save(Product product) { return productRepository.save(product); }
    
    public void delete(Long id) { productRepository.deleteById(id); }
    
    @Transactional(readOnly = true)
    public List<Product> getByCategory(Category category) { return productRepository.findByCategory(category); }
    
    @Transactional(readOnly = true)
    public List<Product> getAvailable() { return productRepository.findByStockGreaterThan(0); }
    
    @Transactional(readOnly = true)
    public List<Product> findByPriceRange(BigDecimal priceMin, BigDecimal priceMax) { 
        return productRepository.findByPriceRange(priceMin, priceMax); 
    }
    
    @Transactional(readOnly = true)
    public List<Product> findByName(String name) { return productRepository.findByNameContaining(name); }
    
    public Product updateStock(Long id, Integer newStock) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStock(newStock);
        return productRepository.save(product);
    }
}