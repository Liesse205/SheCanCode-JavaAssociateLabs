package com.shecan.lab23.scenario3_product_catalogue.exercise27_jdbc_repository;

import com.shecan.lab23.scenario3_product_catalogue.exercise29_repository_service.ProductRepository;
import com.shecan.lab23.scenario3_product_catalogue.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductJdbcRepository implements ProductRepository {
    private final Connection connection;

    public ProductJdbcRepository(Connection connection) {
        this.connection = connection;
        createTable();
    }

    private void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS products (
                id VARCHAR(50) PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                category VARCHAR(50),
                price DECIMAL(10,2),
                stock_quantity INT DEFAULT 0
            )
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create products table", e);
        }
    }

    @Override
    public void save(Product product) {
        String sql = "INSERT INTO products (id, name, category, price, stock_quantity) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, product.getId());
            pstmt.setString(2, product.getName());
            pstmt.setString(3, product.getCategory());
            pstmt.setDouble(4, product.getPrice());
            pstmt.setInt(5, product.getStockQuantity());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save product: " + product.getId(), e);
        }
    }

    @Override
    public Product findById(String id) {
        String sql = "SELECT id, name, category, price, stock_quantity FROM products WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToProduct(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find product by id: " + id, e);
        }
    }

    @Override
    public List<Product> findAll() {
        String sql = "SELECT id, name, category, price, stock_quantity FROM products";
        List<Product> products = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all products", e);
        }
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete product by id: " + id, e);
        }
    }

    @Override
    public void updateStock(String productId, int newStock) {
        String sql = "UPDATE products SET stock_quantity = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newStock);
            pstmt.setString(2, productId);
            int updated = pstmt.executeUpdate();
            if (updated == 0) {
                throw new RuntimeException("Product not found: " + productId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update stock for product: " + productId, e);
        }
    }

    @Override
    public void transferStock(String fromId, String toId, int quantity) {
        try {
            connection.setAutoCommit(false);

            String lockSql = "SELECT stock_quantity FROM products WHERE id = ? FOR UPDATE";
            
            int fromStock;
            int toStock;
            
            try (PreparedStatement pstmt = connection.prepareStatement(lockSql)) {
                pstmt.setString(1, fromId);
                ResultSet rs = pstmt.executeQuery();
                if (!rs.next()) {
                    throw new RuntimeException("Source product not found: " + fromId);
                }
                fromStock = rs.getInt("stock_quantity");
            }
            
            try (PreparedStatement pstmt = connection.prepareStatement(lockSql)) {
                pstmt.setString(1, toId);
                ResultSet rs = pstmt.executeQuery();
                if (!rs.next()) {
                    throw new RuntimeException("Target product not found: " + toId);
                }
                toStock = rs.getInt("stock_quantity");
            }

            if (fromStock < quantity) {
                throw new RuntimeException("Insufficient stock for product: " + fromId + 
                    " (available: " + fromStock + ", requested: " + quantity + ")");
            }

            String updateSql = "UPDATE products SET stock_quantity = ? WHERE id = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(updateSql)) {
                pstmt.setInt(1, fromStock - quantity);
                pstmt.setString(2, fromId);
                pstmt.executeUpdate();
            }
            
            try (PreparedStatement pstmt = connection.prepareStatement(updateSql)) {
                pstmt.setInt(1, toStock + quantity);
                pstmt.setString(2, toId);
                pstmt.executeUpdate();
            }

            connection.commit();
            System.out.println("[TRANSFER] Transferred " + quantity + " units from " + fromId + " to " + toId);

        } catch (Exception e) {
            try {
                connection.rollback();
                System.out.println("[TRANSFER] Rolled back transfer from " + fromId + " to " + toId);
            } catch (SQLException rollbackEx) {
                throw new RuntimeException("Failed to rollback transaction", rollbackEx);
            }
            throw new RuntimeException("Transfer failed: " + e.getMessage(), e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to reset auto-commit", e);
            }
        }
    }

    @Override
    public void clearTable() {
        String sql = "DELETE FROM products";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear products table", e);
        }
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getString("id"));
        product.setName(rs.getString("name"));
        product.setCategory(rs.getString("category"));
        product.setPrice(rs.getDouble("price"));
        product.setStockQuantity(rs.getInt("stock_quantity"));
        return product;
    }
}