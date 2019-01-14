package movienight.movienight.controllers;

import movienight.movienight.models.Movies;
import org.springframework.web.bind.annotation.GetMapping;
import movienight.movienight.repositories.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MoviesController {

    @Autowired
    private MoviesRepository repository;

    @GetMapping("/")
    public List<Movies> getAllMovies() {
        return repository.findAll();
    }
}


