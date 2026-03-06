// ─────────────────────────────────────────────────────────
// COPY TIL: model/Movie.java
// ─────────────────────────────────────────────────────────
package org.example.kinobackend.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String description;
    private int durationMinutes;
    private int ageLimit;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String posterUrl;
    private boolean archived = false;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Movie() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public int getAgeLimit() { return ageLimit; }
    public void setAgeLimit(int ageLimit) { this.ageLimit = ageLimit; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
    public boolean isArchived() { return archived; }
    public void setArchived(boolean archived) { this.archived = archived; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
