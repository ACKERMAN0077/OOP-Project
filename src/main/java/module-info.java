module com.group15.oop_project2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.group15.oop_project2 to javafx.fxml;
    exports com.group15.oop_project2;
}