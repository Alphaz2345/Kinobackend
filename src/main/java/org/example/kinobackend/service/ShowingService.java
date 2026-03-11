package org.example.kinobackend.service;

import org.example.kinobackend.Repositories.MovieRepository;
import org.example.kinobackend.Repositories.ShowingRepository;
import org.example.kinobackend.Repositories.TheatreRepository;
import org.example.kinobackend.model.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShowingService {

    private final ShowingRepository showingRepository;
    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;

    public ShowingService(ShowingRepository showingRepository,
                          MovieRepository movieRepository,
                          TheatreRepository theatreRepository) {
        this.showingRepository = showingRepository;
        this.movieRepository = movieRepository;
        this.theatreRepository = theatreRepository;
    }

    // Hent alle forestillinger
    public List<Showing> getAllShowings() {
        return showingRepository.findAll();
    }

    // Hent forestillinger for en bestemt film (SCRUM-49)
    public List<Showing> getShowingsByMovie(int movieId) {
        return showingRepository.findByMovieId(movieId);
    }

    // Hent én forestilling
    public Showing getById(int id) {
        return showingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forestilling ikke fundet med id: " + id));
    }

    // Opret forestilling med overlap-tjek (SCRUM-15, 16)
    public Showing createShowing(int movieId, int theatreId, LocalDateTime startTime, boolean isExtra) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Film ikke fundet"));
        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new RuntimeException("Sal ikke fundet"));

        if (theatre.isBlocked()) {
            throw new RuntimeException("Salen er blokeret og kan ikke bookes");
        }

        Showing showing = new Showing();
        showing.setMovie(movie);
        showing.setTheatre(theatre);
        showing.setStartTime(startTime);
        showing.setExtra(isExtra);
        showing.calculateEndTime(); // beregner sluttid + rengøringsbuffer (SCRUM-17)

        // Tjek for overlap (SCRUM-16)
        List<Showing> overlapping = showingRepository.findOverlapping(
                theatreId, startTime, showing.getEndTime());
        if (!overlapping.isEmpty()) {
            throw new RuntimeException("Forestillingen overlapper med en eksisterende i samme sal");
        }

        return showingRepository.save(showing);
    }

    // Aflys forestilling (SCRUM-19)
    public Showing cancelShowing(int id) {
        Showing showing = getById(id);
        showing.cancel();
        return showingRepository.save(showing);
    }

    // Markér forestilling som startet (SCRUM-38)
    public Showing markAsStarted(int id) {
        Showing showing = getById(id);
        showing.markStarted();
        return showingRepository.save(showing);
    }

    // Flyt forestilling til anden sal (SCRUM-21)
    public Showing moveToTheatre(int showingId, int newTheatreId) {
        Showing showing = getById(showingId);
        Theatre newTheatre = theatreRepository.findById(newTheatreId)
                .orElseThrow(() -> new RuntimeException("Ny sal ikke fundet"));

        // Tjek overlap i den nye sal
        List<Showing> overlapping = showingRepository.findOverlapping(
                newTheatreId, showing.getStartTime(), showing.getEndTime());
        if (!overlapping.isEmpty()) {
            throw new RuntimeException("Forestillingen overlapper i den nye sal");
        }

        showing.setTheatre(newTheatre);
        return showingRepository.save(showing);
    }

    // Operatørens dagsliste (SCRUM-37)
    public List<Showing> getDailySchedule(int theatreId, LocalDate date) {
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = date.atTime(23, 59, 59);
        return showingRepository.findByTheatreIdAndStartTimeBetween(theatreId, from, to);
    }
}
