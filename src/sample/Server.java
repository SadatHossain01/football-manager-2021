package sample;

import DataModel.League;
import DataModel.Player;
import javafx.util.Pair;
import util.FileOperations;
import util.NetworkUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Server {
    private ServerSocket serverSocket;
    private static League FiveASideLeague;
    private HashMap<String, String> clubPasswordList;
    private HashMap<String, NetworkUtil> clubNetworkUtilMap;
    private ArrayList<Player> transferListedPlayers;
    private ArrayList<Pair<String, String>> countryFlagList;
    private ArrayList<Pair<String, String>> clubLogoList;
    private HashMap<String, String> unaccented_accented;

    public Server(int port) throws Exception {
        FiveASideLeague = new League();
        clubNetworkUtilMap = new HashMap<>();
        transferListedPlayers = new ArrayList<>();
        serverSocket = new ServerSocket(port);
        unaccented_accented = new HashMap<>();
        //Reading all the club passwords
        clubPasswordList = FileOperations.readCredentialsOfClubs("src/Assets/Data/LoginCredentials.txt");
        System.out.println("Loaded login credentials of " + clubPasswordList.size() + " clubs");
        //Reading player data
        var loaded = FileOperations.readPlayerDataFromFile("src/Assets/Data/PlayerDatabase.txt"); //file name path starts from one step back of src, but others all start from src
        for (var p : loaded) {
            FiveASideLeague.addPlayerToLeague(p);
            if (p.isTransferListed()) transferListedPlayers.add(p);
//            else if (p.getClubName().equalsIgnoreCase("Free Agent")){
//                p.setTransferListed(false);
//                p.setTransferFee(0);
//                transferListedPlayers.add(p);
//            }
        }
        System.out.println("Loaded data of " + FiveASideLeague.CentralPlayerDatabase.size() + " players and " + FiveASideLeague.getClubList().size() + " clubs");
        //Reading Country Data
        countryFlagList = FileOperations.readFlagLinkOfCountries("src/Assets/Data/CountryFlags.txt");
        System.out.println("Loaded flags of " + countryFlagList.size() + " countries");
        //Reading Club Data
        clubLogoList = FileOperations.readInformationOfClubs("src/Assets/Data/ClubDatabase.txt", FiveASideLeague, unaccented_accented);
//        for (var e : unaccented_accented.entrySet()) System.out.println(e.getKey() + " " + e.getValue());
//        FileOperations.generateClubPasswords("src/Assets/Text/LoginCredentials.txt", FiveASideLeague.getClubList());
        System.out.println("Server up and running");
    }

    public void serve(Socket clientSocket) throws IOException {
        System.out.println("Server accepts a new socket");
        var networkUtil = new NetworkUtil(clientSocket);
        new ReadThreadServer(networkUtil, FiveASideLeague, transferListedPlayers, clubPasswordList, clubNetworkUtilMap, countryFlagList, clubLogoList, unaccented_accented);
    }

    public static void main(String[] args) throws Exception {
        int port = 44444;
        Server server = new Server(port);
        new Thread(()-> {
            Scanner scanner = new Scanner(System.in);
            String next;
            while (true){
                next = scanner.nextLine();
                if (next.strip().equalsIgnoreCase("Stop")){
                    try {
                        FileOperations.writePlayerDataToFile("src/Assets/Data/PlayerDatabase.txt", FiveASideLeague.CentralPlayerDatabase);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        FileOperations.writeClubInformationToFile("src/Assets/Data/ClubDatabase.txt", FiveASideLeague.getClubList());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("Good Bye");
                    System.exit(0);
                }
            }
        }).start();
        while (true){
            var cs = server.serverSocket.accept();
            server.serve(cs);
        }
    }
}
