package movienight.movienight.controllers;

import movienight.movienight.models.AutoComplete;
import movienight.movienight.models.Movie;
import movienight.movienight.omdb.Url;
import movienight.movienight.repositories.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class MovieController {
    Url url = new Url();

    @Autowired
    private MoviesRepository moviesRepository;




    @GetMapping("getMovieInfo")
    public ResponseEntity<Movie> getMovieInfo(@RequestParam(value = "title") String title) {
        if (moviesRepository.findByTitle(title) != null) {
            return ResponseEntity.ok(moviesRepository.findByTitle(title));
        } else {
            RestTemplate restTemplate = new RestTemplate();
            Movie movie = restTemplate.getForObject(url.url + title + url.key, Movie.class);
            if (movie.getTitle() != null) {
                moviesRepository.save(movie);
                return ResponseEntity.ok(movie);
            }
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Movie>> searchForMovie(@RequestParam(value = "title") String title) {
        RestTemplate restTemplate = new RestTemplate();
        List<AutoComplete> searchResults = Collections.singletonList(restTemplate.getForObject(url.url + title + url.key, AutoComplete.class));
        List<Movie> movies = new ArrayList<>();
        for (AutoComplete movieList : searchResults) {
            for (int i = 0; i < movieList.getMovies().size(); i++) {
                movies.add(movieList.getMovies().get(i));
            }
        }
        return ResponseEntity.ok(movies);
    }
}
