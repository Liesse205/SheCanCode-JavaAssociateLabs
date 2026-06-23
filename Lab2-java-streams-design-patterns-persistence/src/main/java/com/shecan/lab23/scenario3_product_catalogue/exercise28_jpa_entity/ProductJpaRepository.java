package com.shecan.lab23.scenario3_product_catalogue.exercise28_jpa_entity;

import com.shecan.lab23.scenario3_product_catalogue.exercise29_repository_service.ProductRepository;
import com.shecan.lab23.scenario3_product_catalogue.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ProductJpaRepository implements ProductRepository {
    private final EntityManager entityManager;

    public ProductJpaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Product product) {
        ProductJpaEntity jpaProduct = new ProductJpaEntity(
            product.getId(),
            product.getName(),
            product.getCategory(),
            product.getPrice(),
            product.getStockQuantity()
        );
        entityManager.persist(jpaProduct);
    }

    @Override
    public Product findById(String id) {
        ProductJpaEntity jpaProduct = 
            entityManager.find(ProductJpaEntity.class, id);
        if (jpaProduct == null) {
            return null;
        }
        return convertToModel(jpaProduct);
    }

    @Override
    public List<Product> findAll() {
        String jpql = "SELECT p FROM ProductJpaEntity p";
        TypedQuery<ProductJpaEntity> query = 
            entityManager.createQuery(jpql, ProductJpaEntity.class);
        List<ProductJpaEntity> results = query.getResultList();
        return results.stream().map(this::convertToModel).toList();
    }

    @Override
    public void deleteById(String id) {
        ProductJpaEntity product = 
            entityManager.find(ProductJpaEntity.class, id);
        if (product != null) {
            entityManager.remove(product);
        }
    }

    @Override
    public void updateStock(String productId, int newStock) {
        ProductJpaEntity product = 
            entityManager.find(ProductJpaEntity.class, productId);
        if (product == null) {
            throw new RuntimeException("Product not found: " + productId);
        }
        product.setStockQuantity(newStock);
        // No need to call merge - the entity is already managed
        // Just flush to force the update
        entityManager.flush();
    }

    @Override
    public void transferStock(String fromId, String toId, int quantity) {
        try {
            entityManager.getTransaction().begin();
            
            ProductJpaEntity fromProduct = 
                entityManager.find(ProductJpaEntity.class, fromId);
            ProductJpaEntity toProduct = 
                entityManager.find(ProductJpaEntity.class, toId);
            
            if (fromProduct == null) {
                throw new RuntimeException("Source product not found: " + fromId);
            }
            if (toProduct == null) {
                throw new RuntimeException("Target product not found: " + toId);
            }
            
            int fromStock = fromProduct.getStockQuantity();
            int toStock = toProduct.getStockQuantity();
            
            if (fromStock < quantity) {
                throw new RuntimeException("Insufficient stock for product: " + fromId);
            }
            
            fromProduct.setStockQuantity(fromStock - quantity);
            toProduct.setStockQuantity(toStock + quantity);
            
            entityManager.getTransaction().commit();
            System.out.println("[TRANSFER] Transferred " + quantity + " units from " + fromId + " to " + toId);
            
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
                System.out.println("[TRANSFER] Rolled back transfer from " + fromId + " to " + toId);
            }
            throw new RuntimeException("Transfer failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void clearTable() {
        String jpql = "DELETE FROM ProductJpaEntity";
        entityManager.createQuery(jpql).executeUpdate();
    }

    public void flush() {
        entityManager.flush();
    }

    private Product convertToModel(ProductJpaEntity jpaProduct) {
        Product product = new Product();
        product.setId(jpaProduct.getId());
        product.setName(jpaProduct.getName());
        product.setCategory(jpaProduct.getDescription());
        product.setPrice(jpaProduct.getPrice());
        product.setStockQuantity(jpaProduct.getStockQuantity());
        return product;
    }

    public ProductJpaEntity findJpaEntityById(String id) {
        return entityManager.find(ProductJpaEntity.class, id);
    }
}