package DataModel;

import java.util.ArrayList;
import java.util.List;

public class League {
    public List<Player> CentralPlayerDatabase;
    private List<Club> ClubList;
    private List<Country> CountryList;
    private List<Position> PositionList;

    public League() {
        CentralPlayerDatabase = new ArrayList<>();
        ClubList = new ArrayList<>();
        CountryList = new ArrayList<>();
        PositionList = new ArrayList<>();
    }

    public List<Club> getClubList() {
        return ClubList;
    }

    public static String FormatName(String name) {
        StringBuilder str = new StringBuilder(name.trim());
        str.setCharAt(0, Character.toUpperCase(str.charAt(0)));
        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i - 1) == ' ') {
                if (str.charAt(i) == ' ') {
                    str.deleteCharAt(i);
                    i--;
                } else str.setCharAt(i, Character.toUpperCase(str.charAt(i)));
            }
        }
        return str.toString();
    }

    public Player FindPlayer(String PlayerName) { //will return null if club is not registered yet
        Player wanted = null;
        String FormattedPlayerName = FormatName(PlayerName);
        for (var p : CentralPlayerDatabase) {
            if (p.getName().equalsIgnoreCase(FormattedPlayerName)) {
                wanted = p;
                break;
            }
        }
        return wanted;
    }

    public Club FindClub(String ClubName) { //will return null if club is not registered yet
        Club wanted = null;
        String FormattedClubName = FormatName(ClubName);
        for (var c : ClubList) {
            if (c.getName().equalsIgnoreCase(FormattedClubName)) {
                wanted = c;
                break;
            }
        }
        return wanted;
    }

    public Club FindClubWithUnaccentedName(String ClubName) { //will return null if club is not registered yet
        Club wanted = null;
        String FormattedClubName = FormatName(ClubName);
        for (var c : ClubList) {
            if (c.getUnAccentedName().equalsIgnoreCase(FormattedClubName)) {
                wanted = c;
                break;
            }
        }
        return wanted;
    }

    public Country FindCountry(String CountryName) { //will return null if club is not registered yet
        Country wanted = null;
        String FormattedCountryName = FormatName(CountryName);
        for (var c : CountryList) {
            if (c.getName().equalsIgnoreCase(FormattedCountryName)) {
                wanted = c;
                break;
            }
        }
        return wanted;
    }

    public Position FindPosition(String PositionName) { //will return null if club is not registered yet
        Position wanted = null;
        String FormattedPositionName = FormatName(PositionName);
        for (var c : PositionList) {
            if (c.getName().equalsIgnoreCase(FormattedPositionName)) {
                wanted = c;
                break;
            }
        }
        return wanted;
    }

    public void addPlayerToLeague(Player p) {
        //size check and player existence check is done in main
        var c = FindClub(p.getClubName());
        if (c == null) {
            c = new Club(p.getClubName());
            ClubList.add(c);
        }
        String CountryName = FormatName(p.getCountry());
        var country = FindCountry(CountryName);
        if (country == null) {
            country = new Country(CountryName);
            CountryList.add(country);
        }
        String PositionName = FormatName(p.getPosition());
        var position = FindPosition(PositionName);
        if (position == null) {
            position = new Position(PositionName);
            PositionList.add(position);
        }
        CentralPlayerDatabase.add(p);
        c.addPlayerToClub(p);
        country.incrementCount();
        position.incrementCount();
    }

    public void removePlayerFromCurrentClub(Player p){
        var c = FindClub(p.getClubName());
        c.getPlayerList().remove(p);
    }

    public void transferPlayerToNewClub(Player p, Club before, Club after){
        removePlayerFromCurrentClub(p);
        p.setClubName(after.getName());
        p.setTransferListed(false);
        var jerseyNumber = p.getNumber();
        while (after.NumberTaken.contains(jerseyNumber)) jerseyNumber++;
        p.setNumber(jerseyNumber);
        after.addPlayerToClub(p);
        before.increaseTransferBudget(p.getTransferFee());
        after.decreaseTransferBudget(p.getTransferFee());
    }

    public List<Country> getCountryList() {
        return CountryList;
    }
}
