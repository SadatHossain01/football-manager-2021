package sample;

import DTO.*;
import DataModel.Club;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import util.CurrentScene;
import util.MyAlert;
import util.NetworkUtil;

import java.io.IOException;
import java.util.HashMap;

public class ReadThreadClient implements Runnable {
    private Thread t;
    private Club c;
    private Main main;
    private NetworkUtil clientNetworkUtil;
    private boolean isThreadOn = true;

    public ReadThreadClient(Main main) {
        this.main = main;
        clientNetworkUtil = main.myNetworkUtil;
        t = new Thread(this, "Read Thead Client");
        t.start();
    }

    @Override
    public void run() {
        while (isThreadOn) {
            Object next = null;
            while (isThreadOn) {
                try {
                    next = clientNetworkUtil.read();
                    break;
                } catch (IOException | ClassNotFoundException ignored) {
                }
            }
            if (next instanceof RequestResponse) {
                var type = ((RequestResponse) next).type;
                if (type == RequestResponse.Type.AlreadyLoggedIn) {
                    Platform.runLater(() -> main.showAlertMessage(new MyAlert(Alert.AlertType.ERROR, "Already Logged In", "Sorry, this club is already logged in to the system")));
                } else if (type == RequestResponse.Type.UsernameNotRegistered) {
                    Platform.runLater(() -> main.showAlertMessage(new MyAlert(Alert.AlertType.ERROR, "Unregistered club", "Sorry, this club is not registered to the system")));
                } else if (type == RequestResponse.Type.IncorrectPassword) {
                    Platform.runLater(() -> main.showAlertMessage(new MyAlert(Alert.AlertType.ERROR, "Incorrect password", "Sorry, the password you entered is incorrect")));
                } else if (type == RequestResponse.Type.InsufficientTransferBudget) {
                    Platform.runLater(() -> main.showAlertMessage(new MyAlert(Alert.AlertType.ERROR, "Insufficient budget", "Sorry, you do not have sufficient budget to buy this player")));
                } else if (type == RequestResponse.Type.AlreadyBought) {
                    Platform.runLater(() -> main.showAlertMessage(new MyAlert(Alert.AlertType.ERROR, "Player not for sale anymore", "Sorry, this player has been already bought")));
                }
            } else if (next instanceof Club) {
                c = (Club) next;
                main.myClub = c;
                try {
                    main.cLogo = new Image(main.myClub.getLogoLink(), true);
                } catch (Exception e) {
                    main.cLogo = new Image("https://www.shopinimizaj.com/frontend/web/images/no-image.png", true);
                }
                Platform.runLater(() ->
                {
                    try {
                        main.showClubHomePage(c);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } else if (next instanceof NewPlayerPurchased) {
                var p = ((NewPlayerPurchased) next).getPlayer();
                String buyerName = ((NewPlayerPurchased) next).getBuyer();
                String sellerName = ((NewPlayerPurchased) next).getSeller();
                if (c.getName().equalsIgnoreCase(buyerName)) {
                    //so I am the buyer club
                    //this player has to be added to main player list and removed from transfer list
                    //adding to my players
                    c.addPlayerToClub(p);
                    var toBeRemovedFromTransferList = c.FindPlayerInList(p.getName(), main.transferListedPlayers);
                    //removing from transfer list
                    main.transferListedPlayers.remove(toBeRemovedFromTransferList);
                    //changing the transfer status of the player and budget of the club
                    p.setClubName(c.getName());
                    p.setTransferListed(false);
                    c.decreaseTransferBudget(p.getTransferFee());
                    Platform.runLater(() -> main.dashboardController.budget.setText("Budget: " + Club.showCurrency(c.getTransferBudget()))
                    );
                    var currentPageType = main.currentPageType;
                    if (currentPageType == CurrentScene.Type.AskForTransferFee){
                        //so the previous page was MyPlayers, and to that list one new player has been added because of buying
                        //by the way, this condition is probably never going to be true as buying is only possible when you press the buy button.
                        //You are in a AskForFee window, it is not possible that a player is bought in that moment
                        main.isMainListUpdatePending = true;
                    }
                    //if the current page is ShowPlayerDetail, two cases can happen
                    //if a third team buys or sells this player, we will deal with that in UpdatedList response
                    else if (currentPageType == CurrentScene.Type.ShowAPlayerDetail){
                        if (main.previousPageType == CurrentScene.Type.ShowMyPlayers) {
                            //even this is not going to happen because of the previous reason
                            main.isMainListUpdatePending = true;
                        }
                        else if (main.previousPageType == CurrentScene.Type.ShowMarketPlayers) {
                            System.out.println("Previous page type was ShowMarketPlayers and isTransferListPending set to true");
                            main.isTransferListUpdatePending = true;
                        }
                    }
                    else {
                        Platform.runLater(() -> {
                            //only this condition is going to be true
                                    try {
                                        main.refreshPage(main.currentPageType);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                        );
                    }
                }
                else if (c.getName().equalsIgnoreCase(sellerName)) {
                    //I am the seller club
                    var playerInSellingClubList = c.FindPlayerInList(p.getName(), c.getPlayerList());
                    //player needs to be removed from both local and transfer list
                    c.getPlayerList().remove(playerInSellingClubList);
                    main.transferListedPlayers.remove(playerInSellingClubList);
                    c.increaseTransferBudget(p.getTransferFee());
                    Platform.runLater(() -> main.dashboardController.budget.setText("Budget: " + Club.showCurrency(c.getTransferBudget()))
                    );
                    var currentPageType = main.currentPageType;
                    if (currentPageType == CurrentScene.Type.AskForTransferFee){
                        //so the previous page was MyPlayers, and from that list one player has to be removed as a result of selling
                        main.isMainListUpdatePending = true;
                    }
                    //if the current page is ShowPlayerDetail, two cases can happen
                    else if (currentPageType == CurrentScene.Type.ShowAPlayerDetail){
                        if (main.previousPageType == CurrentScene.Type.ShowMyPlayers) {
                            if (p.getName().equalsIgnoreCase(main.latestDetailedPlayer.getName())){
                                //this player has been sold, you should no more be able to see him, he is no more yours
                                Platform.runLater(()->{
                                    main.tempStage.close();
                                    try {
                                        main.refreshPage(CurrentScene.Type.ShowMyPlayers);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                            else main.isMainListUpdatePending = true;
                        }
                        else if (main.previousPageType == CurrentScene.Type.ShowMarketPlayers) main.isTransferListUpdatePending = true;
                    }
                    else {
                        Platform.runLater(() -> {
                                    try {
                                        main.refreshPage(main.currentPageType);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                        );
                    }
                }
            } else if (next instanceof UpdatedTransferList) {
                main.transferListedPlayers = ((UpdatedTransferList) next).getPlayerList();
                var currentPageType = main.currentPageType;
                var previousPageType = main.previousPageType;
                if (currentPageType == CurrentScene.Type.ShowAPlayerDetail){
                    if (previousPageType == CurrentScene.Type.ShowMarketPlayers){
                        var pp = c.FindPlayerInList(main.latestDetailedPlayer.getName(), main.transferListedPlayers);
                        if (pp == null){
                            //this player has been bought, so you should not be able to see him any more
                            Platform.runLater(()->{
                                main.tempStage.close();
                                try {
                                    main.refreshPage(CurrentScene.Type.ShowMarketPlayers);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                        else main.isTransferListUpdatePending = true;
                    }
                }
                else if (currentPageType == CurrentScene.Type.ShowMarketPlayers){
                    Platform.runLater(()->{
                        try {
                            main.refreshPage(CurrentScene.Type.ShowMarketPlayers);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            } else if (next instanceof ClubCountryImageData) {
                var countryList = ((ClubCountryImageData) next).getCountryFlagList();
                main.countryFlagMap = new HashMap<>();
                main.clubLogoMap = new HashMap<>();
                for (var c : countryList) {
                    main.countryFlagMap.put(c.getKey(), c.getValue());
                }
                var clubList = ((ClubCountryImageData) next).getClubLogoList();
                for (var c : clubList) {
                    main.clubLogoMap.put(c.getKey(), c.getValue());
                }
            }
        }
    }
}
