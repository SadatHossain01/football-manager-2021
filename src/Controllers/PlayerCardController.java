package Controllers;

import DataModel.Club;
import DataModel.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.Main;

public class PlayerCardController {
    private Main main;
    private Stage stage;
    private Player p;

    @FXML
    private ImageView playerImage;

    @FXML
    private ImageView flag;

    @FXML
    private ImageView logo;

    @FXML
    private Label countryName;

    @FXML
    private Label clubName;

    @FXML
    private Label pName;

    @FXML
    private Label pValue;

    @FXML
    private Label pClub;

    @FXML
    private Label pCountry;

    @FXML
    private Label pHeight;

    @FXML
    private Label pWeight;

    @FXML
    private Label pPosition;

    @FXML
    private Label pNumber;

    @FXML
    private Label pFoot;

    @FXML
    private Label pSalary;

    @FXML
    private Label pStatus;

    @FXML
    private Label pFee;

    public void setMain(Main main) {
        this.main = main;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initiate(Player player) {
        this.p = player;
        var cName = player.getCountry();
        var club_name = player.getClubName();

        try {
            Image pImage = new Image(player.getImageSource(), true);
            playerImage.setImage(pImage);
        } catch (Exception e) {
            var loaded = Main.class.getResourceAsStream("/Assets/Image/Player Image/Anonymous.jpg");
            assert loaded != null;
            playerImage.setImage(new Image(loaded));
        }

        Image flagImage = null;
        var what = main.countryFlagMap.get(player.getCountry());
        if (what != null) flagImage = new Image(what, true);
        else flagImage = new Image("https://www.pngfind.com/pngs/m/295-2959995_anonymous-png-transparent-png.png", true);
        flag.setImage(flagImage);


        if (player.getClubName().equalsIgnoreCase(main.myClub.getName())) logo.setImage(main.cLogo);
        else {
            try {
                logo.setImage(new Image(main.clubLogoMap.get(player.getClubName()), true));
            } catch (Exception e) {
                logo.setImage(new Image("https://www.shopinimizaj.com/frontend/web/images/no-image.png", true));
            }
        }

        countryName.setText(cName);
        clubName.setText(club_name);
        pName.setText(player.getName());
        pValue.setText(Club.showCurrency(player.getEstimatedValue()));
        pClub.setText(club_name);
        pCountry.setText(player.getCountry());
        pHeight.setText(player.getHeight() + " cm");
        pWeight.setText(player.getWeight() + " kg");
        pPosition.setText(player.getPosition());
        pNumber.setText(String.valueOf(player.getNumber()));
        pFoot.setText(String.valueOf(player.getPreferredFoot()));
        pSalary.setText(Club.showCurrency(player.getWeeklySalary()));
        if (player.isTransferListed()) {
            pStatus.setText("Up for sale");
            pFee.setText(Club.showCurrency(player.getTransferFee()));
        } else {
            pStatus.setText("Not for sale");
            pFee.setText("N/A");
        }
    }
}
