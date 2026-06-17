package com.csc3402.smartlibrarysystem.controller;

import com.csc3402.smartlibrarysystem.model.Book;
import com.csc3402.smartlibrarysystem.model.User;
import com.csc3402.smartlibrarysystem.repository.BookRepository;
import com.csc3402.smartlibrarysystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private BookRepository bookRepository;

    //@Autowired
    //private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String showDashboard(@RequestParam(name = "search", required = false) String search, Model model) {

        List<Book> filteredBooks;

        if (search != null && !search.trim().isEmpty()) {
            filteredBooks = bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(search, search);
        } else {

            filteredBooks = bookRepository.findAll();
        }

        model.addAttribute("books", filteredBooks);

        //User currentUser = userRepository.findByUsername(principal.getName());
        //model.addAttribute("currentUser", currentUser);

        return "dashboard";
    }

    //to ensure the dashboard can live search
    @GetMapping("/dashboard/search")
    @ResponseBody
    public List<Book> instantSearch(@RequestParam(name = "keyword") String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return bookRepository.findAll();
        }
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    //***********(for usage of category seacrh bar)***********
    @GetMapping("/category/filter")
    @ResponseBody
    public List<Book> filterByGenreAndSearch(@RequestParam String genre,
                                             @RequestParam(required = false) String search) {

        // 1. If the user isn't searching anything, just give them the genre rows directly!
        if (search == null || search.trim().isEmpty()) {
            if (genre.equalsIgnoreCase("All Books")) {
                return bookRepository.findAll();
            }
            return bookRepository.findByGenre(genre);
        }

        // 2. If they ARE typing a keyword, handle the filtering safely
        String keyword = search.trim().toLowerCase();

        if (genre.equalsIgnoreCase("All Books")) {
            return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
        }

        // Grab the genre dataset and filter it safely without crashing on null database fields
        List<Book> booksInGenre = bookRepository.findByGenre(genre);
        return booksInGenre.stream()
                .filter(b -> {
                    boolean matchesTitle = b.getTitle() != null && b.getTitle().toLowerCase().contains(keyword);
                    boolean matchesAuthor = b.getAuthor() != null && b.getAuthor().toLowerCase().contains(keyword);
                    return matchesTitle || matchesAuthor;
                })
                .toList();
    }

    @GetMapping("/category")
    public String showCategory(Model model) {
        model.addAttribute("books", bookRepository.findAll());
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