package com.shecan.lab23.scenario3_product_catalogue.exercise29_repository_service;

import com.shecan.lab23.scenario3_product_catalogue.exercise27_jdbc_repository.ProductJdbcRepository;
import com.shecan.lab23.scenario3_product_catalogue.exercise28_jpa_entity.JpaTestUtil;
import com.shecan.lab23.scenario3_product_catalogue.exercise28_jpa_entity.ProductJpaRepository;
import com.shecan.lab23.scenario3_product_catalogue.model.Product;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    private static final String SEPARATOR = "============================================================";
    private static final String JDBC_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

    private Connection connection;
    private ProductService productService;
    private ProductRepository repository;

    @BeforeEach
    void setUp() throws SQLException {
        System.out.println();
        System.out.println(SEPARATOR);
        System.out.println("SETUP: Creating fresh database and service");
        System.out.println(SEPARATOR);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        JpaTestUtil.close();
        System.out.println("TEARDOWN: Cleanup completed");
    }

    private void printHeader(String title) {
        System.out.println();
        System.out.println(SEPARATOR);
        System.out.println("TEST: " + title);
        System.out.println(SEPARATOR);
    }

    // ===== Test 1: JDBC Implementation =====

    @Test
    void testServiceWithJdbcRepository() throws SQLException {
        printHeader("ProductService with JDBC Repository");

        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        repository = new ProductJdbcRepository(connection);
        productService = new ProductService(repository);

        System.out.println("Using: JDBC Repository");
        System.out.println("Repository class: " + repository.getClass().getSimpleName());

        testServiceOperations();
    }

    // ===== Test 2: JPA Implementation (Swapping Implementation) =====

    @Test
    void testServiceWithJpaRepository() {
        printHeader("ProductService with JPA Repository");

        EntityManager entityManager = JpaTestUtil.getEntityManager();
        repository = new ProductJpaRepository(entityManager);
        productService = new ProductService(repository);

        System.out.println("Using: JPA Repository");
        System.out.println("Repository class: " + repository.getClass().getSimpleName());

        testServiceOperations();
    }

    // ===== Test 3: Swap Implementation at Runtime =====

    @Test
    void testSwapImplementationAtRuntime() throws SQLException {
        printHeader("Swap Implementation at Runtime");

        // Start with JDBC
        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        repository = new ProductJdbcRepository(connection);
        productService = new ProductService(repository);

        System.out.println("Initial Implementation: JDBC");
        System.out.println("Repository: " + repository.getClass().getSimpleName());

        // Create a product
        Product product = new Product("P001", "Laptop", "Electronics", 999.99, 50);
        productService.save(product);
        System.out.println("Saved product with JDBC: " + product);

        // Swap to JPA
        System.out.println();
        System.out.println("Swapping to JPA Implementation...");
        EntityManager entityManager = JpaTestUtil.getEntityManager();
        repository = new ProductJpaRepository(entityManager);
        productService = new ProductService(repository);
        System.out.println("New Repository: " + repository.getClass().getSimpleName());

        // Find product using JPA (should still work, but database is fresh)
        // Note: This demonstrates the swap, but each repository has its own database
        // The JPA repository has an empty database, so we need to add data again
        // This test demonstrates that the service works with both implementations

        // Add product with JPA
        Product product2 = new Product("P002", "Smartphone", "Electronics", 699.99, 100);
        productService.save(product2);
        Product found = productService.findById("P002");

        System.out.println("Found with JPA: " + found);
        assertNotNull(found);
        assertEquals("P002", found.getId());

        System.out.println();
        System.out.println("VERIFICATION: Service works with both JDBC and JPA implementations");
        System.out.println("No changes to ProductService were needed!");
        System.out.println(SEPARATOR);
    }

    // ===== Test 4: @Transactional bulkImport =====

    @Test
    void testBulkImportSuccess() throws SQLException {
        printHeader("Bulk Import - Success");

        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        repository = new ProductJdbcRepository(connection);
        productService = new ProductService(repository);

        List<Product> products = new ArrayList<>();
        products.add(new Product("P001", "Laptop", "Electronics", 999.99, 50));
        products.add(new Product("P002", "Smartphone", "Electronics", 699.99, 100));
        products.add(new Product("P003", "Headphones", "Electronics", 89.99, 200));

        System.out.println("Importing " + products.size() + " products:");
        for (Product p : products) {
            System.out.println("  " + p);
        }

        productService.bulkImport(products);
        List<Product> allProducts = productService.findAll();

        System.out.println();
        System.out.println("All products after import:");
        for (Product p : allProducts) {
            System.out.println("  " + p);
        }

        assertEquals(3, allProducts.size());
        System.out.println(SEPARATOR);
    }

    @Test
    void testBulkImportRollbackOnBlankId() throws SQLException {
        printHeader("Bulk Import - Rollback on Blank ID");

        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        repository = new ProductJdbcRepository(connection);
        productService = new ProductService(repository);

        List<Product> products = new ArrayList<>();
        products.add(new Product("P001", "Laptop", "Electronics", 999.99, 50));
        products.add(new Product("", "Invalid Product", "Electronics", 699.99, 100));
        products.add(new Product("P003", "Headphones", "Electronics", 89.99, 200));

        System.out.println("Attempting to import products (one has blank ID):");
        for (Product p : products) {
            System.out.println("  " + p);
        }

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.bulkImport(products);
        });

        System.out.println();
        System.out.println("Expected exception thrown: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("Product ID cannot be blank"));

        // Verify no products were saved (rollback)
        List<Product> allProducts = productService.findAll();
        System.out.println("Products in database after failed import: " + allProducts.size());
        assertTrue(allProducts.isEmpty());
        System.out.println(SEPARATOR);
    }

    @Test
    void testBulkImportRollbackOnNegativePrice() throws SQLException {
        printHeader("Bulk Import - Rollback on Negative Price");

        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        repository = new ProductJdbcRepository(connection);
        productService = new ProductService(repository);

        List<Product> products = new ArrayList<>();
        products.add(new Product("P001", "Laptop", "Electronics", 999.99, 50));
        products.add(new Product("P002", "Invalid Product", "Electronics", -10.00, 100));
        products.add(new Product("P003", "Headphones", "Electronics", 89.99, 200));

        System.out.println("Attempting to import products (one has negative price):");
        for (Product p : products) {
            System.out.println("  " + p);
        }

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.bulkImport(products);
        });

        System.out.println();
        System.out.println("Expected exception thrown: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("Product price cannot be negative"));

        // Verify no products were saved (rollback)
        List<Product> allProducts = productService.findAll();
        System.out.println("Products in database after failed import: " + allProducts.size());
        assertTrue(allProducts.isEmpty());
        System.out.println(SEPARATOR);
    }

    // ===== Helper Method =====

    private void testServiceOperations() {
        // Test CRUD operations
        Product product = new Product("P001", "Laptop", "Electronics", 999.99, 50);
        productService.save(product);
        System.out.println("Saved: " + product);

        Product found = productService.findById("P001");
        System.out.println("Found: " + found);
        assertNotNull(found);
        assertEquals("P001", found.getId());

        List<Product> allProducts = productService.findAll();
        System.out.println("All products: " + allProducts.size());
        assertEquals(1, allProducts.size());

        productService.updateStock("P001", 75);
        Product updated = productService.findById("P001");
        System.out.println("Updated stock: " + updated.getStockQuantity());
        assertEquals(75, updated.getStockQuantity());

        productService.deleteById("P001");
        Product deleted = productService.findById("P001");
        System.out.println("After deletion: " + deleted);
        assertNull(deleted);

        System.out.println();
        System.out.println("VERIFICATION: All CRUD operations work with " + repository.getClass().getSimpleName());
        System.out.println(SEPARATOR);
    }
}