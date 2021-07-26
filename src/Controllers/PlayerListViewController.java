package Controllers;

import DataModel.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import sample.Main;

import java.io.IOException;
import java.util.List;

public class PlayerListViewController {
    private Main main;

    public enum PageType{
        SimpleList, TransferList
    }

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private ListView<VBox> listView;

    public void initiate(List<Player> players, PageType pageType) throws IOException {
        for (var p : players) {
            var newLoader = new FXMLLoader();
            newLoader.setLocation(getClass().getResource("/ViewFX/MinimalPlayerDetailView.fxml"));
            newLoader.load();
            MinimalPlayerDetailController pDetail = newLoader.getController();
            pDetail.setMain(main);
            listView.getItems().add(pDetail.initiate(p, pageType));
        }
    }

}
