module mainpkg.ecoresort {
    requires javafx.controls;
    requires javafx.fxml;


    opens mainpkg.ecoresort to javafx.fxml;
    opens mainpkg.ecoresort.Housekeeping to javafx.fxml;
    opens mainpkg.ecoresort.Restaurant to javafx.fxml;
    exports mainpkg.ecoresort;
    exports mainpkg.ecoresort.Housekeeping;
    exports mainpkg.ecoresort.Restaurant;
}
