package org.example.kinobackend.Repositories;

import org.example.kinobackend.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    Optional<Ticket> findByTicketCode(String ticketCode);
    List<Ticket> findByCustomerId(int customerId);
    boolean existsByShowingIdAndSeatId(int showingId, int seatId);
    long countByShowingId(int showingId);
}