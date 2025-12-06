module com.section2.sceneswitchdemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    opens com.section2.sceneswitchdemo to javafx.fxml;
    exports com.section2.sceneswitchdemo;

    exports com.section2.sceneswitchdemo.asif;
    opens com.section2.sceneswitchdemo.asif to javafx.fxml;

    exports com.section2.sceneswitchdemo.mahmood;
    opens com.section2.sceneswitchdemo.mahmood to javafx.fxml;
}