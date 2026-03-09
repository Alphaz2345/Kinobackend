package org.example.kinobackend.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Filmens titel.
    private String title;

    // En beskrivelse af filmen.
    private String description;

    // Filmens længde i minutter.
    private int durationMinutes;

    // Aldersgrænse for filmen.
    private int ageLimit;

    @ManyToOne
    @JoinColumn(name = "category_id")

    private Category category;

    // URL til filmens plakat eller billede.
    private String posterUrl;

    // archived bruges til "soft delete".
    private boolean archived = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    // JPA kræver en tom konstruktør.
    public Movie() {}

    // Getter for id.
    public int getId() { return id; }

    // Setter for id.
    public void setId(int Id) { this.id = id; }

    // Getter for titel.
    public String getTitle() { return title; }

    // Setter for titel.
    public void setTitle(String title) { this.title = title; }

    // Getter for beskrivelse.
    public String getDescription() { return description; }

    // Setter for beskrivelse.
    public void setDescription(String description) { this.description = description; }

    // Getter for filmens længde.
    public int getDurationMinutes() { return  durationMinutes; }

    // Getter for aldersgrænse.
    public int getAgeLimit() { return ageLimit; }

    // Setter for aldersgrænse.
    public void setAgeLimit(int ageLimit) { this.ageLimit = ageLimit; }

    // Getter for kategori.
    public Category getCategory() { return category; }

    //Setter for kategori.
    public void setCategory(Category category) {this.category = category; }

    // Getter for poster URL.
    public String getPosterUrl() {return posterUrl; }

    // Setter for poster URL.
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    // Getter for archived.
    public boolean isArchived() {return archived; }

    // Getter for oprettelsestidspunktet.
    public LocalDateTime getCreatedAt() {return createdAt; }

    // Setter for oprettelsestidspunktet.
    public void setCreatedAT(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
