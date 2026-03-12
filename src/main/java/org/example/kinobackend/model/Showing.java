package org.example.kinobackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "showings")
public class Showing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "theatre_id")
    private Theatre theatre;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean extra;

    @Enumerated(EnumType.STRING)
    private ShowingStatus status;

    public Showing() {
        this.status = ShowingStatus.SCHEDULED;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Theatre getTheatre() {
        return theatre;
    }

    public void setTheatre(Theatre theatre) {
        this.theatre = theatre;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isExtra() {
        return extra;
    }

    public void setExtra(boolean extra) {
        this.extra = extra;
    }

    public ShowingStatus getStatus() {
        return status;
    }

    public void setStatus(ShowingStatus status) {
        this.status = status;
    }

    public void calculateEndTime() {
        if (this.startTime != null) {
            this.endTime = this.startTime.plusHours(2);
        }
    }

    public void cancel() {
        this.status = ShowingStatus.CANCELLED;
    }

    public void markStarted() {
        this.status = ShowingStatus.STARTED;
    }
}