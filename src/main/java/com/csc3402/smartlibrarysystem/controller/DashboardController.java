package com.csc3402.smartlibrarysystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "dashboard";
    }

    @GetMapping("/category")
    public String showCategory() {
        return "category";
    }

    @GetMapping("/mylibrary")
    public String showMyLibrary() {
        return "mylibrary";
    }

    @GetMapping("/profile")
    public String showProfile() {
        return "profile";
    }
}