package com.csc3402.smartlibrarysystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "BOOKS")
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
    @Column(name="cover_url")
    private String cover_url;
    @Column(name="genre")
    private String genre;

    public Book() {
    }

    public Book(Long book_id, String title, String author, String status, double avg_rating, String cover_url, String genre) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.status = status;
        this.avg_rating = avg_rating;
        this.cover_url = cover_url;
        this.genre = genre;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Long getBook_id() {
        return book_id;
    }

    public void setBook_id(Long book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(double avg_rating) {
        this.avg_rating = avg_rating;
    }

    @Override
    public String toString() {
        return "Book{" +
                "book_id=" + book_id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", status='" + status + '\'' +
                ", avg_rating=" + avg_rating +
                ", cover_url='" + cover_url + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
}
