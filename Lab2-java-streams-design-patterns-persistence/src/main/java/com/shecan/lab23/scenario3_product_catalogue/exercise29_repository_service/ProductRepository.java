package com.shecan.lab23.scenario3_product_catalogue.exercise29_repository_service;

import com.shecan.lab23.scenario3_product_catalogue.model.Product;

import java.util.List;

public interface ProductRepository {
    void save(Product product);
    Product findById(String id);
    List<Product> findAll();
    void deleteById(String id);
    void updateStock(String productId, int newStock);
    void transferStock(String fromId, String toId, int quantity);
    void clearTable();
}