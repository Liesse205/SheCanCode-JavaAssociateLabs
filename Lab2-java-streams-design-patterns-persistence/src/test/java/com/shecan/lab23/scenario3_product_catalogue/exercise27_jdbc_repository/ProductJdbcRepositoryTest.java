package com.shecan.lab23.scenario3_product_catalogue.exercise27_jdbc_repository;

import com.shecan.lab23.scenario3_product_catalogue.model.Product;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ProductJdbcRepositoryTest {

    private static final String JDBC_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";
    private static final String SEPARATOR = "============================================================";

    private Connection connection;
    private ProductJdbcRepository repository;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        repository = new ProductJdbcRepository(connection);
        repository.clearTable();
        System.out.println();
        System.out.println(SEPARATOR);
        System.out.println("SETUP: New database connection created");
        System.out.println(SEPARATOR);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("TEARDOWN: Database connection closed");
        }
    }

    private void printHeader(String title) {
        System.out.println();
        System.out.println(SEPARATOR);
        System.out.println("TEST: " + title);
        System.out.println(SEPARATOR);
    }

    @Test
    void testSaveAndFindById() {
        printHeader("Save and Find By Id");
        
        Product product = new Product("P001", "Laptop", "Electronics", 999.99, 50);
        repository.save(product);
        
        Product found = repository.findById("P001");
        
        System.out.println("Saved: " + product);
        System.out.println("Found: " + found);
        
        assertNotNull(found);
        assertEquals("P001", found.getId());
        assertEquals("Laptop", found.getName());
        assertEquals("Electronics", found.getCategory());
        assertEquals(999.99, found.getPrice(), 0.001);
        assertEquals(50, found.getStockQuantity());
        System.out.println(SEPARATOR);
    }

    @Test
    void testFindAll() {
        printHeader("Find All Products");
        
        List<Product> products = new ArrayList<>();
        products.add(new Product("P001", "Laptop", "Electronics", 999.99, 50));
        products.add(new Product("P002", "Smartphone", "Electronics", 699.99, 100));
        products.add(new Product("P003", "Headphones", "Electronics", 89.99, 200));
        
        for (Product p : products) {
            repository.save(p);
        }
        
        List<Product> allProducts = repository.findAll();
        System.out.println("Products in database:");
        for (Product p : allProducts) {
            System.out.println("  " + p);
        }
        
        assertEquals(3, allProducts.size());
        System.out.println(SEPARATOR);
    }

    @Test
    void testDeleteById() {
        printHeader("Delete By Id");
        
        Product product = new Product("P001", "Laptop", "Electronics", 999.99, 50);
        repository.save(product);
        assertNotNull(repository.findById("P001"));
        
        repository.deleteById("P001");
        Product deleted = repository.findById("P001");
        
        System.out.println("Product after deletion: " + deleted);
        assertNull(deleted);
        System.out.println(SEPARATOR);
    }

    @Test
    void testFindByIdNotFound() {
        printHeader("Find By Id - Not Found");
        
        Product found = repository.findById("NONEXISTENT");
        System.out.println("Searching for nonexistent product: " + found);
        assertNull(found);
        System.out.println(SEPARATOR);
    }

    @Test
    void testUpdateStock() {
        printHeader("Update Stock");
        
        Product product = new Product("P001", "Laptop", "Electronics", 999.99, 50);
        repository.save(product);
        
        System.out.println("Before update: " + repository.findById("P001"));
        repository.updateStock("P001", 75);
        Product updated = repository.findById("P001");
        System.out.println("After update: " + updated);
        
        assertEquals(75, updated.getStockQuantity());
        System.out.println(SEPARATOR);
    }

    @Test
    void testTransferStockSuccess() {
        printHeader("Transfer Stock - Success");
        
        Product product1 = new Product("P001", "Laptop", "Electronics", 999.99, 50);
        Product product2 = new Product("P002", "Smartphone", "Electronics", 699.99, 30);
        repository.save(product1);
        repository.save(product2);
        
        System.out.println("Before transfer:");
        System.out.println("  " + repository.findById("P001"));
        System.out.println("  " + repository.findById("P002"));
        
        repository.transferStock("P001", "P002", 10);
        
        Product from = repository.findById("P001");
        Product to = repository.findById("P002");
        
        System.out.println();
        System.out.println("After transfer:");
        System.out.println("  " + from);
        System.out.println("  " + to);
        
        assertEquals(40, from.getStockQuantity());
        assertEquals(40, to.getStockQuantity());
        System.out.println(SEPARATOR);
    }

    @Test
    void testTransferStockInsufficientStock() {
        printHeader("Transfer Stock - Insufficient Stock");
        
        Product product1 = new Product("P001", "Laptop", "Electronics", 999.99, 5);
        Product product2 = new Product("P002", "Smartphone", "Electronics", 699.99, 30);
        repository.save(product1);
        repository.save(product2);
        
        System.out.println("Before transfer:");
        System.out.println("  " + repository.findById("P001"));
        System.out.println("  " + repository.findById("P002"));
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            repository.transferStock("P001", "P002", 10);
        });
        
        System.out.println();
        System.out.println("Expected exception: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("Insufficient stock"));
        
        // Verify stock unchanged
        Product from = repository.findById("P001");
        Product to = repository.findById("P002");
        System.out.println();
        System.out.println("After failed transfer:");
        System.out.println("  " + from);
        System.out.println("  " + to);
        assertEquals(5, from.getStockQuantity());
        assertEquals(30, to.getStockQuantity());
        System.out.println(SEPARATOR);
    }

    @Test
    void testTransferStockProductNotFound() {
        printHeader("Transfer Stock - Product Not Found");
        
        Product product1 = new Product("P001", "Laptop", "Electronics", 999.99, 50);
        repository.save(product1);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            repository.transferStock("P001", "NONEXISTENT", 10);
        });
        
        System.out.println("Expected exception: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("Target product not found"));
        System.out.println(SEPARATOR);
    }

    @Test
    void testConcurrentTransfers() throws InterruptedException {
        printHeader("Concurrent Transfers - Stock Conservation");
        
        // Insert 50 products with initial stock 100 each
        int numberOfProducts = 50;
        int initialStock = 100;
        
        for (int i = 1; i <= numberOfProducts; i++) {
            String id = String.format("P%03d", i);
            Product product = new Product(id, "Product " + i, "Category", 99.99, initialStock);
            repository.save(product);
        }
        
        int totalInitialStock = numberOfProducts * initialStock;
        System.out.println("Initial total stock: " + totalInitialStock);
        
        // Perform 10 concurrent transfers
        int numberOfTransfers = 10;
        ExecutorService executor = Executors.newFixedThreadPool(5);
        AtomicInteger successfulTransfers = new AtomicInteger(0);
        AtomicInteger failedTransfers = new AtomicInteger(0);
        
        for (int i = 0; i < numberOfTransfers; i++) {
            executor.submit(() -> {
                try {
                    // Randomly select two different products
                    int fromIndex = (int) (Math.random() * numberOfProducts) + 1;
                    int toIndex;
                    do {
                        toIndex = (int) (Math.random() * numberOfProducts) + 1;
                    } while (toIndex == fromIndex);
                    
                    String fromId = String.format("P%03d", fromIndex);
                    String toId = String.format("P%03d", toIndex);
                    int quantity = (int) (Math.random() * 10) + 1;
                    
                    repository.transferStock(fromId, toId, quantity);
                    successfulTransfers.incrementAndGet();
                } catch (Exception e) {
                    failedTransfers.incrementAndGet();
                    // This is expected if random transfer tries to move more stock than available
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        
        // Verify total stock is conserved
        List<Product> allProducts = repository.findAll();
        int totalFinalStock = allProducts.stream()
                .mapToInt(Product::getStockQuantity)
                .sum();
        
        System.out.println();
        System.out.println("Transfer results:");
        System.out.println("  Successful transfers: " + successfulTransfers.get());
        System.out.println("  Failed transfers: " + failedTransfers.get());
        System.out.println("  Final total stock: " + totalFinalStock);
        System.out.println("  Initial total stock: " + totalInitialStock);
        
        assertEquals(totalInitialStock, totalFinalStock, "Total stock must be conserved");
        System.out.println(SEPARATOR);
    }
}