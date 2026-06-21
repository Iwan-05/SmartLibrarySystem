package com.csc3402.smartlibrarysystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name="matric_id", length = 8)
    @Pattern(regexp = "^[0-9]{6}$|^BC[0-9]{6}$")
    private String matric_id;
    @Column(name="username")
    private String username;
    @Column(name="password")
    private String password;
    @Column(name="role")
    private String role;
    @Column(name="fine")
    private double fine;
    @Column(name="faculty")
    private String faculty;
    // Relationship: One User has Many Loans
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Loan> loans;
    // Relationship: One User has Many Ratings
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Rating> ratings;

    public User() {
    }

    public User(String matric_id, String username, String password, String role, double fine, String faculty, String access_status) {
        this.matric_id = matric_id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.fine = fine;
        this.faculty = faculty;
    }

    public String getMatric_id() { return matric_id; }
    public void setMatric_id(String matric_id) { this.matric_id = matric_id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }

    public String getFaculty() { return faculty; }
    public void setFaculty(String faculty) { this.faculty = faculty; }

    public Set<Loan> getLoans() { return loans; }
    public void setLoans(Set<Loan> loans) { this.loans = loans; }

    public Set<Rating> getRatings() { return ratings; }
    public void setRatings(Set<Rating> ratings) { this.ratings = ratings; }

    @Override
    public String toString() {
        return "User{" +
                "matric_id='" + matric_id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", fine=" + fine +
                ", faculty='" + faculty + '\'' +
                ", loans=" + loans +
                ", ratings=" + ratings +
                '}';
    }
}