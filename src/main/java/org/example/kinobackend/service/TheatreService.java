package org.example.kinobackend.service;

import org.example.kinobackend.Repositories.SeatRepository;
import org.example.kinobackend.Repositories.TheatreRepository;
import org.example.kinobackend.model.Seat;
import org.example.kinobackend.model.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TheatreService {

    private final TheatreRepository theatreRepository;
    private final SeatRepository seatRepository;

    public TheatreService(TheatreRepository theatreRepository, SeatRepository seatRepository) {
        this.theatreRepository = theatreRepository;
        this.seatRepository = seatRepository;
    }

    // Hent alle sale
    public List<Theatre> getAllTheatres() {
        return theatreRepository.findAll();
    }

    // Hent én sal
    public Theatre getById(int id) {
        return theatreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sal ikke fundet med id: " + id));
    }

    // Opret sal og generer sæder automatisk (SCRUM-13)
    public Theatre createTheatre(Theatre theatre) {
        Theatre saved = theatreRepository.save(theatre);
        generateSeats(saved);
        return saved;
    }

    // Generer alle sæder for en sal
    private void generateSeats(Theatre theatre) {
        for (int row = 1; row <= theatre.getRows(); row++) {
            for (int seat = 1; seat <= theatre.getSeatsPerRow(); seat++) {
                seatRepository.save(new Seat(theatre, row, seat));
            }
        }
    }

    // Rediger sal (SCRUM-14)
    public Theatre updateTheatre(int id, Theatre updated) {
        Theatre existing = getById(id);
        existing.setName(updated.getName());
        existing.setRows(updated.getRows());
        existing.setSeatsPerRow(updated.getSeatsPerRow());
        return theatreRepository.save(existing);
    }

    // Blokér sal (SCRUM-39)
    public Theatre blockTheatre(int id, String reason, LocalDateTime until) {
        Theatre theatre = getById(id);
        theatre.setBlocked(true);
        theatre.setBlockedReason(reason);
        theatre.setBlockedUntil(until);
        return theatreRepository.save(theatre);
    }

    // Deblokér sal
    public Theatre unblockTheatre(int id) {
        Theatre theatre = getById(id);
        theatre.setBlocked(false);
        theatre.setBlockedReason(null);
        theatre.setBlockedUntil(null);
        return theatreRepository.save(theatre);
    }
}