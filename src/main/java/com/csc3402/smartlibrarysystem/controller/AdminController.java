package com.csc3402.smartlibrarysystem.controller;

import com.csc3402.smartlibrarysystem.BookAPI.GoogleBooksService;
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

        // Change this line to use the new ordered method
        model.addAttribute("allLoans", loanRepository.findAllOrderedByDate());

        if (!model.containsAttribute("loanForm")) {
            model.addAttribute("loanForm", new Loan());
        }

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
    public String saveBook(@ModelAttribute("bookForm") Book bookForm, RedirectAttributes redirectAttributes) {
        // If it's an edit operation (id exists)
        if (bookForm.getBook_id() != null) {
            // 1. Fetch the actual existing record from DB
            Book existingBook = bookRepository.findById(bookForm.getBook_id()).orElse(null);

            if (existingBook != null) {
                // 2. Map ONLY the fields that are present in the form
                existingBook.setTitle(bookForm.getTitle());
                existingBook.setAuthor(bookForm.getAuthor());
                existingBook.setStatus(bookForm.getStatus());
                existingBook.setAvg_rating(bookForm.getAvg_rating());

                // 3. Save the updated database record (unmapped fields stay safe!)
                bookRepository.save(existingBook);
            }
        } else {
            // If it's a completely new book, just save it directly
            bookRepository.save(bookForm);
        }

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
    @Autowired
    private GoogleBooksService googleBooksService;

    @PostMapping("/books/fetch-and-save")
    public String autoFetchBook(@RequestParam("searchQuery") String searchQuery, RedirectAttributes redirectAttributes) {

        // 1. Fetch data from Google API ONLY when the form button is clicked
        Book apiBook = googleBooksService.fetchBookByIsbn(searchQuery);

        if (apiBook != null) {
            // 2. Look up the existing rows using columns you already have
            java.util.Optional<Book> existingBook = bookRepository.findByTitleAndAuthor(apiBook.getTitle(), apiBook.getAuthor());

            if (existingBook.isPresent()) {
                // If the book is found, we block it here. No DB load!
                redirectAttributes.addFlashAttribute("errorMessage", "The book '" + apiBook.getTitle() + "' already exists in your library!");
            } else {
                // If it's completely new, save it ONCE.
                bookRepository.save(apiBook);
                redirectAttributes.addFlashAttribute("successMessage", "New book imported successfully!");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "No book matches found on Google Books.");
        }

        return "redirect:/admin/books";
    }
}
