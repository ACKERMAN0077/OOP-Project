package com.section2.sceneswitchdemo;

import javafx.event.ActionEvent;

import java.io.IOException;

public class ReportRangeController extends BaseController {
    public void Back(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("dashboard.fxml", actionEvent);
    }
}