package org.example.kinobackend.Repositories;

import org.example.kinobackend.model.ReservationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservationLogRepository extends JpaRepository<ReservationLog, Integer> {

    // Hent alle log-indgange for en bestemt reservation (SCRUM-41)
    List<ReservationLog> findByReservationId(int reservationId);
}
