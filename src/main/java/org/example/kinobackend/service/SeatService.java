package org.example.kinobackend.service;

import org.example.kinobackend.Repositories.SeatRepository;
import org.example.kinobackend.model.Seat;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> findByTheatreId(int theatreId) {
        return seatRepository.findByTheatreId(theatreId);
    }

    public Seat findById(int id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sæde ikke fundet"));
    }

    public Seat save(Seat seat) {
        return seatRepository.save(seat);
    }

    public void delete(int id) {
        seatRepository.deleteById(id);
    }
}