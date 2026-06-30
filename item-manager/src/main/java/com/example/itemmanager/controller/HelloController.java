package com.example.itemmanager.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(@AuthenticationPrincipal OAuth2User principal) {
        String name = principal.getAttribute("name") != null
                ? principal.getAttribute("name")
                : principal.getAttribute("login");
        return "Hello, " + name + "! You are logged in. Now you can use the API at /api/items.";
    }
}