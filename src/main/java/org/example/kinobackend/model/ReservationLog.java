package org.example.kinobackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation_logs")
public class ReservationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    // "created", "modified", "cancelled", "converted", "expired"
    private String action;

    @ManyToOne
    @JoinColumn(name = "performed_by")
    private Employee performedBy; // null = system eller kunde

    private String note;
    private LocalDateTime loggedAt = LocalDateTime.now();

    public ReservationLog() {}

    public ReservationLog(Reservation reservation, String action, Employee performedBy, String note) {
        this.reservation = reservation;
        this.action = action;
        this.performedBy = performedBy;
        this.note = note;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Reservation getReservation() { return reservation; }
    public void setReservation(Reservation reservation) { this.reservation = reservation; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public Employee getPerformedBy() { return performedBy; }
    public void setPerformedBy(Employee performedBy) { this.performedBy = performedBy; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public LocalDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }
}
