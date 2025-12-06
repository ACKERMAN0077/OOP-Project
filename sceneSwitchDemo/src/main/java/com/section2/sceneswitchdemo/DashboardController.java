package com.section2.sceneswitchdemo;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController extends BaseController
{
    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void goToGoal2(ActionEvent actionEvent) throws IOException {
        switchTo("/com/section2/sceneswitchdemo/mahmood/goal2.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void goToGoal1(ActionEvent actionEvent) throws IOException {
        switchTo("/com/section2/sceneswitchdemo/filter-bookings.fxml", actionEvent);
    }

}
