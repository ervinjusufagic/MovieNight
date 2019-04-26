package movienight.movienight.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

public class Movie {

    @Id
    private String _id;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Released")
    private String released;

    @JsonProperty("Genre")
    private String genre;

    @JsonProperty("Language")
    private String language;

    @JsonProperty("Poster")
    private String poster;

    private String imdbRating;

    public Movie() { }

    public Movie(String _id, String title, String released, String genre, String language, String poster, String imdbRating) {
        this._id = _id;
        this.title = title;
        this.released = released;
        this.genre = genre;
        this.language = language;
        this.poster = poster;
        this.imdbRating = imdbRating;
    }

    public String getId() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getReleased() {
        return released;
    }

    public String getGenre() {
        return genre;
    }

    public String getLanguage() {
        return language;
    }

    public String getPoster() {
        return poster;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }
}