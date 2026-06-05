package com.csc3402.smartlibrarysystem.controller;

import com.csc3402.smartlibrarysystem.model.Book;
import com.csc3402.smartlibrarysystem.repository.BookRepository;
import com.csc3402.smartlibrarysystem.repository.LoanRepository;
import com.csc3402.smartlibrarysystem.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/book")
public class BookController {
    private final BookRepository bookRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private LoanRepository loanRepository;


    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    //show book list
    @GetMapping("list")
    public String showList(Model model){
        model.addAttribute("books", bookRepository.findAll());
        return "list-book";//ni dalam html punya view
    }

    //add book
    @PostMapping("add")
    public String addBook(@Validated Book book, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-staff";//ni dalam html punya view
        }
        bookRepository.save(book);
        return "redirect:list";
    }

    //update book
    @GetMapping("update")
    public String showUpdateMainForm(Model model){
        model.addAttribute("book", bookRepository.findAll());
        return "choose-book-to-update";//tukar based on html punya name
    }//this method nak pilih book mane nak update

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable(""))
}
