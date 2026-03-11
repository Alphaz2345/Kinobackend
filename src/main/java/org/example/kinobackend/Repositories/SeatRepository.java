package org.example.kinobackend.Repositories;

import org.example.kinobackend.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Integer> {

    List<Seat> findByTheatreId(int theatreId);
}