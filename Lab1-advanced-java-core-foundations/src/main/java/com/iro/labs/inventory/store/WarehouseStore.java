package com.iro.labs.inventory.store;
import com.iro.labs.inventory.model.Category;
import com.iro.labs.inventory.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WarehouseStore<T extends Product> {
    private static final Logger log = LoggerFactory.getLogger(WarehouseStore.class);
    private final List<T> items = new ArrayList<>();
    
    public T addItem(T item) {
        if (item == null) throw new IllegalArgumentException("Cannot add null item");
        items.add(item);
        log.debug("Added item: {}", item.getId());
        return item;
    }
    
    public boolean removeItem(String id) {
        boolean removed = items.removeIf(item -> item.getId().equals(id));
        if (removed) log.debug("Removed item with id: {}", id);
        return removed;
    }
    
    public List<T> findByCategory(Category category) {
        return items.stream().filter(item -> item.getCategory() == category).collect(Collectors.toList());
    }
    
    public T findById(String id) {
        return items.stream().filter(item -> item.getId().equals(id)).findFirst().orElse(null);
    }
    
    public List<T> getAllItems() { return new ArrayList<>(items); }
    public int size() { return items.size(); }
    public boolean isEmpty() { return items.isEmpty(); }
}
