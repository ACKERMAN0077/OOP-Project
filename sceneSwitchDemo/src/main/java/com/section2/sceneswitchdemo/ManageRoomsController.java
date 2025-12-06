package com.section2.sceneswitchdemo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ManageRoomsController extends BaseController {

    @FXML private TableView<List<String>> roomsTable;
    @FXML private TableColumn<List<String>, String> colRoom;
    @FXML private TableColumn<List<String>, String> colType;
    @FXML private TableColumn<List<String>, String> colStatus;
    @FXML private TableColumn<List<String>, Void> colAction;

    @FXML
    public void initialize() {

        // Hardcoded data, each row = [Room, Type, Status]
        ObservableList<List<String>> data = FXCollections.observableArrayList(
                Arrays.asList("101", "Single", "Available"),
                Arrays.asList("102", "Double", "Occupied"),
                Arrays.asList("103", "Suite",  "Available")
        );

        // Column value factories
        colRoom.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().get(0)));
        colType.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().get(1)));
        colStatus.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().get(2)));

        // Action column with Book/Reset button
        colAction.setCellFactory(new Callback<>() {
            @Override
            public TableCell<List<String>, Void> call(TableColumn<List<String>, Void> param) {
                return new TableCell<>() {

                    private final Button btn = new Button();

                    {
                        btn.setOnAction(event -> {
                            List<String> row = getTableView().getItems().get(getIndex());

                            String currentStatus = row.get(2);

                            if (currentStatus.equals("Available")) {
                                row.set(2, "Occupied");
                                btn.setText("Reset");
                            } else {
                                row.set(2, "Available");
                                btn.setText("Book");
                            }

                            roomsTable.refresh();
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                            return;
                        }

                        List<String> row = getTableView().getItems().get(getIndex());
                        String status = row.get(2);

                        btn.setText(status.equals("Available") ? "Book" : "Reset");

                        setGraphic(btn);
                    }
                };
            }
        });

        roomsTable.setItems(data);
    }
    public void Back(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("dashboard.fxml", actionEvent);
    }
}