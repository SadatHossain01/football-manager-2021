package Controllers;

import DTO.PasswordChange;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import sample.Main;
import util.MyAlert;

import java.io.IOException;

public class ChangePasswordController {
    private String current;
    private Main main;

    @FXML
    private PasswordField currentPassword;

    @FXML
    private PasswordField newPassword;

    @FXML
    private PasswordField retypeNewPassword;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initiate(String cur, Main main){
        current = cur;
        this.main = main;
    }

    @FXML
    public void confirm() throws IOException {
        if (!current.equals(currentPassword.getText())){
            new MyAlert(Alert.AlertType.ERROR, "Incorrect Password", "Current passwords do not match").show();
        }
        else if (!newPassword.getText().equals(retypeNewPassword.getText())){
            new MyAlert(Alert.AlertType.ERROR, "Unmatched Passwords", "Please retype your new password correctly").show();
        }
        else if (current.equals(newPassword.getText())){
            new MyAlert(Alert.AlertType.ERROR, "Same Password", "You should input a new password").show();
        }
        else{
            main.myClub.setPassword(newPassword.getText());
            main.myNetworkUtil.write(new PasswordChange(newPassword.getText(), main.myClub.getName()));
        }
    }

    @FXML
    void reset() {
        currentPassword.setText(null);
        newPassword.setText(null);
        retypeNewPassword.setText(null);
    }
}
