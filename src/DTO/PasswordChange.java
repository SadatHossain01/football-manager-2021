package DTO;

import java.io.Serializable;

public class PasswordChange implements Serializable {
    String newPassword;
    String clubName;

    public PasswordChange(String newPassword, String clubName) {
        this.newPassword = newPassword;
        this.clubName = clubName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }
}

