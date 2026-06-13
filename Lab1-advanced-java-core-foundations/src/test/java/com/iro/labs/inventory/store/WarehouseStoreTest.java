package com.iro.labs.inventory.store;
import com.iro.labs.inventory.model.Category;
import com.iro.labs.inventory.model.Electronics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class WarehouseStoreTest {
    private WarehouseStore<Electronics> store;
    
    @BeforeEach
    void setUp() { store = new WarehouseStore<>(); }
    
    @Test
    void testAddItem() {
        Electronics laptop = new Electronics("Laptop", Category.COMPUTERS, 999.99, 24);
        store.addItem(laptop);
        assertEquals(1, store.size());
        assertFalse(store.isEmpty());
    }
    
    @Test
    void testAddNullItem_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> store.addItem(null));
    }
    
    @Test
    void testRemoveItem() {
        Electronics laptop = new Electronics("Laptop", Category.COMPUTERS, 999.99, 24);
        store.addItem(laptop);
        String id = laptop.getId();
        assertTrue(store.removeItem(id));
        assertEquals(0, store.size());
    }
    
    @Test
    void testFindByCategory() {
        store.addItem(new Electronics("Laptop", Category.COMPUTERS, 999.99, 24));
        store.addItem(new Electronics("Mouse", Category.COMPUTERS, 29.99, 12));
        store.addItem(new Electronics("Phone", Category.MOBILE, 599.99, 12));
        assertEquals(2, store.findByCategory(Category.COMPUTERS).size());
        assertEquals(1, store.findByCategory(Category.MOBILE).size());
    }
    
    @Test
    void testFindById() {
        Electronics laptop = new Electronics("Laptop", Category.COMPUTERS, 999.99, 24);
        store.addItem(laptop);
        Electronics found = store.findById(laptop.getId());
        assertNotNull(found);
        assertEquals(laptop.getName(), found.getName());
    }
}