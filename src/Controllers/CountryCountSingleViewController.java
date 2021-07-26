package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import sample.Main;

import java.util.Objects;

public class CountryCountSingleViewController {
    private Main main;
    private String cName;
    private int pCount;

    @FXML
    public HBox countryCard;

    @FXML
    private ImageView countryFlag;

    @FXML
    private Label countryName;

    @FXML
    private Label playerCount;

    public void setMain(Main main) {
        this.main = main;
    }
    public HBox initiate(String name, int count){
        this.cName = name;
        this.pCount = count;
        countryName.setText(name);
        playerCount.setText("Player Count: " + pCount);
        Image image = null;
        try{
            var what = main.countryFlagMap.get(name);
            if (what != null) image = new Image(what, true);
            else throw new Exception("");
        } catch (Exception e){
            image = new Image("file:/src/Assets/Image/Flags/" + name + ".png", true);
        }
        countryFlag.setImage(image);
        return countryCard;
    }

}
