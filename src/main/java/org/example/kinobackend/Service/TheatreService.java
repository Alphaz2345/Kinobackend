package org.example.kinobackend.Service;

import org.example.kinobackend.Repositories.SeatRepository;
import org.example.kinobackend.Model.Seat;
import org.example.kinobackend.Model.Theatre;
import org.example.kinobackend.Repositories.TheatreRepository;
import org.springframework.stereotype.Service;

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
}