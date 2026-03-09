package org.example.kinobackend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation; // kan være null ved direkte køb

    @ManyToOne
    @JoinColumn(name = "showing_id")
    private Showing showing;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(unique = true)
    private String ticketCode;

    private BigDecimal price;
    private boolean used = false;
    private LocalDateTime usedAt;
    private LocalDateTime purchasedAt = LocalDateTime.now();

    public Ticket() {
        this.ticketCode = "TK-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }

    // Scan billet ved indgang — returnerer false hvis allerede brugt
    public boolean scan() {
        if (used) return false;
        this.used = true;
        this.usedAt = LocalDateTime.now();
        return true;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Reservation getReservation() { return reservation; }
    public void setReservation(Reservation reservation) { this.reservation = reservation; }
    public Showing getShowing() { return showing; }
    public void setShowing(Showing showing) { this.showing = showing; }
    public Seat getSeat() { return seat; }
    public void setSeat(Seat seat) { this.seat = seat; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public String getTicketCode() { return ticketCode; }
    public void setTicketCode(String ticketCode) { this.ticketCode = ticketCode; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }
    public LocalDateTime getUsedAt() { return usedAt; }
    public void setUsedAt(LocalDateTime usedAt) { this.usedAt = usedAt; }
    public LocalDateTime getPurchasedAt() { return purchasedAt; }
    public void setPurchasedAt(LocalDateTime purchasedAt) { this.purchasedAt = purchasedAt; }
}
