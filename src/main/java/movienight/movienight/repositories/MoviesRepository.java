package movienight.movienight.repositories;

import movienight.movienight.models.Movies;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface MoviesRepository extends MongoRepository<Movies, String> {
    Movies findBy_id(ObjectId _id);
}