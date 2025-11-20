package com.munayka.repository;

import com.munayka.model.Product;
import com.munayka.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Consulta derivada - busca por categoría (CORREGIDO)
    List<Product> findByCategory(Category category);
    
    // Consulta derivada - busca productos con stock disponible
    List<Product> findByStockGreaterThan(Integer stock);
    
    // Consulta JPQL personalizada con parámetros (CORREGIDO)
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :priceMin AND :priceMax")
    List<Product> findByPriceRange(@Param("priceMin") BigDecimal priceMin, 
                                   @Param("priceMax") BigDecimal priceMax);
    
    // Consulta JPQL para buscar por nombre (case insensitive) (CORREGIDO)
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByNameContaining(@Param("name") String name);
    
    // Consulta para productos por categoría y stock disponible (CORREGIDO)
    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.stock > 0")
    List<Product> findByCategoryWithStock(@Param("category") Category category);
}