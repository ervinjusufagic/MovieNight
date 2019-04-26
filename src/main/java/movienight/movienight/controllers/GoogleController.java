package movienight.movienight.controllers;

import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import movienight.movienight.models.Calender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import movienight.movienight.repositories.UserRepository;
import movienight.movienight.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class GoogleController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoOperations operations;

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity<String> signin(@RequestParam("authCode") String code) throws IOException {
        // Exchange auth code for access token
        GoogleTokenResponse tokenResponse =
                new GoogleAuthorizationCodeTokenRequest(
                        new NetHttpTransport(),
                        JacksonFactory.getDefaultInstance(),
                        "https://www.googleapis.com/oauth2/v4/token",
                        "1067130612493-7ifuln6f3f51k21ml5nnkmfmm82o7d15.apps.googleusercontent.com",
                        "Fi-s71_MlzbEf3AYSRg6yAgN",
                        code,
                        "http://localhost:3000")
                        .execute();

        String accessToken = tokenResponse.getAccessToken();
        String refreshToken = tokenResponse.getRefreshToken();

        // Get profile info from ID token
        GoogleIdToken idToken = tokenResponse.parseIdToken();
        GoogleIdToken.Payload payload = idToken.getPayload();
        String userId = payload.getSubject();  // Use this value as a key to identify a user.
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");

        if (userRepository.findByEmail(email) != null) {
            updateAccessToken();
        } else { userRepository.save(new User(userId, name, email, pictureUrl, accessToken, refreshToken)); }

        return ResponseEntity.ok(email);
    }

    private void updateAccessToken() {
        userRepository.findAll().forEach(user -> {
            GoogleCredential refreshedAccessToken = getRefreshedCredentials(user.getRefreshToken());
            operations.updateFirst(Query.query(Criteria.where("email").is(user.getEmail())), Update.update("accessToken", refreshedAccessToken.getAccessToken()), "user");
        });
    }

    private GoogleCredential getRefreshedCredentials(String refreshCode) {
        try {
            GoogleTokenResponse response = new GoogleRefreshTokenRequest(
                    new NetHttpTransport(), JacksonFactory.getDefaultInstance(), refreshCode, "1067130612493-7ifuln6f3f51k21ml5nnkmfmm82o7d15.apps.googleusercontent.com", "Fi-s71_MlzbEf3AYSRg6yAgN" )
                    .execute();
            return new GoogleCredential().setAccessToken(response.getAccessToken());
        }
        catch( Exception ex ){
            ex.printStackTrace();
            return null;
        }
    }

    @PostMapping("/calendar")
    private ResponseEntity<List<Calender>> getExampleDays(@RequestParam String email ) {

        DateTime now = new DateTime(System.currentTimeMillis());
        List<Event> tempEvent = new ArrayList<>();
        List<Calender> calendarEvents = new ArrayList<>();

        for (User user : userRepository.findAll()) {

            GoogleCredential credential = new GoogleCredential().setAccessToken(user.getAccessToken());
            System.out.println("CREDENTIALS " + credential);
            Calendar calendar =
                    new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                            .setApplicationName("Movie Nights")
                            .build();

            Events events = null;
            try {
                events = calendar.events().list(user.getEmail())
                        .setMaxResults(10)
                        .setTimeMin(now)
                        .setOrderBy("startTime")
                        .setSingleEvents(true)
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            tempEvent = events.getItems();

            for (Event event : tempEvent) {
                calendarEvents.add(new Calender(event.getStart().getDateTime(), event.getEnd().getDateTime(), event.getSummary()));
            }

        }
        return ResponseEntity.ok(calendarEvents);
    }


    @PostMapping("/getevents")
    private ResponseEntity<List<Calender>> getEvents(@RequestParam String email ) {

        DateTime now = new DateTime(System.currentTimeMillis());
        List<Event> tempEvent = new ArrayList<>();
        List<Calender> calendarEvents = new ArrayList<>();

        for (User user : userRepository.findAll()) {

            if (user.getEmail().equals(email)) {
                GoogleCredential credential = new GoogleCredential().setAccessToken(user.getAccessToken());
                System.out.println("CREDENTIALS " + credential);
                Calendar calendar =
                        new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                                .setApplicationName("Movie Nights")
                                .build();

                Events events = null;
                try {
                    events = calendar.events().list(user.getEmail())
                            .setMaxResults(10)
                            .setTimeMin(now)
                            .setOrderBy("startTime")
                            .setSingleEvents(true)
                            .execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                tempEvent = events.getItems();

                for (Event event : tempEvent) {
                    calendarEvents.add(new Calender(event.getStart().getDateTime(), event.getEnd().getDateTime(), event.getSummary()));
                }

            }
        }
        return ResponseEntity.ok(calendarEvents);
    }


    @PostMapping("/book")
    private ResponseEntity<String> scheduleEvent(@RequestBody Calender calender) {
        GoogleCredential credential = new GoogleCredential().setAccessToken(userRepository.findByEmail(calender.getCreatedByUser()).getAccessToken());
        Calendar calendar =
                new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                        .setApplicationName("Movie Nights")
                        .build();

        Event event = new Event()
                .setSummary(calender.getSummary())
                .setLocation("Malmö, Sweden");

        EventDateTime start = new EventDateTime()
                .setDateTime(calender.getStartDate())
                .setTimeZone("Europe/Stockholm");
        event.setStart(start);

        org.joda.time.DateTime jodaTimeSHIT = new org.joda.time.DateTime(calender.getStartDate().getValue()).plusHours(2);
        DateTime endDateTime = new DateTime(jodaTimeSHIT.toString());
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Europe/Stockholm");
        event.setEnd(end);

        addAttendees(event, calender);

        String calendarId = calender.getCreatedByUser();
        try {
            event = calendar.events().insert(calendarId, event).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("CalendarEvent created: " + event);
    }

    private void addAttendees(Event event, Calender calender) {
        userRepository.findAll().forEach(user -> {
            if(!user.getEmail().equals(calender.getCreatedByUser())) {
                if (user.getEmail() == null) {
                    System.out.println("Created event without attendees.");
                } else {
                    EventAttendee[] attendees = new EventAttendee[]{
                            new EventAttendee().setEmail(user.getEmail())
                    };
                    event.setAttendees(Arrays.asList(attendees));
                }
            }
        });
    }
}