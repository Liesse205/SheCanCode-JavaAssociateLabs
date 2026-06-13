package com.iro.labs.inventory.model;

public enum Category {
    ELECTRONICS, COMPUTERS, MOBILE, AUDIO, CLOTHING, GROCERY;
    
    public static Category fromString(String text) {
        for (Category c : Category.values()) {
            if (c.name().equalsIgnoreCase(text)) return c;
        }
        throw new IllegalArgumentException("No category found for: " + text);
    }
}
