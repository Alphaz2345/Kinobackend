package org.example.kinobackend.controller;

import org.example.kinobackend.model.Reservation;
import org.example.kinobackend.service.ReservationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    //  (SCRUM-23)
    @PostMapping
    public Reservation createReservation(@RequestBody Map<String, Object> body) {
        int showingId = (int) body.get("showingId");
        List<Integer> seatIds = (List<Integer>) body.get("seatIds");
        String name = (String) body.get("customerName");
        String phone = (String) body.get("customerPhone");
        return reservationService.createReservation(showingId, seatIds, name, phone);
    }

    //  find på kode (SCRUM-24)
    @GetMapping("/code/{code}")
    public Reservation findByCode(@PathVariable String code) {
        return reservationService.findByCode(code);
    }

    // find på telefon (SCRUM-24)
    @GetMapping("/phone/{phone}")
    public List<Reservation> findByPhone(@PathVariable String phone) {
        return reservationService.findByPhone(phone);
    }

    // DELETE /api/reservations/{id} — annuller (SCRUM-26)
    @DeleteMapping("/{id}")
    public Reservation cancelReservation(@PathVariable int id) {
        return reservationService.cancelReservation(id);
    }

    // POST /api/reservations/{id}/convert — konverter til køb (SCRUM-28)
    @PostMapping("/{id}/convert")
    public Reservation convertToPurchase(@PathVariable int id) {
        return reservationService.convertToPurchase(id);
    }

    // GET /api/reservations/customer/{customerId} — kundens reservationer (SCRUM-56)
    @GetMapping("/customer/{customerId}")
    public List<Reservation> getByCustomer(@PathVariable int customerId) {
        return reservationService.getByCustomer(customerId);
    }
}
