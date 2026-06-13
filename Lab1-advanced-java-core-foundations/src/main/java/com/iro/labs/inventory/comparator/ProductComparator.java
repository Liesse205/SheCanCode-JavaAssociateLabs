package com.iro.labs.inventory.comparator;
import com.iro.labs.inventory.model.Product;
import java.util.Comparator;

public class ProductComparator {
    public static final Comparator<Product> BY_CATEGORY_THEN_PRICE_DESC =
        Comparator.comparing(Product::getCategory)
                  .thenComparing(Comparator.comparing(Product::getPrice, Comparator.nullsLast(Double::compareTo)).reversed());
    
    public static final Comparator<Product> BY_PRICE_ASC =
        Comparator.comparing(Product::getPrice, Comparator.nullsLast(Double::compareTo));
    
    public static final Comparator<Product> BY_NAME =
        Comparator.comparing(Product::getName, Comparator.nullsLast(String::compareTo));
}
