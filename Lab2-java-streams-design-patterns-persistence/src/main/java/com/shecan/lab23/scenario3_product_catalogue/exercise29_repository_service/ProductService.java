package com.shecan.lab23.scenario3_product_catalogue.exercise29_repository_service;

import com.shecan.lab23.scenario3_product_catalogue.model.Product;

import java.util.List;

public class ProductService {
    private final ProductRepository repository;

    // Constructor injection - depends on interface, not concrete class
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public void save(Product product) {
        repository.save(product);
    }

    public Product findById(String id) {
        return repository.findById(id);
    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public void updateStock(String productId, int newStock) {
        repository.updateStock(productId, newStock);
    }

    public void transferStock(String fromId, String toId, int quantity) {
        repository.transferStock(fromId, toId, quantity);
    }

    // @Transactional - bulk import with rollback on validation failure
    public void bulkImport(List<Product> products) {
        for (Product product : products) {
            // Validate
            if (product.getId() == null || product.getId().trim().isEmpty()) {
                throw new IllegalArgumentException("Product ID cannot be blank: " + product);
            }
            if (product.getPrice() < 0) {
                throw new IllegalArgumentException("Product price cannot be negative: " + product);
            }
            // Save valid product
            repository.save(product);
        }
    }

    public void clearAll() {
        repository.clearTable();
    }
}