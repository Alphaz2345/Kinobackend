package org.example.kinobackend.service;

import org.example.kinobackend.Repositories.*;
import org.example.kinobackend.model.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ShowingRepository showingRepository;
    private final SeatRepository seatRepository;
    private final ReservationLogRepository logRepository;
    private final TicketRepository ticketRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ShowingRepository showingRepository,
                              SeatRepository seatRepository,
                              ReservationLogRepository logRepository,
                              TicketRepository ticketRepository) {
        this.reservationRepository = reservationRepository;
        this.showingRepository = showingRepository;
        this.seatRepository = seatRepository;
        this.logRepository = logRepository;
        this.ticketRepository = ticketRepository;
    }

    // Opret reservation (SCRUM-23)
    public Reservation createReservation(int showingId, List<Integer> seatIds,
                                         String customerName, String customerPhone) {
        Showing showing = showingRepository.findById(showingId)
                .orElseThrow(() -> new RuntimeException("Forestilling ikke fundet"));

        // Tjek at ingen sæder allerede er solgt (SCRUM-30)
        for (int seatId : seatIds) {
            if (ticketRepository.existsByShowingIdAndSeatId(showingId, seatId)) {
                throw new RuntimeException("Sæde " + seatId + " er allerede solgt");
            }
        }

        List<Seat> seats = seatRepository.findAllById(seatIds);

        Reservation reservation = new Reservation();
        reservation.setShowing(showing);
        reservation.setCustomerName(customerName);
        reservation.setCustomerPhone(customerPhone);
        reservation.setSeats(seats);
        reservation.setStatus(ReservationStatus.CONFIRMED);

        Reservation saved = reservationRepository.save(reservation);

        // Log oprettelsen (SCRUM-41)
        logRepository.save(new ReservationLog(saved, "created", null, "Reservation oprettet via telefon"));

        return saved;
    }

    // Find reservation på kode (SCRUM-24)
    public Reservation findByCode(String code) {
        return reservationRepository.findByReservationCode(code)
                .orElseThrow(() -> new RuntimeException("Reservation ikke fundet med kode: " + code));
    }

    // Find reservation på telefon (SCRUM-24)
    public List<Reservation> findByPhone(String phone) {
        return reservationRepository.findByCustomerPhone(phone);
    }

    // Annuller reservation (SCRUM-26)
    public Reservation cancelReservation(int id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation ikke fundet"));
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setUpdatedAt(LocalDateTime.now());
        Reservation saved = reservationRepository.save(reservation);

        logRepository.save(new ReservationLog(saved, "cancelled", null, "Annulleret af kunde"));
        return saved;
    }

    // Konverter reservation til køb (SCRUM-28)
    public Reservation convertToPurchase(int id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation ikke fundet"));

        if (reservation.isExpired()) {
            throw new RuntimeException("Reservationen er udløbet");
        }

        reservation.setStatus(ReservationStatus.CONVERTED);
        reservation.setUpdatedAt(LocalDateTime.now());
        Reservation saved = reservationRepository.save(reservation);

        logRepository.save(new ReservationLog(saved, "converted", null, "Konverteret til køb"));
        return saved;
    }

    // Auto-expire udløbne reservationer (SCRUM-27) — kald fra scheduled task
    public void expireOldReservations() {
        List<Reservation> expired = reservationRepository
                .findByStatusAndExpiresAtBefore(ReservationStatus.PENDING, LocalDateTime.now());
        for (Reservation r : expired) {
            r.setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(r);
            logRepository.save(new ReservationLog(r, "expired", null, "Auto-udløbet"));
        }
    }

    // Hent kundens reservationer (SCRUM-56)
    public List<Reservation> getByCustomer(int customerId) {
        return reservationRepository.findByCustomerId(customerId);
    }
}
