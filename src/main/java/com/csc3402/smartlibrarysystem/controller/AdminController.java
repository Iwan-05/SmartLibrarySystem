package com.csc3402.smartlibrarysystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String showOverview(Model model) {
        model.addAttribute("activeSection", "overview");
        return "admin";
    }

    @GetMapping("/books")
    public String showBooks(Model model) {
        model.addAttribute("activeSection", "books");
        return "admin-books";
    }

    @GetMapping("/loans")
    public String showLoans(Model model) {
        model.addAttribute("activeSection", "loans");
        return "admin-loans";
    }

    @GetMapping("/members")
    public String showMembers(Model model) {
        model.addAttribute("activeSection", "members");
        return "admin-members";
    }

    @GetMapping("/settings")
    public String showSettings(Model model) {
        model.addAttribute("activeSection", "settings");
        return "admin-settings";
    }
}
