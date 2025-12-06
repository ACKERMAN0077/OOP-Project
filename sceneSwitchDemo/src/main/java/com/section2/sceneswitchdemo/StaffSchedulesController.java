package com.section2.sceneswitchdemo;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

import java.io.IOException;

public class StaffSchedulesController extends BaseController {

    @FXML private TableView<StaffRow> scheduleTable;
    @FXML private TableColumn<StaffRow, String> colName;
    @FXML private TableColumn<StaffRow, String> colDate;
    @FXML private TableColumn<StaffRow, String> colSchedule;
    @FXML private TableColumn<StaffRow, Void> colEditButton;

    private ObservableList<StaffRow> data;

    @FXML
    public void initialize() {

        // -----------------------------
        // HARD CODED TABLE DATA
        // -----------------------------
        data = FXCollections.observableArrayList(
                new StaffRow("John Doe", "2025-12-06", "Morning Shift"),
                new StaffRow("Sarah Ali", "2025-12-06", "Evening Shift"),
                new StaffRow("David Kim", "2025-12-06", "Night Shift")
        );

        scheduleTable.setEditable(true);

        colName.setCellValueFactory(cell -> cell.getValue().nameProperty());
        colDate.setCellValueFactory(cell -> cell.getValue().dateProperty());
        colSchedule.setCellValueFactory(cell -> cell.getValue().scheduleProperty());

        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colDate.setCellFactory(TextFieldTableCell.forTableColumn());
        colSchedule.setCellFactory(TextFieldTableCell.forTableColumn());

        colName.setOnEditCommit(e -> e.getRowValue().nameProperty().set(e.getNewValue()));
        colDate.setOnEditCommit(e -> e.getRowValue().dateProperty().set(e.getNewValue()));
        colSchedule.setOnEditCommit(e -> e.getRowValue().scheduleProperty().set(e.getNewValue()));

        addEditButtonColumn();

        scheduleTable.setItems(data);
    }

    // -------------------------------------------------------
    // EDIT BUTTON COLUMN (Edit → Save → Edit toggle)
    // -------------------------------------------------------
    private void addEditButtonColumn() {
        Callback<TableColumn<StaffRow, Void>, TableCell<StaffRow, Void>> cellFactory = (param) -> new TableCell<>() {

            final Button btn = new Button("Edit");
            boolean editing = false;

            {
                btn.setOnAction(e -> {
                    StaffRow row = getTableView().getItems().get(getIndex());

                    if (!editing) {
                        // Start editing
                        scheduleTable.edit(getIndex(), colName);
                        editing = true;
                        btn.setText("Save");

                    } else {
                        // Save changes
                        scheduleTable.getSelectionModel().clearSelection();
                        editing = false;
                        btn.setText("Edit");
                        scheduleTable.refresh();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        };

        colEditButton.setCellFactory(cellFactory);
    }

    public void Back(ActionEvent event) throws IOException {
        SceneSwitcher.switchTo("dashboard.fxml", event);
    }

    // -------------------------------------------------------
    // SIMPLE DATA MODEL
    // -------------------------------------------------------
    public static class StaffRow {

        private final SimpleStringProperty name;
        private final SimpleStringProperty date;
        private final SimpleStringProperty schedule;

        public StaffRow(String name, String date, String schedule) {
            this.name = new SimpleStringProperty(name);
            this.date = new SimpleStringProperty(date);
            this.schedule = new SimpleStringProperty(schedule);
        }

        public SimpleStringProperty nameProperty() { return name; }
        public SimpleStringProperty dateProperty() { return date; }
        public SimpleStringProperty scheduleProperty() { return schedule; }
    }
}
