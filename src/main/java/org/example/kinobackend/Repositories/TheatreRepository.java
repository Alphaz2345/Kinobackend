package org.example.kinobackend.Repositories;

import org.example.kinobackend.model.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TheatreRepository extends JpaRepository<Theatre, Integer> {

    // Kun sale der ikke er blokerede
    List<Theatre> findByBlockedFalse();
}
