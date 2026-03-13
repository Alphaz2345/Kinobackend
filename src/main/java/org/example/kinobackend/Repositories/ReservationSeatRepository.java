package org.example.kinobackend.Repositories;

import org.example.kinobackend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReservationSeatRepository extends JpaRepository<Reservation, Integer> {

    @Query("SELECT s.id FROM Reservation r JOIN r.seats s WHERE r.showing.id = :showingId AND r.status != 'CANCELLED'")
    List<Integer> findOccupiedSeatIdsByShowingId(@Param("showingId") int showingId);
}