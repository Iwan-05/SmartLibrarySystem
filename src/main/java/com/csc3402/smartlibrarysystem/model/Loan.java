package com.csc3402.smartlibrarysystem.model;
import java.time.LocalDateTime;

public class Loan {
    private String loan_id;
    private String user_id;
    private String book_id;
    private LocalDateTime borrow_date;
    private LocalDateTime due_date;
    private LocalDateTime return_date;
    private double fine_amount;

    public Loan() {
    }

    public Loan(String loan_id, String user_id, String book_id, LocalDateTime borrow_date, LocalDateTime due_date, LocalDateTime return_date, double fine_amount) {
        this.loan_id = loan_id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.borrow_date = borrow_date;
        this.due_date = due_date;
        this.return_date = return_date;
        this.fine_amount = fine_amount;
    }

    public String getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(String loan_id) {
        this.loan_id = loan_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
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
                "loan_id='" + loan_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", book_id='" + book_id + '\'' +
                ", borrow_date=" + borrow_date +
                ", due_date=" + due_date +
                ", return_date=" + return_date +
                ", fine_amount=" + fine_amount +
                '}';
    }
}
