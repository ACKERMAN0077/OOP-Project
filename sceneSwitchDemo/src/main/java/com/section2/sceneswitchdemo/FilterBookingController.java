package com.section2.sceneswitchdemo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;

public class FilterBookingController extends BaseController {

    @FXML private TableView<String[]> bookingTable;

    @FXML private TableColumn<String[], String> roomCol;
    @FXML private TableColumn<String[], String> guestCol;
    @FXML private TableColumn<String[], String> statusCol;
    @FXML private TableColumn<String[], String> arrivalCol;
    @FXML private TableColumn<String[], String> departCol;

    @FXML private TextField searchField;

    ObservableList<String[]> data;

    @FXML
    public void initialize() {

        // Hard-coded rows (no extra class needed)
        data = FXCollections.observableArrayList(
                new String[]{"101", "John Smith",  "Checked-In", "2025-12-10", "2025-12-15"},
                new String[]{"203", "Emily Brown", "Reserved",    "2025-12-12", "2025-12-14"},
                new String[]{"305", "Mike Johnson","Checked-Out", "2025-12-08", "2025-12-10"},
                new String[]{"101", "Sarah Miller","Reserved",    "2025-12-20", "2025-12-22"}
        );

        // Map table columns → array indexes
        roomCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()[0]));
        guestCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()[1]));
        statusCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()[2]));
        arrivalCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()[3]));
        departCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()[4]));

        // Filtered wrapper
        FilteredList<String[]> filtered = new FilteredList<>(data, p -> true);

        // Search logic
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String keyword = newVal.toLowerCase();

            filtered.setPredicate(row -> {
                if (keyword.isEmpty()) return true;

                return row[0].toLowerCase().contains(keyword) // room #
                        || row[1].toLowerCase().contains(keyword); // guest name
            });
        });

        bookingTable.setItems(filtered);
    }
}