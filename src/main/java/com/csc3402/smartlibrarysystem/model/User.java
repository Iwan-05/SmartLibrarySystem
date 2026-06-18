package com.csc3402.smartlibrarysystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private String user_id;
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
    @Column(name="access_status")
    private String access_status = "PENDING"; // Sets the default in Java

    public User() {
    }

    public User(String user_id, String username, String password, String role, double fine, String faculty) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.fine = fine;
        this.faculty = faculty;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }
    public String getAccess_status() { return access_status; }

    public void setAccess_status(String access_status) { this.access_status = access_status; }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", fine=" + fine +
                ", faculty='" + faculty + '\'' +
                '}';
    }
}
