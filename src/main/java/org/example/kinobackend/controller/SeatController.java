package org.example.kinobackend.controller;

import org.example.kinobackend.Repositories.ReservationSeatRepository;
import org.example.kinobackend.Repositories.SeatRepository;
import org.example.kinobackend.Repositories.ShowingRepository;
import org.example.kinobackend.model.Seat;
import org.example.kinobackend.model.Showing;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seats")
@CrossOrigin(origins = "*")
public class SeatController {

    private final SeatRepository seatRepository;
    private final ShowingRepository showingRepository;
    private final ReservationSeatRepository reservationSeatRepository;

    public SeatController(SeatRepository seatRepository,
                          ShowingRepository showingRepository,
                          ReservationSeatRepository reservationSeatRepository) {
        this.seatRepository = seatRepository;
        this.showingRepository = showingRepository;
        this.reservationSeatRepository = reservationSeatRepository;
    }

    @GetMapping("/showing/{showingId}")
    public List<Map<String, Object>> getSeatMap(@PathVariable int showingId) {
        Showing showing = showingRepository.findById(showingId)
                .orElseThrow(() -> new RuntimeException("Forestilling ikke fundet"));

        List<Seat> allSeats = seatRepository.findByTheatreId(showing.getTheatre().getId());
        List<Integer> occupiedIds = reservationSeatRepository.findOccupiedSeatIdsByShowingId(showingId);

        return allSeats.stream().map(seat -> {
            Map<String, Object> seatMap = new HashMap<>();
            seatMap.put("id", seat.getId());
            seatMap.put("row", seat.getRowNumber());
            seatMap.put("seat", seat.getSeatNumber());
            seatMap.put("occupied", occupiedIds.contains(seat.getId()));
            return seatMap;
        }).toList();
    }
}