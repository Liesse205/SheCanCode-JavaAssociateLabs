package com.example.itemmanager.repository;

import com.example.itemmanager.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // Find items belonging to a specific user
    List<Item> findByOwnerEmail(String email);
}