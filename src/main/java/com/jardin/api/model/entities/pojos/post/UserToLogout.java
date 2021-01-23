package com.jardin.api.model.entities.pojos.post;

public class UserToLogout {

    String sessionToken;
    String username;

    public UserToLogout(String sessionToken, String username) {
        this.sessionToken = sessionToken;
        this.username = username;
    }

    public UserToLogout() {
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserToLogout{" +
                "sessionToken='" + sessionToken + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
