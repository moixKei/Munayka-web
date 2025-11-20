package com.munayka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/test-db")
    public String testDatabase() {
        try {
            jdbcTemplate.execute("SELECT 1");
            return "✅ CONNECTION SUCCESSFUL - MySQL MUNAYKA working!";
        } catch (Exception e) {
            return "❌ CONNECTION ERROR: " + e.getMessage();
        }
    }

    @GetMapping("/test")
    public String test() {
        return "🚀 MUNAYKA Backend active - Ready to develop!";
    }
    
    @GetMapping("/test-products")
    public String testProducts() {
        try {
            jdbcTemplate.execute("SELECT COUNT(*) FROM productos");
            return "✅ PRODUCTS TABLE EXISTS - Ready for products API!";
        } catch (Exception e) {
            return "❌ PRODUCTS TABLE ERROR: " + e.getMessage();
        }
    }
}