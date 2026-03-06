package org.example.kinobackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String passwordHash;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private boolean active = true;
    private LocalDateTime createdAt = LocalDateTime.now();

    // JPA kræver en tom konstruktør (no-args constructor).
    public Employee() {}

    // Getter for id.
    public int getId() { return id; }

    // Setter for id.
    public void setId(int id) { this.id = id; }

    // Getter for email.
    public String getEmail() {return email; }

    // Setter for email.
    public void setEmail(String email) {this.email = email;}

    // Getter for passwordHash.
    public String getPasswordHash() { return passwordHash; }

    // Setter for passwordHash.
    public void setPasswordHash(String passwordHash) {this.passwordHash = passwordHash; }

    // Getter for role.
    public Role getRole() { return role; }

     // Setter for role.
    public void setRole(Role role) {this.role = role; }

     // Getter for active.
    public boolean isActive() {return active; }

    // Setter for active.
    public void setActive(boolean active) { this.active = active;}

    // Getter for createdAt.
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setter for createdAt.
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt; }



}

