package org.example.kinobackend.Repositories;

import org.example.kinobackend.Model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Integer> {

    // Alle sæder i en bestemt sal
    List<Seat> findByTheatreId(int theatreId);
}