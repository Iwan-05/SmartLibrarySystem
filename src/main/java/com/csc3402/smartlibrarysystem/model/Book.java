package com.csc3402.smartlibrarysystem.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="book_id")
    private Long book_id;

    @Column(name="title")
    private String title;

    @Column(name="author")
    private String author;

    @Column(name="status")
    private String status;

    @Column(name="avg_rating")
    private double avg_rating;

    // Relationship: One Book has Many Loans
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private Set<Loan> loans;

    // Relationship: One Book has Many Ratings
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private Set<Rating> ratings;

    public Book() {
    }

    public Book(Long book_id, String title, String author, String status, double avg_rating) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.status = status;
        this.avg_rating = avg_rating;
    }

    public Long getBook_id() { return book_id; }
    public void setBook_id(Long book_id) { this.book_id = book_id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getAvg_rating() { return avg_rating; }
    public void setAvg_rating(double avg_rating) { this.avg_rating = avg_rating; }

    public Set<Loan> getLoans() { return loans; }
    public void setLoans(Set<Loan> loans) { this.loans = loans; }
    public Set<Rating> getRatings() { return ratings; }
    public void setRatings(Set<Rating> ratings) { this.ratings = ratings; }

    @Override
    public String toString() {
        return "Book{" +
                "book_id=" + book_id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", status='" + status + '\'' +
                ", avg_rating=" + avg_rating +
                '}';
    }
}