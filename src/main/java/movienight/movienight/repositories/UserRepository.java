package movienight.movienight.repositories;

import movienight.movienight.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
        User findByEmail(String email);
    }

