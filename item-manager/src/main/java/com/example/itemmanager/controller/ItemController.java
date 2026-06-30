package com.example.itemmanager.controller;

import com.example.itemmanager.model.Item;
import com.example.itemmanager.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    // CREATE
    @PostMapping
    public Item createItem(@RequestBody Item item, @AuthenticationPrincipal OAuth2User principal) {
        String userEmail = principal.getAttribute("email");
        item.setOwnerEmail(userEmail);
        return itemRepository.save(item);
    }

    // READ ALL (User's own items)
    @GetMapping
    public List<Item> getMyItems(@AuthenticationPrincipal OAuth2User principal) {
        String userEmail = principal.getAttribute("email");
        return itemRepository.findByOwnerEmail(userEmail);
    }

    // READ ONE
    @GetMapping("/{id}")
    public Item getItemById(@PathVariable Long id, @AuthenticationPrincipal OAuth2User principal) {
        // For simplicity, assuming item exists and belongs to user.
        // You should add error handling (e.g., throw exception if not found or not owner).
        return itemRepository.findById(id).orElseThrow();
    }

    // UPDATE
    @PutMapping("/{id}")
    public Item updateItem(@PathVariable Long id, @RequestBody Item updatedItem,
                           @AuthenticationPrincipal OAuth2User principal) {
        Item existingItem = itemRepository.findById(id).orElseThrow();
        // Check if the item belongs to the logged-in user (optional but recommended)
        if (!existingItem.getOwnerEmail().equals(principal.getAttribute("email"))) {
            throw new RuntimeException("Unauthorized");
        }
        existingItem.setName(updatedItem.getName());
        existingItem.setDescription(updatedItem.getDescription());
        return itemRepository.save(existingItem);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id, @AuthenticationPrincipal OAuth2User principal) {
        Item item = itemRepository.findById(id).orElseThrow();
        if (!item.getOwnerEmail().equals(principal.getAttribute("email"))) {
            throw new RuntimeException("Unauthorized");
        }
        itemRepository.deleteById(id);
    }
}