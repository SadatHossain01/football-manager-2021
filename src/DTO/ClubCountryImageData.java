package DTO;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;

public class ClubCountryImageData implements Serializable {
    private ArrayList<Pair<String, String>> countryFlagList;
    private ArrayList<Pair<String, String>> clubLogoList;

    public ClubCountryImageData(ArrayList<Pair<String, String>> countryFlagList, ArrayList<Pair<String, String>> clubLogoList) {
        this.countryFlagList = new ArrayList<>(countryFlagList);
        this.clubLogoList = new ArrayList<>(clubLogoList);
    }

    public ArrayList<Pair<String, String>> getCountryFlagList() {
        return countryFlagList;
    }

    public void setCountryFlagList(ArrayList<Pair<String, String>> countryFlagList) {
        this.countryFlagList = countryFlagList;
    }

    public ArrayList<Pair<String, String>> getClubLogoList() {
        return clubLogoList;
    }

    public void setClubLogoList(ArrayList<Pair<String, String>> clubLogoList) {
        this.clubLogoList = clubLogoList;
    }
}
