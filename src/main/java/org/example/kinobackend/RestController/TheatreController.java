package org.example.kinobackend.RestControllers;

import org.example.kinobackend.model.Theatre;
import org.example.kinobackend.service.TheatreService;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/theatres")
@CrossOrigin(origins = "*")
public class TheatreController {

    private final TheatreService theatreService;

    public TheatreController(TheatreService theatreService) {
        this.theatreService = theatreService;
    }

    // GET /api/theatres — alle sale (SCRUM-50)
    @GetMapping
    public List<Theatre> getAllTheatres() {
        return theatreService.getAllTheatres();
    }

    // GET /api/theatres/{id}
    @GetMapping("/{id}")
    public Theatre getTheatre(@PathVariable int id) {
        return theatreService.getById(id);
    }

    // POST /api/theatres — opret sal (SCRUM-13)
    @PostMapping
    public Theatre createTheatre(@RequestBody Theatre theatre) {
        return theatreService.createTheatre(theatre);
    }

    // PUT /api/theatres/{id} — rediger sal (SCRUM-14)
    @PutMapping("/{id}")
    public Theatre updateTheatre(@PathVariable int id, @RequestBody Theatre theatre) {
        return theatreService.updateTheatre(id, theatre);
    }

    // POST /api/theatres/{id}/block — blokér sal (SCRUM-39)
    @PostMapping("/{id}/block")
    public Theatre blockTheatre(@PathVariable int id, @RequestBody Map<String, String> body) {
        String reason = body.get("reason");
        LocalDateTime until = body.containsKey("until")
                ? LocalDateTime.parse(body.get("until"))
                : LocalDateTime.now().plusDays(1);
        return theatreService.blockTheatre(id, reason, until);
    }

    // POST /api/theatres/{id}/unblock — deblokér sal
    @PostMapping("/{id}/unblock")
    public Theatre unblockTheatre(@PathVariable int id) {
        return theatreService.unblockTheatre(id);
    }
}
