package com.example.itemmanager.controller;

import com.example.itemmanager.model.Item;
import com.example.itemmanager.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PageController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            return "redirect:/items";
        }
        return "home";
    }

    @GetMapping("/items")
    public String listItems(Model model, @AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        model.addAttribute("items", itemRepository.findByOwnerEmail(email));
        model.addAttribute("userName", principal.getAttribute("name"));
        model.addAttribute("userEmail", email);
        model.addAttribute("userAvatar", principal.getAttribute("avatar_url") != null
                ? principal.getAttribute("avatar_url")
                : principal.getAttribute("picture"));
        return "items";
    }

    @GetMapping("/items/new")
    public String showCreateForm(Model model) {
        model.addAttribute("item", new Item());
        model.addAttribute("isEdit", false);
        return "item-form";
    }

    @GetMapping("/items/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Item item = itemRepository.findById(id).orElseThrow();
        model.addAttribute("item", item);
        model.addAttribute("isEdit", true);
        return "item-form";
    }

    @PostMapping("/items/save")
    public String saveItem(@ModelAttribute Item item, @AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");

        if (item.getId() == null) {
            // New item - set the owner
            item.setOwnerEmail(email);
        } else {
            // Existing item - preserve owner
            Item existing = itemRepository.findById(item.getId()).orElseThrow();
            item.setOwnerEmail(existing.getOwnerEmail());
        }

        itemRepository.save(item);
        return "redirect:/items";
    }

    @GetMapping("/items/delete/{id}")
    public String deleteItem(@PathVariable Long id) {
        itemRepository.deleteById(id);
        return "redirect:/items";
    }
}