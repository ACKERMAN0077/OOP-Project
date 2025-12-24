module mainpkg.ecoresort {
    requires javafx.controls;
    requires javafx.fxml;

    exports mainpkg.ecoresort.AdminFinance;
    opens mainpkg.ecoresort.AdminFinance to javafx.fxml;
}
