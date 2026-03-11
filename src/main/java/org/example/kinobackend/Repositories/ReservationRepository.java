package org.example.kinobackend.Repositories;

import org.example.kinobackend.model.Reservation;
import org.example.kinobackend.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    Optional<Reservation> findByReservationCode(String reservationCode);
    List<Reservation> findByCustomerPhone(String customerPhone);
    List<Reservation> findByShowingId(int showingId);
    List<Reservation> findByStatusAndExpiresAtBefore(ReservationStatus status, LocalDateTime now);
    List<Reservation> findByCustomerId(int customerId);

    @Query("SELECT s.id FROM Reservation r JOIN r.seats s WHERE r.showing.id = :showingId AND r.status != 'CANCELLED'")
    List<Integer> findOccupiedSeatIdsByShowingId(@Param("showingId") int showingId);
}