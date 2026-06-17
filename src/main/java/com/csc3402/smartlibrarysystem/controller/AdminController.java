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
import org.springframework.web.bind.annotation.RequestMapping;

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
}
