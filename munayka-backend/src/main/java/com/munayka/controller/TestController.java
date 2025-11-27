package com.munayka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired private JdbcTemplate jdbcTemplate;

    @GetMapping("/test")
    public String test() {
        return "🚀 MUNAYKA Backend funcionando correctamente!";
    }
    
    @GetMapping("/test-db")
    public String testDatabase() {
        try {
            jdbcTemplate.execute("SELECT 1");
            return "✅ Conexión a MySQL exitosa!";
        } catch (Exception e) {
            return "❌ Error de conexión: " + e.getMessage();
        }
    }
}