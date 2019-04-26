package movienight.movienight.repositories;

import movienight.movienight.models.Movie;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MoviesRepository extends MongoRepository<Movie, String> {
    @Query(value = "{'title': {$regex : ?0, $options: 'i'}}")
    Movie findByTitle(String title);
}