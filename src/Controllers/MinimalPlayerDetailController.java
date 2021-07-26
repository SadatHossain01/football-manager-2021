package Controllers;

import DTO.BuyRequest;
import DataModel.Club;
import DataModel.Player;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Main;

import java.io.IOException;

public class MinimalPlayerDetailController {
    private PlayerListViewController.PageType pageType;
    private Player player;
    private Main main;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private Label playerName;

    @FXML
    private Label clubName;

    @FXML
    private Label countryName;

    @FXML
    private Label age;

    @FXML
    private Label position;

    @FXML
    private Label salary;

    @FXML
    private Label fee;

    @FXML
    public VBox playerPane;

    @FXML
    private ImageView transferLabel;

    @FXML
    private JFXButton transferButton;

    public Label getFee() {
        return fee;
    }

    public void setFee(Label fee) {
        this.fee = fee;
    }

    public ImageView getTransferLabel() {
        return transferLabel;
    }

    public JFXButton getTransferButton() {
        return transferButton;
    }

    public VBox initiate(Player p, PlayerListViewController.PageType pageType) {
        this.pageType = pageType;
        this.player = p;
        playerName.setText(p.getName());
        clubName.setText(p.getClubName());
        countryName.setText(p.getCountry());
        age.setText(String.valueOf(p.getAge()));
        position.setText(p.getPosition());
        salary.setText(Club.showCurrency(p.getWeeklySalary()));
        if (pageType == PlayerListViewController.PageType.SimpleList){
            transferButton.setText("Sell Player");
            if (player.isTransferListed()) transferButton.setDisable(true);
        }
        else if (pageType == PlayerListViewController.PageType.TransferList) {
            transferButton.setText("Buy Player");
            if (player.getClubName().equalsIgnoreCase(main.myClub.getName())) transferButton.setDisable(true);
        }
        if (p.isTransferListed()){
            fee.setText(Club.showCurrency(p.getTransferFee()));
            transferLabel.setImage(main.seal);
        }
        else {
            fee.setText("N/A");
            transferLabel.setImage(null);
        }
        return playerPane;
    }

    @FXML
    void DoTransferAction(ActionEvent event) throws IOException {
        if (pageType == PlayerListViewController.PageType.SimpleList) {
            main.AskForTransferFee(this);
        } else {
            main.myNetworkUtil.write(new BuyRequest(player.getName(), player.getClubName(), main.myClub.getName()));
        }
    }

    @FXML
    void showPlayerDetails(ActionEvent event) throws IOException {
        main.showPlayerDetail(new Stage(), player);
    }
}
