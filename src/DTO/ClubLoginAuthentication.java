package DTO;

import java.io.Serializable;

public class ClubLoginAuthentication implements Serializable {
    private String username;
    private String password;

    public ClubLoginAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
