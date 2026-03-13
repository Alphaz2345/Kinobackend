package org.example.kinobackend.controller;

import org.example.kinobackend.model.Showing;
import org.example.kinobackend.service.ShowingService;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/showings")
@CrossOrigin(origins = "*")
public class ShowingController {

    private final ShowingService showingService;

    public ShowingController(ShowingService showingService) {
        this.showingService = showingService;
    }

    // alle forestillinger
    @GetMapping
    public List<Showing> getAllShowings() {
        return showingService.getAllShowings();
    }

    //forestillinger for en film (SCRUM-49)
    @GetMapping("/movie/{movieId}")
    public List<Showing> getByMovie(@PathVariable int movieId) {
        return showingService.getShowingsByMovie(movieId);
    }

    // GET /api/showings/{id}
    @GetMapping("/{id}")
    public Showing getShowing(@PathVariable int id) {
        return showingService.getById(id);
    }

    //opret forestilling (SCRUM-15, 16, 18)
    @PostMapping
    public Showing createShowing(@RequestBody Map<String, Object> body) {
        int movieId = (int) body.get("movieId");
        int theatreId = (int) body.get("theatreId");
        LocalDateTime startTime = LocalDateTime.parse((String) body.get("startTime"));
        boolean isExtra = body.containsKey("extra") && (boolean) body.get("extra");
        return showingService.createShowing(movieId, theatreId, startTime, isExtra);
    }

    //aflys forestilling (SCRUM-19)
    @DeleteMapping("/{id}")
    public Showing cancelShowing(@PathVariable int id) {
        return showingService.cancelShowing(id);
    }

    //markér som startet (SCRUM-38)
    @PatchMapping("/{id}/start")
    public Showing markStarted(@PathVariable int id) {
        return showingService.markAsStarted(id);
    }

    // flyt til anden sal (SCRUM-21)
    @PatchMapping("/{id}/move")
    public Showing moveShowing(@PathVariable int id, @RequestBody Map<String, Integer> body) {
        return showingService.moveToTheatre(id, body.get("theatreId"));
    }

    //  dagsliste (SCRUM-37)
    @GetMapping("/schedule")
    public List<Showing> getDailySchedule(
            @RequestParam int theatreId,
            @RequestParam String date) {
        return showingService.getDailySchedule(theatreId, LocalDate.parse(date));
    }
}
