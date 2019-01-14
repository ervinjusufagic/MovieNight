package movienight.movienight.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
public class Movies {

        @Id
        public ObjectId _id;

        public String title;

        // Constructors
        public Movies() {}

        public Movies(ObjectId _id, String title) {
            this._id = _id;
            this.title = title;
        }

        // ObjectId needs to be converted to string
        public String get_id() { return _id.toHexString(); }
        public void set_id(ObjectId _id) { this._id = _id; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
    }

