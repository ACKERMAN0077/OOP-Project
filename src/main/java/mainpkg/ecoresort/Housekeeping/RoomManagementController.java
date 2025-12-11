package mainpkg.ecoresort.Housekeeping;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class RoomManagementController
{
    @javafx.fxml.FXML
    private ComboBox<Integer> roomCB;
    @javafx.fxml.FXML
    private Label statusL;
    @javafx.fxml.FXML
    private TextField linenTF;
    @javafx.fxml.FXML
    private TableView<Room> roomTV;
    @javafx.fxml.FXML
    private TableColumn<Room, Integer> roomNumberTC;
    @javafx.fxml.FXML
    private TableColumn<Room, String> roomStatusTC;
    @javafx.fxml.FXML
    private TableColumn<Room, String> assignedToTC;
    @javafx.fxml.FXML
    private TableColumn<Room, String> lastCleanedTC;
    @javafx.fxml.FXML
    private TableColumn<Room, String> linenChangeTC;

    HousekeepingStaff loggedInStaff ;
    ObservableList<Room> roomList ;

    public void setter(HousekeepingStaff staff) {
        this.loggedInStaff = staff ;
        loadRooms();
    }

    @javafx.fxml.FXML
    public void initialize() {
        roomNumberTC.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        roomStatusTC.setCellValueFactory(new PropertyValueFactory<>("status"));
        assignedToTC.setCellValueFactory(new PropertyValueFactory<>("assignedTo"));
        lastCleanedTC.setCellValueFactory(new PropertyValueFactory<>("lastCleanedDate"));
        linenChangeTC.setCellValueFactory(new PropertyValueFactory<>("nextLinenChange"));

        roomCB.setOnAction(e -> {
            Integer selectedRoom = roomCB.getValue();
            if (selectedRoom != null) {
                for (Room room : roomList) {
                    if (room.getRoomNumber() == selectedRoom) {
                        statusL.setText(room.getStatus());
                        break;
                    }
                }
            }
        });
    }

    private void loadRooms() {
        roomList = FXCollections.observableArrayList();
        ArrayList<Integer> roomNumbers = new ArrayList<>();

        File f = new File("RoomData.bin");
        if (f.exists()) {
            try {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                while (true) {
                    try {
                        Room room = (Room) ois.readObject();
                        roomList.add(room);
                        roomNumbers.add(room.getRoomNumber());
                    } catch (EOFException e) {
                        break;
                    }
                }
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        roomTV.setItems(roomList);
        roomCB.setItems(FXCollections.observableArrayList(roomNumbers));
    }

    @javafx.fxml.FXML
    public void markCleanOA(ActionEvent event) {
        Integer selectedRoom = roomCB.getValue();
        if (selectedRoom == null) {
            showAlert("Error", "Please select a room");
            return;
        }

        for (Room room : roomList) {
            if (room.getRoomNumber() == selectedRoom) {
                room.setStatus("Clean");
                room.setLastCleanedDate(LocalDate.now().toString());
                break;
            }
        }
        saveRooms();
        loadRooms();
        showAlert("Success", "Room marked as Clean");
    }

    @javafx.fxml.FXML
    public void markDNDOA(ActionEvent event) {
        Integer selectedRoom = roomCB.getValue();
        if (selectedRoom == null) {
            showAlert("Error", "Please select a room");
            return;
        }

        for (Room room : roomList) {
            if (room.getRoomNumber() == selectedRoom) {
                room.setStatus("DND");
                break;
            }
        }
        saveRooms();
        loadRooms();
        showAlert("Success", "Room marked as DND");
    }

    @javafx.fxml.FXML
    public void updateLinenOA(ActionEvent event) {
        Integer selectedRoom = roomCB.getValue();
        String days = linenTF.getText();
        if (selectedRoom == null || days.isEmpty()) {
            showAlert("Error", "Please select a room and enter days");
            return;
        }

        try {
            int daysInt = Integer.parseInt(days);
            for (Room room : roomList) {
                if (room.getRoomNumber() == selectedRoom) {
                    LocalDate nextChange = LocalDate.now().plusDays(daysInt);
                    room.setNextLinenChange(nextChange.toString());
                    break;
                }
            }
            saveRooms();
            loadRooms();
            showAlert("Success", "Linen change updated");
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number");
        }
    }

    @javafx.fxml.FXML
    public void postMaintenanceOA(ActionEvent event) {
        Integer selectedRoom = roomCB.getValue();
        if (selectedRoom == null) {
            showAlert("Error", "Please select a room");
            return;
        }

        for (Room room : roomList) {
            if (room.getRoomNumber() == selectedRoom) {
                if (room.getStatus().equals("Maintenance")) {
                    room.setStatus("Clean");
                    room.setLastCleanedDate(LocalDate.now().toString());
                    saveRooms();
                    loadRooms();
                    showAlert("Success", "Room status updated post-maintenance");
                } else {
                    showAlert("Info", "Room is not in Maintenance status");
                }
                break;
            }
        }
    }

    private void saveRooms() {
        try {
            FileOutputStream fos = new FileOutputStream("RoomData.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (Room room : roomList) {
                oos.writeObject(room);
            }
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void backOA(ActionEvent event) throws IOException {
        Parent root = null ;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HousekeepingDashboard.fxml"));
        root = loader.load();
        HousekeepingDashboardController controller = loader.getController() ;
        controller.setter(loggedInStaff);
        Scene scene = new Scene(root) ;
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
