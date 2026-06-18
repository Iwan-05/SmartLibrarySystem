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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired UserRepository userRepository;
    @Autowired BookRepository bookRepository;
    @Autowired LoanRepository loanRepository;

    private double totalAvgValue = 0;

    // ==========================================
    // CORE DASHBOARD OVERVIEW MAPPING
    // ==========================================
    @GetMapping("")
    public String showAdmin(Model model, Principal principal) {
        totalAvgValue = 0;
        model.addAttribute("activeSection", "dashboard");

        // Safe check for User Sessions
        if (principal != null && principal.getName() != null) {
            User currentUser = userRepository.findByUsername(principal.getName());
            model.addAttribute("currentUser", currentUser);
        } else {
            User guest = new User();
            guest.setUsername("Guest Admin");
            model.addAttribute("currentUser", guest);
        }

        // Dashboard aggregations using correct repository methods
        List<Loan> activeLoans = loanRepository.findAllActiveLoansOrderByDueDate();
        model.addAttribute("activeLoanCount", activeLoans != null ? activeLoans.size() : 0);

        List<Book> totalBooks = bookRepository.findAll();
        model.addAttribute("totalBook", totalBooks != null ? totalBooks.size() : 0);

        List<User> totalMembers = userRepository.findAllByRole("STUDENT");
        model.addAttribute("totalMember", totalMembers != null ? totalMembers.size() : 0);

        if (totalBooks != null && !totalBooks.isEmpty()) {
            for (Book book : totalBooks) {
                totalAvgValue += book.getAvg_rating();
            }
            double avgValue = totalAvgValue / totalBooks.size();
            model.addAttribute("avgBookRating", String.format("%.2f", avgValue));
        } else {
            model.addAttribute("avgBookRating", "0.00");
        }

        // Slice lists for Recent items widget
        if (totalBooks != null && !totalBooks.isEmpty()) {
            List<Book> recentBooks = totalBooks.size() > 3
                    ? totalBooks.subList(totalBooks.size() - 3, totalBooks.size())
                    : totalBooks;
            model.addAttribute("recentBooks", recentBooks);
        }

        if (activeLoans != null && !activeLoans.isEmpty()) {
            List<Loan> recentLoans = activeLoans.size() > 3
                    ? activeLoans.subList(0, 3)
                    : activeLoans;
            model.addAttribute("recentLoans", recentLoans);
        }

        return "admin";
    }

    // ==========================================
    // BOOKS MANAGEMENT
    // ==========================================
    @GetMapping("/books")
    public String showBooks(Model model, Principal principal) {
        model.addAttribute("activeSection", "books");
        model.addAttribute("allBooks", bookRepository.findAll());
        if (!model.containsAttribute("bookForm")) {
            model.addAttribute("bookForm", new Book());
        }

        if (principal != null && principal.getName() != null) {
            User currentUser = userRepository.findByUsername(principal.getName());
            model.addAttribute("currentUser", currentUser);
        } else {
            User guest = new User();
            guest.setUsername("Guest Admin");
            model.addAttribute("currentUser", guest);
        }

        return "admin-books";
    }

    @GetMapping("/books/edit/{id}")
    public String editBook(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) { // Type changed to Long
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            redirectAttributes.addFlashAttribute("bookForm", book.get());
        }
        return "redirect:/admin/books";
    }

    @PostMapping("/books/save")
    public String saveOrUpdateBook(@ModelAttribute("bookForm") Book book, RedirectAttributes redirectAttributes) {
        bookRepository.save(book);
        redirectAttributes.addFlashAttribute("successMessage", "Book catalog updated!");
        return "redirect:/admin/books";
    }

    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) { // Type changed to Long
        try {
            bookRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Book record removed.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("successMessage", "Cannot delete: book has active transactions.");
        }
        return "redirect:/admin/books";
    }

    // ==========================================
    // LOANS MANAGEMENT
    // ==========================================
    @GetMapping("/loans")
    public String showLoans(Model model, Principal principal) {
        model.addAttribute("activeSection", "loans");
        model.addAttribute("allLoans", loanRepository.findAll());
        model.addAttribute("booksList", bookRepository.findAll());
        model.addAttribute("membersList", userRepository.findAll());

        if (!model.containsAttribute("loanForm")) {
            model.addAttribute("loanForm", new Loan());
        }

        if (principal != null && principal.getName() != null) {
            User currentUser = userRepository.findByUsername(principal.getName());
            model.addAttribute("currentUser", currentUser);
        } else {
            User guest = new User();
            guest.setUsername("Guest Admin");
            model.addAttribute("currentUser", guest);
        }

        return "admin-loans";
    }

    @GetMapping("/loans/edit/{id}")
    public String editLoan(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) { // Type changed to Long
        Optional<Loan> loan = loanRepository.findById(id);
        if (loan.isPresent()) {
            redirectAttributes.addFlashAttribute("loanForm", loan.get());
        }
        return "redirect:/admin/loans";
    }

    @PostMapping("/loans/save")
    public String saveLoan(@ModelAttribute("loanForm") Loan loan,
                           @RequestParam(value = "inputDueDate", required = false) String inputDueDate,
                           @RequestParam("selectedStatus") String selectedStatus, // <-- Capture the dropdown value manually
                           RedirectAttributes redirectAttributes) {

        // 1. Handle due date assignment conversion safely
        if (inputDueDate != null && !inputDueDate.isEmpty()) {
            loan.setDue_date(LocalDate.parse(inputDueDate).atStartOfDay());
        }

        // 2. Default initialize values for completely new records
        if (loan.getLoan_id() == null) {
            loan.setBorrow_date(LocalDateTime.now());
            loan.setFine_amount(0.0);
        }

        // 3. Instead of checking loan.getStatus(), check the incoming form string directly
        if ("Returned".equals(selectedStatus)) {
            // Only stamp the date if it wasn't already marked as returned previously
            if (loan.getReturn_date() == null) {
                loan.setReturn_date(LocalDateTime.now());
            }
        } else {
            // If the admin switches a returned book back to active/overdue, wipe out the return date timestamp
            loan.setReturn_date(null);
        }

        loanRepository.save(loan);
        redirectAttributes.addFlashAttribute("successMessage", "Loan log updated successfully!");
        return "redirect:/admin/loans";
    }

    @GetMapping("/loans/delete/{id}")
    public String deleteLoan(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) { // Type changed to Long
        loanRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Loan file dropped.");
        return "redirect:/admin/loans";
    }

    // ==========================================
    // MEMBERS MANAGEMENT
    // ==========================================
    @GetMapping("/members")
    public String showMembers(Model model, Principal principal) {
        model.addAttribute("activeSection", "members");
        model.addAttribute("allMembers", userRepository.findAll());

        if (!model.containsAttribute("memberForm")) {
            model.addAttribute("memberForm", new User());
        }

        if (principal != null && principal.getName() != null) {
            User currentUser = userRepository.findByUsername(principal.getName());
            model.addAttribute("currentUser", currentUser);
        } else {
            User guest = new User();
            guest.setUsername("Guest Admin");
            model.addAttribute("currentUser", guest);
        }

        return "admin-members";
    }

    @GetMapping("/members/edit/{id}")
    public String editMember(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        // Change findByUsername to findById
        Optional<User> member = userRepository.findById(Integer.parseInt(id));
        if (member.isPresent()) {
            redirectAttributes.addFlashAttribute("memberForm", member.get());
            redirectAttributes.addFlashAttribute("isEditMode", true);
        }
        return "redirect:/admin/members";
    }

    @PostMapping("/members/save")
    public String saveMember(@ModelAttribute("memberForm") User member,
                             @RequestParam(value = "isEdit", required = false) String isEdit, // Changed to String to safely handle HTML input value
                             RedirectAttributes redirectAttributes) {
        if ("true".equals(isEdit)) {
            // Safe fallback fetch by ID to preserve original password if not altered
            Optional<User> existingUser = userRepository.findById(Integer.parseInt(member.getUser_id()));
            if(existingUser.isPresent()) {
                member.setPassword(existingUser.get().getPassword());
            }
        }
        userRepository.save(member);
        redirectAttributes.addFlashAttribute("successMessage", "Member profile saved.");
        return "redirect:/admin/members";
    }

    @GetMapping("/members/delete/{id}")
    public String deleteMember(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        Optional<User> user = userRepository.findById(Integer.parseInt(id));
        if(user.isPresent()) {
            try {
                userRepository.delete(user.get());
                redirectAttributes.addFlashAttribute("successMessage", "User authorization revoked.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("successMessage", "Cannot drop member: user has outstanding active database associations.");
            }
        }
        return "redirect:/admin/members";
    }

    @GetMapping("/settings")
    public String showSettings(Model model) {
        model.addAttribute("activeSection", "settings");
        return "admin-settings";
    }
}