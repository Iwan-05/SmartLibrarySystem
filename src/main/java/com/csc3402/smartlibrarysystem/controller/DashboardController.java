package com.csc3402.smartlibrarysystem.controller;

import com.csc3402.smartlibrarysystem.model.Book;
import com.csc3402.smartlibrarysystem.model.Loan;
import com.csc3402.smartlibrarysystem.model.User;
import com.csc3402.smartlibrarysystem.repository.BookRepository;
import com.csc3402.smartlibrarysystem.repository.LoanRepository;
import com.csc3402.smartlibrarysystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanRepository loanRepository;

    @GetMapping("/dashboard")
    public String showDashboard(@RequestParam(name = "search", required = false) String search,
                                Model model,
                                Principal principal) {
        List<Book> filteredBooks;

        if (search != null && !search.trim().isEmpty()) {
            filteredBooks = bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(search, search);
        } else {

            filteredBooks = bookRepository.findAll();
        }

        model.addAttribute("books", filteredBooks);

        User currentUser = userRepository.findByUsername(principal.getName()); //func nak tarik username ke dashbaord
        model.addAttribute("currentUser", currentUser);

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
    public String showCategory(Model model,Principal principal) {
        model.addAttribute("books", bookRepository.findAll());

        User currentUser = userRepository.findByUsername(principal.getName()); //func nak tarik username ke dashbaord
        model.addAttribute("currentUser", currentUser);

        return "category";
    }

    @GetMapping("/mylibrary")
    public String showMyLibrary(Model model,Principal principal) {

        User currentUser = userRepository.findByUsername(principal.getName()); //func nak tarik username ke dashbaord
        model.addAttribute("currentUser", currentUser);

        List<Loan> activeLoans = loanRepository.findActiveLoansByUserId(currentUser.getUser_id());
        model.addAttribute("loans", activeLoans);
        return "mylibrary";
    }

    @GetMapping("/profile")
    public String showProfile(Model model,Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName()); //func nak tarik username ke dashbaord
        model.addAttribute("currentUser", currentUser);

        List<Loan> activeLoans = loanRepository.findActiveLoansByUserId(currentUser.getUser_id());
        model.addAttribute("activeLoanCount", activeLoans.size());
        List<Loan> pastLoans = loanRepository.findPastLoansByUserId(currentUser.getUser_id());
        model.addAttribute("pastLoanCount", pastLoans.size());
       return "profile";
    }

    @PostMapping("/returnBook")
    public String returnBook(@RequestParam("loanId") Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElse(null);

        if (loan != null) {
            loan.setReturn_date(LocalDateTime.now());
            loanRepository.save(loan);
        }

        return "redirect:/mylibrary";
    }


    @GetMapping("/")
    public String homeRedirect(HttpServletRequest request) {
        // Checks if the logged-in user has the LIBRARIAN role
        if (request.isUserInRole("LIBRARIAN")) {
            // Sends the admin to the root admin page (admin.html)
            return "redirect:/admin";
        }

        // If student, send them to the regular dashboard
        return "redirect:/dashboard";
    }
}