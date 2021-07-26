module myjfx {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires com.jfoenix;
    opens sample to javafx.graphics, javafx.fxml, javafx.base;
    opens Controllers to javafx.base, javafx.fxml, javafx.graphics;
    opens DataModel to javafx.base;
}