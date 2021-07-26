package DTO;

import DataModel.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UpdatedTransferList implements Serializable {
    private List<Player> playerList;
    private String toWhichClub;

    public UpdatedTransferList(List<Player> playerList, String toWhichClub) {
        this.playerList = new ArrayList<>(playerList);
        this.toWhichClub = toWhichClub;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public String getToWhichClub() {
        return toWhichClub;
    }
}
