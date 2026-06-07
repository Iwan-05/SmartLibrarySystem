package com.csc3402.smartlibrarysystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rating_id")
    private Long rating_id;

    @Column(name="star_score")
    private double star_score;

    // Foreign Key mapping to User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Foreign Key mapping to Book
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    public Rating() {
    }

    public Rating(Long rating_id, User user, Book book, double star_score) {
        this.rating_id = rating_id;
        this.user = user;
        this.book = book;
        this.star_score = star_score;
    }

    public Long getRating_id() { return rating_id; }
    public void setRating_id(Long rating_id) { this.rating_id = rating_id; }
    public double getStar_score() { return star_score; }
    public void setStar_score(double star_score) { this.star_score = star_score; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    @Override
    public String toString() {
        return "Rating{" +
                "rating_id=" + rating_id +
                ", star_score=" + star_score +
                '}';
    }
}