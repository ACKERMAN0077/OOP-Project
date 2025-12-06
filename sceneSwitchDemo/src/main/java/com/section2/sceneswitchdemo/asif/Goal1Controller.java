package com.section2.sceneswitchdemo.asif;

import com.section2.sceneswitchdemo.BaseController;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Goal1Controller extends BaseController
{
    @javafx.fxml.FXML
    private Label label;
    @javafx.fxml.FXML
    private TextArea textArea;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void save(ActionEvent actionEvent) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("data.txt")
        )) {
            String content = textArea.getText();
            writer.write(content);
            label.setText("Data saved successfully.");
        } catch (IOException e) {
            label.setText("Something went wrong!");
        }
    }
}