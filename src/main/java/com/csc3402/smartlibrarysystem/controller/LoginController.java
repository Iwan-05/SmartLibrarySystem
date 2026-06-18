package com.csc3402.smartlibrarysystem.controller;

import com.csc3402.smartlibrarysystem.model.User;
import com.csc3402.smartlibrarysystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/login")
    public String showLoginPage() {
        // This exact string must match the name of your HTML file (without the .html extension)
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    // Routes the user to the new registration page
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               @RequestParam String faculty,
                               @RequestParam String role,
                               Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "register";
        }

        if (userRepository.findByUsername(username) != null) {
            model.addAttribute("error", "Username already taken.");
            return "register";
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRole(role);
        newUser.setFaculty(faculty);
        newUser.setFine(0.0);

        userRepository.save(newUser);

        return "redirect:/login";
    }
}