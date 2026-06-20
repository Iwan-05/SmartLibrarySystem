package com.csc3402.smartlibrarysystem.controller;

import com.csc3402.smartlibrarysystem.model.User;
import com.csc3402.smartlibrarysystem.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;

@Controller
public class LoginController {

    @Autowired
    UserRepository userRepository;

    // Shows the actual login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // --- THE FIXED LOGIN PROCESSOR ---
    // Uses /process-login so Spring Security doesn't hijack it!
    @PostMapping("/process-login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String role,
                               RedirectAttributes redirectAttributes,
                               HttpSession session,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        System.out.println("--- LOGIN ATTEMPT ---");
        System.out.println("User entered: " + username);
        System.out.println("Role selected: " + role);

        User user = userRepository.findByUsername(username);

        // 1. Check if user exists and password matches
        if (user == null || !user.getPassword().equals(password)) {
            System.out.println("Result: Wrong password or user not found.");
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or password.");
            return "redirect:/login";
        }

        // 2. Check if the selected role matches the database role
        if (!user.getRole().equalsIgnoreCase(role)) {
            System.out.println("Result: Wrong role! DB Role is: " + user.getRole());
            redirectAttributes.addFlashAttribute("errorMessage", "Oh no, wrong role! Please select your actual account role.");
            return "redirect:/login";
        }

        // 3. SUCCESS!
        System.out.println("Result: SUCCESS! Logging into Spring Security...");

        // Create the official token
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()))
        );

        // Create an empty context and set our token
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);

        // CRITICAL SPRING SECURITY 6 FIX: Explicitly save the context to the session!
        HttpSessionSecurityContextRepository repository = new HttpSessionSecurityContextRepository();
        repository.saveContext(context, request, response);

        // Keep your manual session attribute for Thymeleaf
        session.setAttribute("currentUser", user);

        // --- NEW TRAFFIC COP: ROUTE BASED ON ROLE ---
        if ("LIBRARIAN".equalsIgnoreCase(user.getRole())) {
            System.out.println("Routing to Librarian Dashboard...");
            return "redirect:/admin"; // Change this to your actual librarian URL!
        } else {
            System.out.println("Routing to Student Dashboard...");
            return "redirect:/dashboard";
        }
    }


    // Shows the registration page
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    // Processes new user registration
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