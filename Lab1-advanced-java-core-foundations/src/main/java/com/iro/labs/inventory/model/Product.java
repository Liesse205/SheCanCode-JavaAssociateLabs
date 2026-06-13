package com.iro.labs.inventory.model;
import java.util.Objects;
import java.util.UUID;

public abstract class Product {
    private final String id;
    private String name;
    private Category category;
    private Double price;
    
    public Product(String name, Category category, Double price) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.category = category;
        this.price = price;
    }
    
    public Product(String id, String name, Category category, Double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public Category getCategory() { return category; }
    public Double getPrice() { return price; }
    
    public void setName(String name) { this.name = Objects.requireNonNull(name, "Name cannot be null"); }
    public void setCategory(Category category) { this.category = Objects.requireNonNull(category, "Category cannot be null"); }
    public void setPrice(Double price) {
        if (price != null && price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.price = price;
    }
    
    @Override
    public String toString() {
        return String.format("Product{id='%s', name='%s', category=%s, price=%s}", id, name, category, price);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }
    
    @Override
    public int hashCode() { return Objects.hash(id); }
}
