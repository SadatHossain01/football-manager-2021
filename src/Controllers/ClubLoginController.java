package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import sample.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.sort;

public class ClubLoginController {
    private Main main;

    @FXML
    private PasswordField password;

    @FXML
    private MenuButton clubName;

    public void initiate(Main main){
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
