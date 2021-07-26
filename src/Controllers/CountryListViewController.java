package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sample.Main;

import java.io.IOException;
import java.util.HashMap;

public class CountryListViewController {
    private Main main;
    private Stage stage;

    @FXML
    private ListView<HBox> countryListView;

    public void setMain(Main main) {
        this.main = main;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initiate(HashMap<String, Integer> countryWiseCount) throws IOException {
        for (var they : countryWiseCount.entrySet()){
            var newLoader = new FXMLLoader();
            newLoader.setLocation(getClass().getResource("/ViewFX/CountryCountSingleView.fxml"));
            newLoader.load();
            CountryCountSingleViewController cDetail = newLoader.getController();
            cDetail.setMain(main);
            countryListView.getItems().add(cDetail.initiate(they.getKey(), they.getValue()));
        }
    }

}
