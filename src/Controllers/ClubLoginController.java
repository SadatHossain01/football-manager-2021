package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import sample.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.sort;

public class ClubLoginController {
    private Main main;

    @FXML
    public Label appName;

    @FXML
    public Label playerCount;

    @FXML
    public Label clubCount;

    @FXML
    public Label countryCount;

    @FXML
    public Label l1;

    @FXML
    public Label l2;

    @FXML
    public Label l3;

    @FXML
    private PasswordField password;

    @FXML
    private MenuButton clubName;

    public void initiate(Main main){
        if (!main.isFirstTimeTransition){
            appName.setText("Football Manager 2021");
            playerCount.setText("16707");
            l1.setText("Players");
            clubCount.setText("661");
            l2.setText("Clubs");
            countryCount.setText("163");
            l3.setText("Countries");
        }
        this.main = main;
        List<String> all = new ArrayList<>();
        for (var c : main.clubLogoMap.entrySet()){
            all.add(c.getKey());
        }
        sort(all);
        for (var any : all){
            var item = new MenuItem(any);
            item.setOnAction(event -> clubName.setText(any));
            clubName.getItems().add(item);
        }
    }

    public void SignIn() throws IOException {
        String name = clubName.getText();
        String pass = password.getText();
        main.ConnectToServer(name, pass);
    }

    @FXML
    void DoSignIn(ActionEvent event) throws IOException {
        SignIn();
    }

    public void setClubClient(Main main) {
        this.main = main;
    }
}
