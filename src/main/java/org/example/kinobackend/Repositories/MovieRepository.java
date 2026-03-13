package org.example.kinobackend.Repositories;

import org.example.kinobackend.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    // Hent alle film der IKKE er arkiverede
    List<Movie> findByArchivedFalse();

    // Søg på titel (SCRUM-46)
    List<Movie> findByTitleContainingIgnoreCaseAndArchivedFalse(String title);

    // Filtrér på kategori (SCRUM-47)
    List<Movie> findByCategoryIdAndArchivedFalse(int categoryId);
}
