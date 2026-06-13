package com.iro.labs.inventory.model;

public class Electronics extends Product {
    private int warrantyMonths;
    
    public Electronics(String name, Category category, Double price, int warrantyMonths) {
        super(name, category, price);
        this.warrantyMonths = warrantyMonths;
    }
    
    public Electronics(String id, String name, Category category, Double price, int warrantyMonths) {
        super(id, name, category, price);
        this.warrantyMonths = warrantyMonths;
    }
    
    public int getWarrantyMonths() { return warrantyMonths; }
    public void setWarrantyMonths(int warrantyMonths) { this.warrantyMonths = warrantyMonths; }
    
    @Override
    public String toString() {
        return String.format("Electronics{id='%s', name='%s', category=%s, price=%s, warranty=%d months}",
                            getId(), getName(), getCategory(), getPrice(), warrantyMonths);
    }
}
