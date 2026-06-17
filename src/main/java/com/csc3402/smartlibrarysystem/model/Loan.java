package com.csc3402.smartlibrarysystem.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="loan_id")
    private Long loan_id;
    @Column(name="user_id")
    private String user_id;
    @ManyToOne
    @JoinColumn(name = "book_id")
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

    public Loan(Long loan_id, String user_id, Book book, LocalDateTime borrow_date, LocalDateTime due_date, LocalDateTime return_date, double fine_amount) {
        this.loan_id = loan_id;
        this.user_id = user_id;
        this.book = book;
        this.borrow_date = borrow_date;
        this.due_date = due_date;
        this.return_date = return_date;
        this.fine_amount = fine_amount;
    }

    public Long getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(Long loan_id) {
        this.loan_id = loan_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getBorrow_date() {
        return borrow_date;
    }

    public void setBorrow_date(LocalDateTime borrow_date) {
        this.borrow_date = borrow_date;
    }

    public LocalDateTime getDue_date() {
        return due_date;
    }

    public void setDue_date(LocalDateTime due_date) {
        this.due_date = due_date;
    }

    public LocalDateTime getReturn_date() {
        return return_date;
    }

    public void setReturn_date(LocalDateTime return_date) {
        this.return_date = return_date;
    }

    public double getFine_amount() {
        return fine_amount;
    }

    public void setFine_amount(double fine_amount) {
        this.fine_amount = fine_amount;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "loan_id=" + loan_id +
                ", user_id='" + user_id + '\'' +
                ", book=" + (book != null ? book.getTitle() : null) +
                ", borrow_date=" + borrow_date +
                ", due_date=" + due_date +
                ", return_date=" + return_date +
                ", fine_amount=" + fine_amount +
                '}';
    }
}