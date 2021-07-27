package sample;

import Controllers.*;
import DTO.ClubLoginAuthentication;
import DTO.Request;
import DataModel.Club;
import DataModel.Player;
import javafx.animation.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.CurrentScene;
import util.MyAlert;
import util.NetworkUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Main extends Application {
    public Stage primaryStage, tempStage;
    public Parent RootOfAll;
    public AnchorPane mainPane;
    public ClubDashboardController dashboardController;
    public boolean isFirstTimeCentering = true, isFirstTimeTransition = true;
    public static double screenHeight, screenWidth;
    public InetAddress LocalAddress;
    public Socket socket;
    public int port = 44444;
    public Club myClub, latestCountryWiseCountClub;
    public Image cLogo;
    public Player latestDetailedPlayer;
    public CurrentScene.Type currentPageType, previousPageType;
    public boolean isMainListUpdatePending = false, isTransferListUpdatePending = false;
    public List<Player> transferListedPlayers, latestSearchedPlayers;
    public HashMap<String, String> countryFlagMap;
    public HashMap<String, String> clubLogoMap;
    public NetworkUtil myNetworkUtil;
    public Image seal = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Assets/Image/Stamp1.png")));

    public void initiateApplication() throws IOException {
        var screen = Screen.getPrimary().getBounds();
        screenHeight = screen.getHeight();
        screenWidth = screen.getWidth();
        LocalAddress = InetAddress.getLocalHost();
        showLoginPage();
    }

    public void ConnectToServer(String username, String password) throws IOException {
        myNetworkUtil.write(new ClubLoginAuthentication(username, password));
    }

    public void showAlertMessage(MyAlert myAlert) {
        myAlert.show();
    }

    public Animation numberGeneratingAnimation(int n, Label label){
        return new Transition() {
            {
                setCycleDuration(Duration.millis(2000));
            }

            @Override
            protected void interpolate(double frac) {
                final int n1 = Math.round(n * (float) frac);
                label.setText(String.valueOf(n1));
            }
        };
    }

    public void showLoginPage() throws IOException {
        socket = new Socket(LocalAddress, port);
        myNetworkUtil = new NetworkUtil(socket);
        new ReadThreadClient(this);
        currentPageType = CurrentScene.Type.LoginPage;
        var loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ViewFX/ClubLoginView.fxml"));
        Parent root = loader.load();
        ClubLoginController controller = loader.getController();
        //name transition starts
        if (isFirstTimeTransition) {
            final String content = "Football Manager 2021";
            final Animation titleAnimation = new Transition() {
                {
                    setCycleDuration(Duration.millis(2000));
                }

                @Override
                protected void interpolate(double frac) {
                    final int length = content.length();
                    final int n = Math.round(length * (float) frac);
                    controller.appName.setText(content.substring(0, n));
                }
            };
            titleAnimation.play();
            var anim1 = numberGeneratingAnimation(16707, controller.playerCount);
            var anim2 = numberGeneratingAnimation(661, controller.clubCount);
            var anim3 = numberGeneratingAnimation(163, controller.countryCount);
            titleAnimation.setOnFinished(event -> {
                controller.l1.setText("Players");
                anim1.play();
            });
            anim1.setOnFinished(event -> {
                controller.l2.setText("Clubs");
                anim2.play();
            });
            anim2.setOnFinished(event -> {
                controller.l3.setText("Countries");
                anim3.play();
            });
            anim3.setOnFinished(event -> isFirstTimeTransition = false);
        }
        //ends
        controller.initiate(this);
        controller.setClubClient(this);
        var scene = new javafx.scene.Scene(root);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    controller.SignIn();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.setOpacity(0.94);
        primaryStage.setOnCloseRequest(event -> {
            try {
                myNetworkUtil.write(new Request((myClub == null ? "" : myClub.getName()), Request.Type.IAmOut));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        });
        primaryStage.setTitle("Login Page");
        primaryStage.setResizable(false);
        if (isFirstTimeCentering) {
            primaryStage.centerOnScreen();
            isFirstTimeCentering = false;
        }
        primaryStage.show();
    }

    public void showClubHomePage(Club c) throws IOException {
        var loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ViewFX/ClubDashboardView.fxml"));
        RootOfAll = loader.load();
        primaryStage.setScene(new javafx.scene.Scene(RootOfAll));
        dashboardController = loader.getController();
        dashboardController.setMain(this);
        dashboardController.initiate(c);
        showMyPlayers();
    }

    public void resetAllButtons() {
        dashboardController.myPlayerButton.setStyle("-fx-background-color: #FED45F; -fx-background-radius: 15 15 15 15");
        dashboardController.marketplaceButton.setStyle("-fx-background-color: #FED45F; -fx-background-radius: 15 15 15 15");
        dashboardController.searchPlayerButton.setStyle("-fx-background-color: #FED45F; -fx-background-radius: 15 15 15 15");
    }

    public void showMyPlayers() throws IOException {
        resetAllButtons();
        mainPane.getChildren().clear();
        dashboardController.myPlayerButton.setStyle("-fx-background-color: #DA3A34; -fx-background-radius: 15 15 15 15");
        currentPageType = CurrentScene.Type.ShowMyPlayers;
        displayList(myClub.getPlayerList(), PlayerListViewController.PageType.SimpleList);
    }

    public void showSearchPage() throws IOException {
        currentPageType = CurrentScene.Type.ShowSearchOptions;
        resetAllButtons();
        mainPane.getChildren().clear();
        dashboardController.searchPlayerButton.setStyle("-fx-background-color: #DA3A34; -fx-background-radius: 15 15 15 15");
        var loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ViewFX/PlayerSearchView.fxml"));
        Parent root = loader.load();
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(750), root);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setCycleCount(1);
        fadeTransition.setInterpolator(Interpolator.EASE_OUT);
        fadeTransition.play();
        mainPane.getChildren().add(root);
        PlayerSearchController searchController = loader.getController();
        searchController.setMain(this);
        searchController.initiate(myClub);
        primaryStage.setTitle("Player Search Options");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void showCountryWiseCount(Stage stage, Club club) throws IOException {
        previousPageType = currentPageType;
        currentPageType = CurrentScene.Type.ShowCountryWiseCount;
        latestCountryWiseCountClub = club;
        tempStage = stage;
        var loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ViewFX/CountryListView.fxml"));
        Parent root = loader.load();
        CountryListViewController c = loader.getController();
        c.setStage(stage);
        c.setMain(this);
        c.initiate(club.getCountryWisePlayerCount());
        var scene = new javafx.scene.Scene(root);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> currentPageType = previousPageType);
        stage.setTitle("Country Wise Player Count");
        stage.setResizable(false);
        if (!stage.isShowing()) {
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
    }

    public void showPlayerDetail(Stage stage, Player player) throws IOException {
        latestDetailedPlayer = player;
        tempStage = stage;
        previousPageType = currentPageType;
        currentPageType = CurrentScene.Type.ShowAPlayerDetail;
        var loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ViewFX/PlayerCardView.fxml"));
        Parent root = loader.load();
        PlayerCardController p = loader.getController();
        p.setMain(this);
        p.setStage(stage);
        p.initiate(player);
        var scene = new javafx.scene.Scene(root);
        stage.setOnCloseRequest(event -> {
            try {
                if (isTransferListUpdatePending) {
                    refreshPage(CurrentScene.Type.ShowMarketPlayers);
                    isTransferListUpdatePending = false;
                }
                else if (isMainListUpdatePending){
                    refreshPage(CurrentScene.Type.ShowMyPlayers);
                    isMainListUpdatePending = false;
                }
                else currentPageType = previousPageType;
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(750), root);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setCycleCount(1);
        fadeTransition.setInterpolator(Interpolator.EASE_IN);
        fadeTransition.play();
        stage.setScene(scene);
        stage.setTitle("Player Detail");
        stage.setResizable(false);
        if (!stage.isShowing()) {
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
    }

    public void displayList(List<Player> playerList, PlayerListViewController.PageType pageType) throws IOException {
        mainPane.getChildren().clear();
        var loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ViewFX/PlayerListView.fxml"));
        Parent root = loader.load();
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(750), root);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setCycleCount(1);
        fadeTransition.setInterpolator(Interpolator.EASE_BOTH);
        fadeTransition.play();
        mainPane.getChildren().add(root);
        PlayerListViewController playerListViewController = loader.getController();
        playerListViewController.setMain(this);
        playerListViewController.initiate(playerList, pageType);
        primaryStage.setTitle("Player List Display");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void AskForTransferFee(MinimalPlayerDetailController singlePlayerDetailController) throws IOException {
        Stage stage = new Stage();
        tempStage = stage;
        previousPageType = currentPageType;
        currentPageType = CurrentScene.Type.AskForTransferFee;
        var loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ViewFX/AskForTransferFee.fxml"));
        Parent root = loader.load();
        AskForTransferFeeController askForTransferFeeController = loader.getController();
        askForTransferFeeController.setMain(this);
        askForTransferFeeController.setStage(stage);
        askForTransferFeeController.initiate(singlePlayerDetailController);
        var scene = new javafx.scene.Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    askForTransferFeeController.confirmListing();
                    if (isMainListUpdatePending) {
                        refreshPage(CurrentScene.Type.ShowMyPlayers);
                        isMainListUpdatePending = false;
                    }
                    else currentPageType = CurrentScene.Type.ShowMyPlayers;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        stage.setOpacity(0.9);
        stage.setOnCloseRequest(event -> {
            try {
                if (isMainListUpdatePending) {
                    refreshPage(CurrentScene.Type.ShowMyPlayers);
                    isMainListUpdatePending = false;
                }
                else currentPageType = CurrentScene.Type.ShowMyPlayers;
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        stage.setScene(scene);
        stage.setTitle("Transfer Fee Input");
        stage.setResizable(false);
        stage.centerOnScreen();
        if (!stage.isShowing()) {
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
    }

    public void showBuyablePlayers() throws IOException {
        currentPageType = CurrentScene.Type.ShowMarketPlayers;
        resetAllButtons();
        mainPane.getChildren().clear();
        dashboardController.marketplaceButton.setStyle("-fx-background-color: #DA3A34; -fx-background-radius: 15 15 15 15");
        displayList(transferListedPlayers, PlayerListViewController.PageType.TransferList);
    }

    public void refreshPage(CurrentScene.Type pageType) throws IOException {
        if (pageType == CurrentScene.Type.ShowMyPlayers) showMyPlayers();
        else if (pageType == CurrentScene.Type.ShowMarketPlayers) showBuyablePlayers();
        else if (pageType == CurrentScene.Type.ShowSearchedPlayers){
            //refining the list
            List<Player> newList = new ArrayList<>();
            List<Player> clubPlayerList = myClub.getPlayerList();
            for (var p : latestSearchedPlayers){
                if (clubPlayerList.contains(p)) newList.add(p);
            }
            latestSearchedPlayers = newList;
            displayList(newList, PlayerListViewController.PageType.SimpleList);
        }
        else if (pageType == CurrentScene.Type.ShowCountryWiseCount){
            showCountryWiseCount(tempStage, latestCountryWiseCountClub);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        initiateApplication();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
