package org.example.kinobackend.RestController;

import org.example.kinobackend.model.Movie;
import org.example.kinobackend.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "*")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // alle aktive film (SCRUM-45)
    @GetMapping
    public List<Movie> getAllMovies(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer categoryId) {

        if (search != null && !search.isEmpty()) {
            return movieService.searchByTitle(search); // SCRUM-46
        }
        if (categoryId != null) {
            return movieService.filterByCategory(categoryId); // SCRUM-47
        }
        return movieService.getAllActiveMovies();
    }

    // filmdetaljer (SCRUM-48)
    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable int id) {
        return movieService.getById(id);
    }

    // opret film (SCRUM-10)
    @PostMapping
    public Movie createMovie(@RequestBody Movie movie) {
        return movieService.createMovie(movie);
    }

    // rediger film (SCRUM-11)
    @PutMapping("/{id}")
    public Movie updateMovie(@PathVariable int id, @RequestBody Movie movie) {
        return movieService.updateMovie(id, movie);
    }

    // arkiver film (SCRUM-12)
    @PatchMapping("/{id}/archive")
    public Movie archiveMovie(@PathVariable int id) {
        return movieService.archiveMovie(id);
    }
}
