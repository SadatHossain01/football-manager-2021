package util;

import DataModel.Club;
import DataModel.League;
import DataModel.Player;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static DataModel.Club.decodeSalaryString;


public class FileOperations {
    public static List<Player> readPlayerDataFromFile(String FILE_NAME) throws Exception {
        List<Player> playerList = new ArrayList<>();
        BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_NAME), "windows-1252"));
        while (true) {
            String line = input.readLine();
            if (line == null)
                break;
            String[] tokens = line.split(";");
            try {
//                String name, String unAccentedName, String clubName, String country, int age, double estimatedValue, int number, String position, double height, double weight, String pFoot, double weeklySalary, String imageSource, int isTransferListed, double transferFee
                Player p = new Player(tokens[0], tokens[1], tokens[2], tokens[3], Integer.parseInt(tokens[4]), Club.decodeSalaryString(tokens[5]), Integer.parseInt(tokens[6]), tokens[7], Double.parseDouble(tokens[8]), Double.parseDouble(tokens[9]), tokens[10], Club.decodeSalaryString(tokens[11]), tokens[12], Integer.parseInt(tokens[13]), Club.decodeSalaryString(tokens[14]));
                playerList.add(p);
            } catch (Exception e) {
                System.out.println(e);
                System.out.println(line);
            }
        }
        input.close();
        return playerList;
    }

    public static HashMap<String, String> readCredentialsOfClubs(String FILE_NAME) throws IOException {
        HashMap<String, String> clubPasswords = new HashMap<>();
        BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_NAME)));
        while (true) {
            String line = input.readLine();
            if (line == null) break;
            try {
                String[] tokens = line.split(";");
                String username = tokens[0];
                String password = tokens[2];
                clubPasswords.put(username, password);
            } catch (Exception ignored) {
            }
        }
        input.close();
        return clubPasswords;
    }

    public static ArrayList<Pair<String, String>> readFlagLinkOfCountries(String FILE_NAME) throws IOException {
        ArrayList<Pair<String, String>> countryFlagList = new ArrayList<>();
        BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_NAME)));
        while (true) {
            String line = input.readLine();
            if (line == null) break;
            try {
                String[] tokens = line.split(";");
                String name = tokens[1];
                String flagLink = tokens[2];
                countryFlagList.add(new Pair<>(name, flagLink));
            } catch (Exception ignored) {
            }
        }
        input.close();
        return countryFlagList;
    }

    public static ArrayList<Pair<String, String>> readInformationOfClubs(String FILE_NAME, League league, HashMap<String, String> unaccented_accented) throws IOException {
        ArrayList<Pair<String, String>> clubLogoList = new ArrayList<>();
        BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_NAME), "windows-1252"));
        while (true) {
            String line = input.readLine();
            if (line == null) break;
            try {
                String[] tokens = line.split(";");
                String name = tokens[0];
                String unAccentedName = tokens[1];
                double worth = decodeSalaryString(tokens[2]);
                double budget = decodeSalaryString(tokens[3]);
                String logoLink = tokens[4];
                clubLogoList.add(new Pair<>(name, logoLink));
                var club = league.FindClub(name);
                if (club != null) {
                    club.setWorth(worth);
                    club.setTransferBudget(budget);
                    club.setLogoLink(logoLink);
                    club.setUnAccentedName(unAccentedName);
                    unaccented_accented.put(unAccentedName, club.getName());
                }
            } catch (Exception e) {
                System.out.println(line);
                e.printStackTrace();
            }
        }
        input.close();
        return clubLogoList;
    }

    public static void writePlayerDataToFile(String FILE_NAME, List<Player> playerList) throws Exception {
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_NAME), "windows-1252"));
        for (Player p : playerList) {
//            Lionel Messi;Lionel Messi;FC Barcelona;Argentina;33;€103.5M;10;RW ST CF;170;72;Left;€560K;https://cdn.fifacm.com/content/media/imgs/fifa21/players/p158023.png?v=22;0;€0
            output.write(p.getName() + ";" + p.getUnAccentedName() + ";" + p.getClubName() + ";" + p.getCountry() + ";"
                    + p.getAge() + ";" + Club.showCurrency(p.getEstimatedValue()) + ";" + p.getNumber() + ";" + p.getPosition() + ";" + p.getHeight() + ";" +
                    p.getWeight() + ";" + p.getPreferredFoot() + ";" + Club.showCurrency(p.getWeeklySalary()) + ";" + p.getImageSource() + ";" +
                    (p.isTransferListed() ? 1 : 0) + ";" + Club.showCurrency(p.getTransferFee()));
            output.write("\n");
        }
        output.close();
    }

    public static void generateClubPasswords(String FILE_NAME, List<Club> clubList) throws Exception {
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_NAME)));
        for (Club c : clubList) {
            output.write(c.getName() + ";" + c.getUnAccentedName().toLowerCase());
            output.write("\n");
        }
        output.close();
    }

    public static void writeClubInformationToFile(String FILE_NAME, List<Club> clubList) throws Exception {
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_NAME), "windows-1252"));
        for (Club c : clubList) {
            String buffer = c.getName() + ";" + c.getUnAccentedName() + ";" + Club.showCurrency(c.getWorth()) + ";" + Club.showCurrency(c.getTransferBudget()) + ";" + c.getLogoLink();
            output.write(buffer);
            output.write("\n");
        }
        output.close();
    }
}
