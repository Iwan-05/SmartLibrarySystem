package com.csc3402.smartlibrarysystem.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="loan_id")
    private Long loan_id;
    // Foreign Key mapping to User via matric_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matric_id", nullable = false)
    private User user;
    // Foreign Key mapping to Book
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    @Column(name="borrow_date")
    private LocalDateTime borrow_date;
    @Column(name="due_date")
    private LocalDateTime due_date;
    @Column(name="return_date")
    private LocalDateTime return_date;
    @Column(name="fine_amount")
    private double fine_amount;


    public Loan() {
    }

    public Loan(Long loan_id, User user, Book book, LocalDateTime borrow_date, LocalDateTime due_date, LocalDateTime return_date, double fine_amount) {
        this.loan_id = loan_id;
        this.user = user;
        this.book = book;
        this.borrow_date = borrow_date;
        this.due_date = due_date;
        this.return_date = return_date;
        this.fine_amount = fine_amount;
    }

    public Long getLoan_id() { return loan_id; }
    public void setLoan_id(Long loan_id) { this.loan_id = loan_id; }
    public LocalDateTime getBorrow_date() { return borrow_date; }
    public void setBorrow_date(LocalDateTime borrow_date) { this.borrow_date = borrow_date; }
    public LocalDateTime getDue_date() { return due_date; }
    public void setDue_date(LocalDateTime due_date) { this.due_date = due_date; }
    public LocalDateTime getReturn_date() { return return_date; }
    public void setReturn_date(LocalDateTime return_date) { this.return_date = return_date; }
    public double getFine_amount() { return fine_amount; }
    public void setFine_amount(double fine_amount) { this.fine_amount = fine_amount; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
}