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
import mainpkg.ecoresort.AppendableObjectOutputStream;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class InventoryMaintenanceController
{
    @javafx.fxml.FXML
    private ComboBox<Integer> roomCB;
    @javafx.fxml.FXML
    private TextField issueTF;
    @javafx.fxml.FXML
    private ComboBox<String> amenityCB;
    @javafx.fxml.FXML
    private TextField qtyTF;
    @javafx.fxml.FXML
    private TableView<StockItem> stockTV;
    @javafx.fxml.FXML
    private TableColumn<StockItem, String> itemNameTC;
    @javafx.fxml.FXML
    private TableColumn<StockItem, Integer> quantityTC;
    @javafx.fxml.FXML
    private TableColumn<StockItem, Integer> reorderTC;
    @javafx.fxml.FXML
    private TableColumn<StockItem, String> categoryTC;
    @javafx.fxml.FXML
    private TableView<MaintenanceTicket> ticketTV;
    @javafx.fxml.FXML
    private TableColumn<MaintenanceTicket, String> ticketIdTC;
    @javafx.fxml.FXML
    private TableColumn<MaintenanceTicket, Integer> ticketRoomTC;
    @javafx.fxml.FXML
    private TableColumn<MaintenanceTicket, String> issueTC;
    @javafx.fxml.FXML
    private TableColumn<MaintenanceTicket, String> ticketStatusTC;

    HousekeepingStaff loggedInStaff ;
    ObservableList<StockItem> stockList ;
    ObservableList<MaintenanceTicket> ticketList ;

    public void setter(HousekeepingStaff staff) {
        this.loggedInStaff = staff ;
        loadData();
    }

    @javafx.fxml.FXML
    public void initialize() {
        itemNameTC.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantityTC.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        reorderTC.setCellValueFactory(new PropertyValueFactory<>("reorderLevel"));
        categoryTC.setCellValueFactory(new PropertyValueFactory<>("category"));

        ticketIdTC.setCellValueFactory(new PropertyValueFactory<>("ticketId"));
        ticketRoomTC.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        issueTC.setCellValueFactory(new PropertyValueFactory<>("issueDescription"));
        ticketStatusTC.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadData() {
        loadStock();
        loadTickets();
        loadRoomNumbers();
    }

    private void loadStock() {
        stockList = FXCollections.observableArrayList();
        ArrayList<String> amenities = new ArrayList<>();

        File f = new File("StockData.bin");
        if (f.exists()) {
            try {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                while (true) {
                    try {
                        StockItem item = (StockItem) ois.readObject();
                        stockList.add(item);
                        if (item.getCategory().equals("Amenity")) {
                            amenities.add(item.getItemName());
                        }
                    } catch (EOFException e) {
                        break;
                    }
                }
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        stockTV.setItems(stockList);
        amenityCB.setItems(FXCollections.observableArrayList(amenities));
    }

    private void loadTickets() {
        ticketList = FXCollections.observableArrayList();

        File f = new File("MaintenanceTicket.bin");
        if (f.exists()) {
            try {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                while (true) {
                    try {
                        MaintenanceTicket ticket = (MaintenanceTicket) ois.readObject();
                        ticketList.add(ticket);
                    } catch (EOFException e) {
                        break;
                    }
                }
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ticketTV.setItems(ticketList);
    }

    private void loadRoomNumbers() {
        ArrayList<Integer> roomNumbers = new ArrayList<>();

        File f = new File("RoomData.bin");
        if (f.exists()) {
            try {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                while (true) {
                    try {
                        Room room = (Room) ois.readObject();
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

        roomCB.setItems(FXCollections.observableArrayList(roomNumbers));
    }

    @javafx.fxml.FXML
    public void reportMaintenanceOA(ActionEvent event) {
        Integer roomNum = roomCB.getValue();
        String issue = issueTF.getText();

        if (roomNum == null || issue.isEmpty()) {
            showAlert("Error", "Please select room and enter issue");
            return;
        }

        Random r = new Random();
        String ticketId = "TK" + (r.nextInt(9000) + 1000);
        MaintenanceTicket ticket = new MaintenanceTicket(ticketId, roomNum, issue, "Open", loggedInStaff.getId(), LocalDate.now().toString());

        try {
            File f = new File("MaintenanceTicket.bin");
            FileOutputStream fos ;
            ObjectOutputStream oos ;

            if (f.exists()) {
                fos = new FileOutputStream(f, true);
                oos = new AppendableObjectOutputStream(fos);
            } else {
                fos = new FileOutputStream(f);
                oos = new ObjectOutputStream(fos);
            }
            oos.writeObject(ticket);
            oos.close();

            updateRoomToMaintenance(roomNum);
            loadData();
            issueTF.clear();
            showAlert("Success", "Maintenance ticket created: " + ticketId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateRoomToMaintenance(int roomNum) {
        ArrayList<Room> rooms = new ArrayList<>();
        File f = new File("RoomData.bin");
        if (f.exists()) {
            try {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                while (true) {
                    try {
                        Room room = (Room) ois.readObject();
                        if (room.getRoomNumber() == roomNum) {
                            room.setStatus("Maintenance");
                        }
                        rooms.add(room);
                    } catch (EOFException e) {
                        break;
                    }
                }
                ois.close();

                FileOutputStream fos = new FileOutputStream(f);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                for (Room room : rooms) {
                    oos.writeObject(room);
                }
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @javafx.fxml.FXML
    public void processAmenityOA(ActionEvent event) {
        String amenity = amenityCB.getValue();
        String qtyStr = qtyTF.getText();

        if (amenity == null || qtyStr.isEmpty()) {
            showAlert("Error", "Please select amenity and enter quantity");
            return;
        }

        try {
            int qty = Integer.parseInt(qtyStr);
            for (StockItem item : stockList) {
                if (item.getItemName().equals(amenity)) {
                    if (item.getQuantity() >= qty) {
                        item.setQuantity(item.getQuantity() - qty);
                        saveStock();
                        loadStock();
                        qtyTF.clear();
                        showAlert("Success", "Amenity request processed");
                    } else {
                        showAlert("Error", "Insufficient stock");
                    }
                    break;
                }
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid quantity");
        }
    }

    private void saveStock() {
        try {
            FileOutputStream fos = new FileOutputStream("StockData.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (StockItem item : stockList) {
                oos.writeObject(item);
            }
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void viewLowStockOA(ActionEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append("Low Stock Alert:\n\n");

        boolean hasLowStock = false;
        for (StockItem item : stockList) {
            if (item.getQuantity() <= item.getReorderLevel()) {
                sb.append(item.getItemName()).append(" - Qty: ").append(item.getQuantity());
                sb.append(" (Reorder at: ").append(item.getReorderLevel()).append(")\n");
                hasLowStock = true;
            }
        }

        if (!hasLowStock) {
            sb.append("No items below reorder level.");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Inventory Alert");
        alert.setHeaderText("Low Stock Items");
        alert.setContentText(sb.toString());
        alert.showAndWait();
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
