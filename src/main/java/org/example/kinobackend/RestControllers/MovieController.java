
package org.example.kinobackend.RestControllers;

import org.example.kinobackend.Model.Movie;
import org.example.kinobackend.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "*")
public class MovieController {
}