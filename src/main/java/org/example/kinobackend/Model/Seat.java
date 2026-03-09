package org.example.kinobackend.Model;


import jakarta.persistence.*;

@Entity

// Her siger vi at tabellen i databasen skal hedde "seats".
@Table(name = "seats")
public class Seat {

    // Dette felt er primærnøglen i tabellen.
    // Det er den værdi der unikt identificerer hvert sæde.
    @Id

    // ID bliver genereret automatisk af databasen.
    // IDENTITY betyder typisk at databasen bruger auto-increment.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Et sæde hører til én sal.
    // Men en sal kan have mange sæder.
    // Derfor bruger vi ManyToOne.
    @ManyToOne

    // Denne kolonne i databasen gemmer hvilken sal sædet tilhører.
    // theatre_id er en foreign key til Theatre tabellen.
    @JoinColumn(name = "theatre_id")
    private Theatre theatre;

    // Hvilken række sædet ligger i.
    // Fx række 1, række 2 osv.
    private int rowNumber;

    // Nummeret på sædet i rækken.
    // Fx sæde 1, sæde 2 osv.
    private int seatNumber;

    // JPA kræver en tom constructor.
    // Den bruges når Hibernate henter data fra databasen.
    public Seat() {}

    // En constructor der gør det nemt at oprette et sæde
    // hvor vi med det samme sætter sal, række og sædenummer.
    public Seat(Theatre theatre, int rowNumber, int seatNumber) {
        this.theatre = theatre;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
    }

    // Returnerer sædets id
    public int getId() { return id; }

    // Sætter id (bruges normalt ikke fordi databasen selv laver id)
    public void setId(int id) { this.id = id; }

    // Henter hvilken sal sædet tilhører
    public Theatre getTheatre() { return theatre; }

    // Sætter hvilken sal sædet tilhører
    public void setTheatre(Theatre theatre) { this.theatre = theatre; }

    // Henter række nummeret
    public int getRowNumber() { return rowNumber; }

    // Sætter række nummeret
    public void setRowNumber(int rowNumber) { this.rowNumber = rowNumber; }

    // Henter sæde nummeret
    public int getSeatNumber() { return seatNumber; }

    // Sætter sæde nummeret
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }
}