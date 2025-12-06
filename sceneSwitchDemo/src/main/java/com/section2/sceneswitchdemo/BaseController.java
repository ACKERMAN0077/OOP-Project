package com.section2.sceneswitchdemo;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class BaseController {
    @javafx.fxml.FXML
    public void logOut(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("login.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goToDashboard(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("dashboard.fxml", actionEvent);
    }
    @javafx.fxml.FXML
    public void goToHome(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("home.fxml", actionEvent);
    }
    public static void switchTo(String fxml, ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource(fxml)
        );
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
    @javafx.fxml.FXML
    public void goToManageRooms(ActionEvent actionEvent) throws IOException {
        switchTo("manage-rooms.fxml", actionEvent);
    }
    @javafx.fxml.FXML
    public void goToSchedules(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("staff-schedules.fxml", actionEvent);
    }
    @javafx.fxml.FXML
    public void goToReport(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("report-range.fxml", actionEvent);
    }
    @javafx.fxml.FXML
    public void goToFilterBookings(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("filter-bookings.fxml", actionEvent);
    }
}
