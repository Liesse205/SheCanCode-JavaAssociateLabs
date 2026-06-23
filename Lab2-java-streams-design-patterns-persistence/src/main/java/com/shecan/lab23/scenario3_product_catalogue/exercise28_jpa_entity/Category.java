package com.shecan.lab23.scenario3_product_catalogue.exercise28_jpa_entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<ProductJpaEntity> products = new ArrayList<>();

    public Category() {}

    public Category(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<ProductJpaEntity> getProducts() { return products; }
    public void setProducts(List<ProductJpaEntity> products) { this.products = products; }

    public void addProduct(ProductJpaEntity product) {
        products.add(product);
        product.setCategory(this);
    }

    @Override
    public String toString() {
        return "Category{id=" + id + ", name='" + name + "', products=" + products.size() + "}";
    }
}