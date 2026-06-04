package com.csc3402.smartlibrarysystem.model;

public class Rating {
    private String rating_id;
    private String user_id;
    private String book_id;
    private double star_score;

    public Rating() {
    }

    public Rating(String rating_id, String user_id, String book_id, double star_score) {
        this.rating_id = rating_id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.star_score = star_score;
    }

    public String getRating_id() {
        return rating_id;
    }

    public void setRating_id(String rating_id) {
        this.rating_id = rating_id;
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

    public double getStar_score() {
        return star_score;
    }

    public void setStar_score(double star_score) {
        this.star_score = star_score;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "rating_id='" + rating_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", book_id='" + book_id + '\'' +
                ", star_score=" + star_score +
                '}';
    }
}
