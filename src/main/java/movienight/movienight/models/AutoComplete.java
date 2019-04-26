package movienight.movienight.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class AutoComplete {

        @JsonProperty("Search")
        private ArrayList<Movie> movies = new ArrayList<>();

        public AutoComplete() { }

        public ArrayList<Movie> getMovies() { return movies; }

        public void setMovies(ArrayList<Movie> movies) { this.movies = movies; }


}
