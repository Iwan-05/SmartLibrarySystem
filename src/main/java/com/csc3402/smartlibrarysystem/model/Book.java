package com.csc3402.smartlibrarysystem.model;

public class Book {
    private String book_id;
    private String title;
    private String author;
    private String status;
    private double avg_rating;

    public Book() {
    }

    public Book(String book_id, String title, String author, String status, double avg_rating) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.status = status;
        this.avg_rating = avg_rating;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
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
                "book_id='" + book_id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", status='" + status + '\'' +
                ", avg_rating=" + avg_rating +
                '}';
    }
}
