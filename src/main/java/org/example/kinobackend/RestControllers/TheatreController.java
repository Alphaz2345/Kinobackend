package org.example.kinobackend.RestControllers;

import org.example.kinobackend.Model.Theatre;
import org.example.kinobackend.Service.TheatreService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/theatres")
@CrossOrigin(origins = "*")

public class TheatreController {

    private final TheatreService theatreService;

    public TheatreController(TheatreService theatreService) {
        this.theatreService = theatreService;
    }

    // POST /api/theatres — opret sal (SCRUM-13)
    @PostMapping
    public Theatre createTheatre(@RequestBody Theatre theatre) {
        return theatreService.createTheatre(theatre);
    }
}
