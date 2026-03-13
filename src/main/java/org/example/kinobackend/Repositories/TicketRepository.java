package org.example.kinobackend.Repositories;

import org.example.kinobackend.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TicketRepository<Ticket> extends JpaRepository<Ticket, Integer> {

    // Find billet på unik kode (SCRUM-34, 36)
    Optional<Ticket> findByTicketCode(String ticketCode);

    // Find alle billetter for en kunde (SCRUM-57)
    List<Ticket> findByCustomerId(int customerId);

    // Tjek om et sæde allerede er solgt til en forestilling (SCRUM-30)
    boolean existsByShowingIdAndSeatId(int showingId, int seatId);

    // Antal solgte billetter pr. forestilling (til belæg-rapport SCRUM-43)
    long countByShowingId(int showingId);
}
