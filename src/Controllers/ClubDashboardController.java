package Controllers;

import DTO.Request;
import DataModel.Club;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import sample.Main;

import java.io.IOException;

public class ClubDashboardController {
    private Main main;
    private Club club;

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private Label clubName;

    @FXML
    private ImageView clubLogo;

    @FXML
    public JFXButton myPlayerButton;

    @FXML
    public JFXButton searchPlayerButton;

    @FXML
    public JFXButton marketplaceButton;

    @FXML
    public JFXButton logoutButton;

    @FXML
    public Label budget;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label clubWorth;

    public void initiate(Club club){
        main.mainPane = anchorPane;
        main.dashboardController = this;
        this.club = club;
        clubName.setText(club.getName());
        budget.setText("Budget: " + Club.showCurrency(club.getTransferBudget()));
        clubWorth.setText("Worth: " + Club.showCurrency(club.getWorth()));
        clubLogo.setImage(main.cLogo);
    }

    @FXML
    void LogOut(ActionEvent event) throws IOException {
        main.myNetworkUtil.write(new Request(main.myClub.getName(), Request.Type.LogOut));
        if (main.tempStage != null && main.tempStage.isShowing()) main.tempStage.close();
        main.showLoginPage();
    }

    @FXML
    void ShowBuyablePlayers(ActionEvent event) throws IOException {
        main.showBuyablePlayers();
    }

    @FXML
    void ShowMyPlayers(ActionEvent event) throws IOException {
        main.showMyPlayers();
    }

    @FXML
    void ShowSearchOptions(ActionEvent event) throws IOException {
        main.showSearchPage();
    }

}
