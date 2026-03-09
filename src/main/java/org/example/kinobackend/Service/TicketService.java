package org.example.kinobackend.service;

import org.example.kinobackend.Repositories.*;
import org.example.kinobackend.model.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ShowingRepository showingRepository;
    private final SeatRepository seatRepository;

    public TicketService(TicketRepository ticketRepository,
                         ShowingRepository showingRepository,
                         SeatRepository seatRepository) {
        this.ticketRepository = ticketRepository;
        this.showingRepository = showingRepository;
        this.seatRepository = seatRepository;
    }

    // Sælg billetter direkte uden reservation (SCRUM-29)
    public List<Ticket> purchaseTickets(int showingId, List<Integer> seatIds, Customer customer) {
        Showing showing = showingRepository.findById(showingId)
                .orElseThrow(() -> new RuntimeException("Forestilling ikke fundet"));

        List<Ticket> tickets = new ArrayList<>();
        for (int seatId : seatIds) {
            // Tjek dobbeltsalg (SCRUM-30)
            if (ticketRepository.existsByShowingIdAndSeatId(showingId, seatId)) {
                throw new RuntimeException("Sæde " + seatId + " er allerede solgt");
            }
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new RuntimeException("Sæde ikke fundet"));

            Ticket ticket = new Ticket();
            ticket.setShowing(showing);
            ticket.setSeat(seat);
            ticket.setCustomer(customer);
            ticket.setPrice(new BigDecimal("115.00")); // fast pris
            tickets.add(ticketRepository.save(ticket));
        }
        return tickets;
    }

    // Find billet på kode (SCRUM-36)
    public Ticket findByCode(String code) {
        return ticketRepository.findByTicketCode(code)
                .orElseThrow(() -> new RuntimeException("Billet ikke fundet med kode: " + code));
    }

    // Scan billet ved indgang (SCRUM-34, 35)
    public boolean scanTicket(String code) {
        Ticket ticket = findByCode(code);
        boolean success = ticket.scan(); // returnerer false hvis allerede brugt (SCRUM-35)
        ticketRepository.save(ticket);
        return success;
    }

    // Hent kundens billetter (SCRUM-57)
    public List<Ticket> getTicketsByCustomer(int customerId) {
        return ticketRepository.findByCustomerId(customerId);
    }
}
