package org.example.kinobackend.service;

import org.example.kinobackend.Repositories.MovieRepository;
import org.example.kinobackend.model.Movie;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Hent alle aktive film
    public List<Movie> getAllActiveMovies() {
        return movieRepository.findByArchivedFalse();
    }

    // Søg på titel (SCRUM-46)
    public List<Movie> searchByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCaseAndArchivedFalse(title);
    }

    // Filtrér på kategori (SCRUM-47)
    public List<Movie> filterByCategory(int categoryId) {
        return movieRepository.findByCategoryIdAndArchivedFalse(categoryId);
    }

    // Hent én film (SCRUM-48)
    public Movie getById(int id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Film ikke fundet med id: " + id));
    }

    // Opret film (SCRUM-10)
    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    // Rediger film (SCRUM-11)
    public Movie updateMovie(int id, Movie updated) {
        Movie existing = getById(id);
        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setDurationMinutes(updated.getDurationMinutes());
        existing.setAgeLimit(updated.getAgeLimit());
        existing.setCategory(updated.getCategory());
        existing.setPosterUrl(updated.getPosterUrl());
        return movieRepository.save(existing);
    }

    // Arkiver film uden at slette (SCRUM-12)
    public Movie archiveMovie(int id) {
        Movie movie = getById(id);
        movie.setArchived(true);
        return movieRepository.save(movie);
    }
}
