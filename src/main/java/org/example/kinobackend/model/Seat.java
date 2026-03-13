package org.example.kinobackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "theatre_id")
    private Theatre theatre;

    private int rowNumber;
    private int seatNumber;

    public Seat() {}

    public Seat(Theatre theatre, int rowNumber, int seatNumber) {
        this.theatre = theatre;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Theatre getTheatre() { return theatre; }
    public void setTheatre(Theatre theatre) { this.theatre = theatre; }
    public int getRowNumber() { return rowNumber; }
    public void setRowNumber(int rowNumber) { this.rowNumber = rowNumber; }
    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }
}