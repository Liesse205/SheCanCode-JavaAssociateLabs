package com.iro.labs.inventory.demo;
import com.iro.labs.inventory.comparator.ProductComparator;
import com.iro.labs.inventory.model.Category;
import com.iro.labs.inventory.model.Electronics;
import com.iro.labs.inventory.store.WarehouseStore;

public class MainDemo {
    public static void main(String[] args) {
        System.out.println("=== Lab 1.1: Generic Warehouse Store ===\n");
        WarehouseStore<Electronics> techStore = new WarehouseStore<>();
        System.out.println("Adding products...");
        techStore.addItem(new Electronics("Gaming Laptop", Category.COMPUTERS, 1299.99, 24));
        techStore.addItem(new Electronics("Wireless Mouse", Category.COMPUTERS, 49.99, 12));
        techStore.addItem(new Electronics("Noise Cancelling Headphones", Category.AUDIO, 199.99, 18));
        techStore.addItem(new Electronics("Smartphone", Category.MOBILE, 899.99, 12));
        System.out.println("Total items: " + techStore.size());
        System.out.println("\n--- Products in COMPUTERS category ---");
        techStore.findByCategory(Category.COMPUTERS).forEach(System.out::println);
        System.out.println("\n--- All products sorted by Category ASC, Price DESC ---");
        var sorted = techStore.getAllItems();
        sorted.sort(ProductComparator.BY_CATEGORY_THEN_PRICE_DESC);
        sorted.forEach(System.out::println);
        System.out.println("\n Lab 1.1 Complete!");
    }
}
