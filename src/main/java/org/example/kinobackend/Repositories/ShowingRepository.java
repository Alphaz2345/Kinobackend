package org.example.kinobackend.Repositories;

import org.example.kinobackend.model.Showing;
import org.example.kinobackend.model.ShowingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface ShowingRepository extends JpaRepository<Showing, Integer> {

    // Alle forestillinger for en bestemt film
    List<Showing> findByMovieId(int movieId);

    // Alle forestillinger i en bestemt sal
    List<Showing> findByTheatreId(int theatreId);

    // Alle forestillinger med en bestemt status
    List<Showing> findByStatus(ShowingStatus status);

    // Bruges til at tjekke overlap i samme sal (SCRUM-16)
    @Query("SELECT s FROM Showing s WHERE s.theatre.id = :theatreId " +
            "AND s.status != 'CANCELLED' " +
            "AND s.startTime < :endTime AND s.endTime > :startTime")
    List<Showing> findOverlapping(
            @Param("theatreId") int theatreId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    // Forestillinger for en bestemt dato (operatørens dagsliste SCRUM-37)
    List<Showing> findByTheatreIdAndStartTimeBetween(
            int theatreId,
            LocalDateTime from,
            LocalDateTime to
    );
}
