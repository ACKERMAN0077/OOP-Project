package com.section2.sceneswitchdemo;

import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController extends BaseController{    @javafx.fxml.FXML
public void promotion(ActionEvent actionEvent) throws IOException {
    SceneSwitcher.switchTo("promotion.fxml", actionEvent);
}
    public void feedback(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("feedback.fxml", actionEvent);
    }
}
