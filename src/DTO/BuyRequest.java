package DTO;

import java.io.Serializable;

public class BuyRequest implements Serializable {
    private String playerName;
    private String playerCurrentClub;
    private String potentialBuyerClub;

    public BuyRequest(String playerName, String playerCurrentClub, String potentialBuyerClub) {
        this.playerName = playerName;
        this.playerCurrentClub = playerCurrentClub;
        this.potentialBuyerClub = potentialBuyerClub;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerCurrentClub() {
        return playerCurrentClub;
    }

    public String getPotentialBuyerClub() {
        return potentialBuyerClub;
    }
}
