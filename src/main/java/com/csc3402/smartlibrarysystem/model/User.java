package com.csc3402.smartlibrarysystem.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long user_id;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @Column(name="role")
    private String role;

    @Column(name="fine")
    private double fine;

    // Relationship: One User has Many Loans
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Loan> loans;

    // Relationship: One User has Many Ratings
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Rating> ratings;

    public User() {
    }

    public User(Long user_id, String username, String password, String role, double fine) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.fine = fine;
    }

    public Long getUser_id() { return user_id; }
    public void setUser_id(Long user_id) { this.user_id = user_id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }

    public Set<Loan> getLoans() { return loans; }
    public void setLoans(Set<Loan> loans) { this.loans = loans; }
    public Set<Rating> getRatings() { return ratings; }
    public void setRatings(Set<Rating> ratings) { this.ratings = ratings; }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", fine=" + fine +
                '}';
    }
}