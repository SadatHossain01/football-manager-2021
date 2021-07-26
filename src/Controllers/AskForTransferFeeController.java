package Controllers;

import DTO.SellRequest;
import DataModel.Club;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.Main;
import util.CurrentScene;
import util.MyAlert;

import java.io.IOException;
import java.util.Objects;

public class AskForTransferFeeController {
    private Main main;
    private MinimalPlayerDetailController minimalPlayerDetailController;
    private Stage stage;
    private String playerName;
    private double marketValue;

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private AnchorPane Pane;

    @FXML
    private TextField AskedTransferFee;

    @FXML
    private Label pName;

    @FXML
    private MenuButton multiplier;

    @FXML
    private Label estimatedValue;

    public void initiate(MinimalPlayerDetailController minimalPlayerDetailController){
        this.minimalPlayerDetailController = minimalPlayerDetailController;
        this.playerName = minimalPlayerDetailController.getPlayer().getName();
        this.marketValue = minimalPlayerDetailController.getPlayer().getEstimatedValue();
        pName.setText(playerName);
        estimatedValue.setText("Market Value: " + Club.showCurrency(marketValue));
    }

    public void confirmListing() throws IOException {
        double transferFee;
        double mul;

        if (multiplier.getText() == null || multiplier.getText().isEmpty()) mul = 1;
        else if (multiplier.getText().equalsIgnoreCase("K")) mul = 1e3;
        else mul = 1e6;

        try {
            transferFee = Double.parseDouble(AskedTransferFee.getText()) * mul;
            if (transferFee <= 0) throw new Exception();
        } catch (Exception e){
            new MyAlert(Alert.AlertType.ERROR, MyAlert.MessageType.InvalidSalaryInput).show();
            return;
        }
        var player = minimalPlayerDetailController.getPlayer();
        var transferLabel = minimalPlayerDetailController.getTransferLabel();
        var transferButton = minimalPlayerDetailController.getTransferButton();
        var fee = minimalPlayerDetailController.getFee();
        player.setTransferFee(transferFee);
        player.setTransferListed(true);
        transferLabel.setImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/Assets/Image/Stamp1.png"))));
        transferButton.setDisable(true);
        fee.setText(Club.showCurrency(transferFee));
        main.transferListedPlayers.add(player);
        main.myNetworkUtil.write(new SellRequest(player.getName(), main.myClub.getName(), transferFee));
        stage.close();
    }

    @FXML
    void confirmTransferListing(ActionEvent event) throws IOException {
        confirmListing();
        if (main.isMainListUpdatePending){
            main.refreshPage(CurrentScene.Type.ShowMyPlayers);
            main.isMainListUpdatePending = false;
        }
        else main.currentPageType = CurrentScene.Type.ShowMyPlayers;
    }

    @FXML
    void setK(ActionEvent event) {
        multiplier.setText("K");
    }

    @FXML
    void setM(ActionEvent event) {
        multiplier.setText("M");
    }

    @FXML
    void setNull(ActionEvent event) {
        multiplier.setText("");
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
