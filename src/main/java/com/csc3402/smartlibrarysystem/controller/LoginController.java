package com.csc3402.smartlibrarysystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        // This exact string must match the name of your HTML file (without the .html extension)
        return "login";
    }

    // Routes the user to the new registration page
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }
}