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
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    LoanRepository loanRepository;

    double totalAvgValue=0;




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
    @GetMapping("")
    public String showAdmin(Model model, Principal principal) {
        totalAvgValue=0;
        User currentUser = userRepository.findByUsername(principal.getName()); //func nak tarik username ke dashbaord
        model.addAttribute("currentUser", currentUser);

        List<Loan> activeLoans = loanRepository.findAllActiveLoansOrderByDueDate();//to get total loan
        model.addAttribute("activeLoanCount", activeLoans.size());

        List<Book> totalBook = bookRepository.findAll();//total book available
        model.addAttribute("totalBook", totalBook.size());

        List<User> totalMember = userRepository.findAllByRole("STUDENT"); //total student available
        model.addAttribute("totalMember", totalMember.size());

        List<Book> avgBookRating = bookRepository.findAll();//avg rating
        for(int i=0;i< avgBookRating.size();i++){
            totalAvgValue+=avgBookRating.get(i).getAvg_rating();
        }
        double avgValue=totalAvgValue/avgBookRating.size();
        model.addAttribute("avgBookRating", String.format("%.2f",avgValue));

        List<Book> recentBooks = totalBook.size() > 3 //recent book punya
                ? totalBook.subList(totalBook.size() - 3, totalBook.size())
                : totalBook;
        model.addAttribute("recentBooks", recentBooks);

        List<Loan> allActiveLoans = loanRepository.findAllActiveLoansOrderByDueDate();

        List<Loan> recentLoans = allActiveLoans.size() > 3
                ? allActiveLoans.subList(0, 3)
                : allActiveLoans;
        model.addAttribute("recentLoans", recentLoans);
        return "admin";
    }


    // 1. Display the page with the list and an empty form
    @GetMapping("/books") // Kept standard admin routing context
    public String showBooksPage(Model model, Principal principal) {
        model.addAttribute("allBooks", bookRepository.findAll());
        model.addAttribute("bookForm", new Book());

        User currentUser = userRepository.findByUsername(principal.getName()); //func nak tarik username ke dashbaord
        model.addAttribute("currentUser", currentUser);

        // FIXED: Changed from "admin" to "admin-books" to open the correct file!
        return "admin-books";
    }

    // 2. Handle saving a new book OR updating an existing one
    @PostMapping("/books/save")
    public String saveBook(@ModelAttribute("bookForm") Book book, RedirectAttributes redirectAttributes) {
        bookRepository.save(book);
        redirectAttributes.addFlashAttribute("successMessage", "Book catalog registry updated successfully!");
        return "redirect:/admin/books";
    }

    // 3. Handle clicking the "Edit" button
    @GetMapping("/books/edit/{id}")
    public String editBookForm(@PathVariable("id") Long id, Model model) {
        Book existingBook = bookRepository.findById(id).orElse(new Book());

        model.addAttribute("allBooks", bookRepository.findAll());
        model.addAttribute("bookForm", existingBook);

        // FIXED: Changed from "admin" to "admin-books" to stay on the correct screen while editing!
        return "admin-books";
    }

    // 4. Handle clicking the "Del" button
    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        bookRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Book deleted successfully!");
        return "redirect:/admin/books";
    }
}
