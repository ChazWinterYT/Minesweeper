module com.chazwinter.minesweeper {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.chazwinter.minesweeper to javafx.fxml;
    exports com.chazwinter.minesweeper;

    exports com.chazwinter.minesweeper.model;
    opens com.chazwinter.minesweeper.model to javafx.fxml;
    exports com.chazwinter.minesweeper.ui;
    opens com.chazwinter.minesweeper.ui to javafx.fxml;
    opens com.chazwinter.minesweeper.settings to javafx.fxml;
    exports com.chazwinter.minesweeper.settings;
    exports com.chazwinter.minesweeper.util;
    opens com.chazwinter.minesweeper.util to javafx.fxml;
}