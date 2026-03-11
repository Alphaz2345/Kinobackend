package org.example.kinobackend.RestController;

public class ReservationController {
    // DELETE /api/reservations/{id} — annuller (SCRUM-26)
    @DeleteMapping("/{id}")
    public Reservation cancelReservation(@PathVariable int id) {
        return reservationService.cancelReservation(id);
}
