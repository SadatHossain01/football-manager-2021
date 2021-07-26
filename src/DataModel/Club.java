package DataModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static DataModel.League.FormatName;

public class Club implements Serializable {
    private String name;
    private String unAccentedName;
    private String logoLink;
    private double TransferBudget, Worth;
    private List<Player> PlayerList;
    public List<Integer> NumberTaken;

    public Club(String name) {
        setName(name);
        PlayerList = new ArrayList<>();
        NumberTaken = new ArrayList<>();
    }

    public Club(Club club){
        this.name = club.name;
        this.unAccentedName = club.getUnAccentedName();
        this.logoLink = club.logoLink;
        this.TransferBudget = club.TransferBudget;
        this.PlayerList = new ArrayList<>(PlayerList);
        this.NumberTaken = new ArrayList<>(NumberTaken);
        this.Worth = club.Worth;
    }

    public void setLogoLink(String logoLink) {
        this.logoLink = logoLink;
    }

    public String getLogoLink() {
        return logoLink;
    }

    public void setName(String name) {
        this.name = FormatClubName(name);
    }

    public String getName() {
        return name;
    }

    public double getTransferBudget() {
        return TransferBudget;
    }

    public void setTransferBudget(double transferBudget) {
        TransferBudget = transferBudget;
    }

    public double getWorth() {
        return Worth;
    }

    public void setWorth(double worth) {
        Worth = worth;
    }

    public void increaseTransferBudget(double increment){
        TransferBudget += increment;
    }

    public void decreaseTransferBudget(double decrement){
        TransferBudget -= decrement;
    }

    public String getUnAccentedName() {
        return unAccentedName;
    }

    public void setUnAccentedName(String unAccentedName) {
        this.unAccentedName = unAccentedName;
    }

    public List<Player> getPlayerList(){return PlayerList;}

    public List<Player> SearchMaximumSalary() {
        List<Player> wantedPlayers = new ArrayList<>();
        double maxSalary = 0;
        for (var player : PlayerList) maxSalary = Math.max(maxSalary, player.getWeeklySalary());
        for (var player : PlayerList) {
            if (player.getWeeklySalary() == maxSalary) {
                wantedPlayers.add(player);
            }
        }
        return wantedPlayers;
    }

    public List<Player> SearchMaximumAge() {
        List<Player> wantedPlayers = new ArrayList<>();
        double maxAge = 0;
        for (var player : PlayerList) maxAge = Math.max(maxAge, player.getAge());
        for (var player : PlayerList) {
            if (player.getAge() == maxAge) {
                wantedPlayers.add(player);
            }
        }
        return wantedPlayers;
    }

    public List<Player> SearchMaximumHeight() {
        List<Player> wantedPlayers = new ArrayList<>();
        double maxHeight = 0;
        for (var player : PlayerList) maxHeight = Math.max(maxHeight, player.getHeight());
        for (var player : PlayerList) {
            if (player.getHeight() == maxHeight) {
                wantedPlayers.add(player);
            }
        }
        return wantedPlayers;
    }

    public double TotalYearlySalary() {
        double total_salary = 0;
        for (var player : PlayerList) total_salary += player.getWeeklySalary();
        return total_salary * 52;
    }

    public void addPlayerToClub(Player p) {
        //assuming the club size check is done in main
        PlayerList.add(p);
        NumberTaken.add(p.getNumber());
    }

    public HashMap<String, Integer> getCountryWisePlayerCount() {
        HashMap<String, Integer> countryWiseCount = new HashMap<>();
        for (var p : PlayerList){
            var c = p.getCountry();
            if (countryWiseCount.containsKey(c)){
                countryWiseCount.put(p.getCountry(), countryWiseCount.get(p.getCountry()) + 1);
            }
            else{
                countryWiseCount.put(p.getCountry(), 1);
            }
        }
        return countryWiseCount;
    }

    public List<Player> SearchByNameInClub(String name) {
        List<Player> ans = new ArrayList<>();
        String FormattedName = FormatName(name);
        for (var p : PlayerList) {
            if (p.getName().equalsIgnoreCase(FormattedName)) {
                ans.add(p);
                return ans;
            }
        }
        return null;
    }

    public List<Player> SearchByUnaccentedNameInClub(String name) {
        List<Player> ans = new ArrayList<>();
        String FormattedName = FormatName(name);
        for (var p : PlayerList) {
            if (p.getName().equalsIgnoreCase(FormattedName) || p.getUnAccentedName().equalsIgnoreCase(FormattedName)) {
                ans.add(p);
                return ans;
            }
        }
        return null;
    }

    public List<Player> SearchPlayerByCountryInClub(String country) {
        String FormattedCountryName = FormatName(country);
        List<Player> wantedPlayers = new ArrayList<>();
        for (var p : PlayerList) {
            if ((country.equalsIgnoreCase("ANY") || p.getCountry().equalsIgnoreCase(FormattedCountryName))) {
                wantedPlayers.add(p);
            }
        }
        return wantedPlayers;
    }

    public List<Player> SearchPlayerBySalaryInClub(double lowRange, double highRange) {
        List<Player> wantedPlayers = new ArrayList<>();
        for (var p : PlayerList) {
            double salary = p.getWeeklySalary();
            if ((lowRange == -1 || lowRange <= salary) && (highRange == -1 || salary <= highRange)) {
                wantedPlayers.add(p);
            }
        }
        return wantedPlayers;
    }

    public List<Player> SearchPlayerByPositionInClub(String position) {
        List<Player> wantedPlayers = new ArrayList<>();
        List<String> applicablePositions;
        if (position.equalsIgnoreCase("Forward")) applicablePositions = List.of("CF", "ST", "LW", "RW");
        else if (position.equalsIgnoreCase("Midfielder")) applicablePositions = List.of("CDM", "RM", "LM", "CM", "CAM");
        else if (position.equalsIgnoreCase("Defender")) applicablePositions = List.of("CB", "LB", "RB", "RWB", "LWB");
        else applicablePositions = List.of("GK");
        for (var p : PlayerList) {
            var positionsOfThisPlayer = p.getPosition().split(" ");
            for (var each : positionsOfThisPlayer) if (applicablePositions.contains(each)) {
                wantedPlayers.add(p);
                break;
            }
        }
        return wantedPlayers;
    }

    public static String showCurrency(double salaryNumber){
        String salary;
        double num;
        String suffix;
        if (salaryNumber >= 1e9){
            num = salaryNumber/(1e9);
            suffix = "B";
        }
        else if (salaryNumber >= 1e6){
            num = salaryNumber/(1e6);
            suffix = "M";
        }
        else if (salaryNumber >= 1e3){
            num = salaryNumber/(1e3);
            suffix = "K";
        }
        else{
            num = salaryNumber;
            suffix = "";
        }
        num = Math.round(num*1000.0)/1000.0;
        salary = "€" + num + suffix;
        return salary;
    }

    public static double decodeSalaryString(String salary){
        int len = salary.length();
        if (len == 2){
            return Double.parseDouble(salary.substring(1, 2));
        }
        double v = Double.parseDouble(salary.substring(1, len - 1));
        if (salary.endsWith("B")) return 1e9 * v;
        else if (salary.endsWith("M")) return 1e6 * v;
        else if (salary.endsWith("K")) return 1e3 * v;
        else return Double.parseDouble(salary.substring(1, len));
    }

    public Player FindPlayerInList(String PlayerName, List<Player> list) { //will return null if club is not registered yet
        Player wanted = null;
        String FormattedPlayerName = FormatName(PlayerName);
        for (var p : list) {
            if (p.getName().equalsIgnoreCase(FormattedPlayerName)) {
                wanted = p;
                break;
            }
        }
        return wanted;
    }

    public String FormatClubName(String name) {
        return FormatName(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
