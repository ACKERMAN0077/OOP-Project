package com.section2.sceneswitchdemo;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController
{
    @javafx.fxml.FXML
    private TextField usernameField;
    @javafx.fxml.FXML
    private PasswordField passwordField;
    @javafx.fxml.FXML
    private Label label;

    @javafx.fxml.FXML
    public void initialize() {
    }
    @javafx.fxml.FXML
    public void Back(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("home.fxml", actionEvent);
    }
    @javafx.fxml.FXML
    public void logIn(ActionEvent actionEvent) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("test") && password.equals("1234")) {
            SceneSwitcher.switchTo("dashboard.fxml");
        }
        else {
            label.setText("Username or password do not match");
        }
    }
}