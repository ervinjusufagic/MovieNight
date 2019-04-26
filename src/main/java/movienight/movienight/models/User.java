package movienight.movienight.models;

public class User {

    private String userId;

    private String name;

    private String email;

    private String profilePictureUrl;

    private String accessToken;

    private String refreshToken;


    public User() { }

    public User(String userId, String name, String email, String profilePictureUrl, String accessToken, String refreshToken) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getUserId() { return userId; }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public String getAccessToken() { return accessToken; }

    public String getRefreshToken() { return refreshToken; }
}
