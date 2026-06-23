package com.shecan.lab23.scenario3_product_catalogue.exercise28_jpa_entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class ProductJpaEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private double price;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Version
    @Column(name = "version", nullable = false)
    private Long version = 0L;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public ProductJpaEntity() {}

    public ProductJpaEntity(String id, String name, String description, double price, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    @Override
    public String toString() {
        return "ProductJpaEntity{id='" + id + "', name='" + name + "', price=" + price + 
               ", stockQuantity=" + stockQuantity + ", version=" + version + "}";
    }
}