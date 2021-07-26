package DTO;

import java.io.Serializable;

public class SellRequest implements Serializable {
    private String playerName;
    private String currentClub;
    private double TransferFee;

    public SellRequest(String playerName, String currentClub, double TransferFee) {
        this.playerName = playerName;
        this.currentClub = currentClub;
        this.TransferFee = TransferFee;
    }

    public String getPlayerName() {
        return playerName;
    }

    public double getTransferFee() {
        return TransferFee;
    }
}
