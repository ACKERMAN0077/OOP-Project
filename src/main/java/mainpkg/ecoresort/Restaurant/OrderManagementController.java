package mainpkg.ecoresort.Restaurant;

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
import mainpkg.ecoresort.Housekeeping.Room;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class OrderManagementController
{
    @javafx.fxml.FXML
    private TextField tableTF;
    @javafx.fxml.FXML
    private ComboBox<String> menuCB;
    @javafx.fxml.FXML
    private Label orderIdL;
    @javafx.fxml.FXML
    private Label totalL;
    @javafx.fxml.FXML
    private TextField discountTF;
    @javafx.fxml.FXML
    private TextField roomTF;
    @javafx.fxml.FXML
    private TableView<Order> orderTV;
    @javafx.fxml.FXML
    private TableColumn<Order, Integer> orderIdTC;
    @javafx.fxml.FXML
    private TableColumn<Order, Integer> tableTC;
    @javafx.fxml.FXML
    private TableColumn<Order, String> statusTC;
    @javafx.fxml.FXML
    private TableColumn<Order, Double> subtotalTC;
    @javafx.fxml.FXML
    private TableColumn<Order, Double> grandTotalTC;
    @javafx.fxml.FXML
    private TableColumn<Order, String> paymentTC;
    @javafx.fxml.FXML
    private TableView<MenuItem> itemsTV;
    @javafx.fxml.FXML
    private TableColumn<MenuItem, String> itemNameTC;
    @javafx.fxml.FXML
    private TableColumn<MenuItem, Double> priceTC;
    @javafx.fxml.FXML
    private TableColumn<MenuItem, String> categoryTC;

    RestaurantStaff loggedInStaff ;
    ObservableList<Order> orderList ;
    ObservableList<MenuItem> currentOrderItems ;
    ArrayList<MenuItem> menuItems ;
    Order currentOrder ;

    public void setter(RestaurantStaff staff) {
        this.loggedInStaff = staff ;
        loadData();
    }

    @javafx.fxml.FXML
    public void initialize() {
        orderIdTC.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        tableTC.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));
        statusTC.setCellValueFactory(new PropertyValueFactory<>("status"));
        subtotalTC.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        grandTotalTC.setCellValueFactory(new PropertyValueFactory<>("grandTotal"));
        paymentTC.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));

        itemNameTC.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceTC.setCellValueFactory(new PropertyValueFactory<>("price"));
        categoryTC.setCellValueFactory(new PropertyValueFactory<>("category"));

        currentOrderItems = FXCollections.observableArrayList();
        itemsTV.setItems(currentOrderItems);

        orderTV.setOnMouseClicked(e -> {
            Order selected = orderTV.getSelectionModel().getSelectedItem();
            if (selected != null) {
                currentOrder = selected;
                orderIdL.setText(String.valueOf(selected.getOrderId()));
                totalL.setText(String.format("%.2f", selected.getGrandTotal()));
                currentOrderItems.clear();
                currentOrderItems.addAll(selected.getItems());
            }
        });
    }

    private void loadData() {
        loadOrders();
        loadMenuItems();
    }

    private void loadOrders() {
        orderList = FXCollections.observableArrayList();

        File f = new File("OrderData.bin");
        if (f.exists()) {
            try {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                while (true) {
                    try {
                        Order order = (Order) ois.readObject();
                        orderList.add(order);
                    } catch (EOFException e) {
                        break;
                    }
                }
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        orderTV.setItems(orderList);
    }

    private void loadMenuItems() {
        menuItems = new ArrayList<>();
        ArrayList<String> menuNames = new ArrayList<>();

        File f = new File("MenuData.bin");
        if (f.exists()) {
            try {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                while (true) {
                    try {
                        MenuItem item = (MenuItem) ois.readObject();
                        menuItems.add(item);
                        menuNames.add(item.getName());
                    } catch (EOFException e) {
                        break;
                    }
                }
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        menuCB.setItems(FXCollections.observableArrayList(menuNames));
    }

    @javafx.fxml.FXML
    public void newOrderOA(ActionEvent event) {
        String tableStr = tableTF.getText();
        if (tableStr.isEmpty()) {
            showAlert("Error", "Please enter table number");
            return;
        }

        try {
            int tableNum = Integer.parseInt(tableStr);
            Random r = new Random();
            int orderId = r.nextInt(9000) + 1000;

            currentOrder = new Order(orderId, tableNum);
            orderIdL.setText(String.valueOf(orderId));
            totalL.setText("0.00");
            currentOrderItems.clear();

            showAlert("Success", "New order created: " + orderId);
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid table number");
        }
    }

    @javafx.fxml.FXML
    public void addItemOA(ActionEvent event) {
        if (currentOrder == null) {
            showAlert("Error", "Please create a new order first");
            return;
        }

        String selectedMenu = menuCB.getValue();
        if (selectedMenu == null) {
            showAlert("Error", "Please select a menu item");
            return;
        }

        for (MenuItem item : menuItems) {
            if (item.getName().equals(selectedMenu)) {
                currentOrder.addItem(item);
                currentOrderItems.add(item);
                totalL.setText(String.format("%.2f", currentOrder.getGrandTotal()));
                break;
            }
        }
    }

    @javafx.fxml.FXML
    public void applyDiscountOA(ActionEvent event) {
        if (currentOrder == null) {
            showAlert("Error", "No order selected");
            return;
        }

        String discountStr = discountTF.getText();
        if (discountStr.isEmpty()) {
            showAlert("Error", "Please enter discount amount");
            return;
        }

        try {
            double discount = Double.parseDouble(discountStr);
            currentOrder.setDiscount(discount);
            totalL.setText(String.format("%.2f", currentOrder.getGrandTotal()));
            showAlert("Success", "Discount applied");
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount");
        }
    }

    @javafx.fxml.FXML
    public void cashPaymentOA(ActionEvent event) {
        processPayment("Cash");
    }

    @javafx.fxml.FXML
    public void cardPaymentOA(ActionEvent event) {
        processPayment("Card");
    }

    @javafx.fxml.FXML
    public void roomChargeOA(ActionEvent event) {
        if (currentOrder == null) {
            showAlert("Error", "No order selected");
            return;
        }

        String roomStr = roomTF.getText();
        if (roomStr.isEmpty()) {
            showAlert("Error", "Please enter room number");
            return;
        }

        try {
            int roomNum = Integer.parseInt(roomStr);
            if (verifyRoomGuest(roomNum)) {
                currentOrder.setRoomNumber(roomNum);
                processPayment("Room Charge");
            } else {
                showAlert("Error", "Room not eligible for room service");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid room number");
        }
    }

    @javafx.fxml.FXML
    public void verifyGuestOA(ActionEvent event) {
        String roomStr = roomTF.getText();
        if (roomStr.isEmpty()) {
            showAlert("Error", "Please enter room number");
            return;
        }

        try {
            int roomNum = Integer.parseInt(roomStr);
            if (verifyRoomGuest(roomNum)) {
                showAlert("Success", "Room " + roomNum + " is eligible for room service");
            } else {
                showAlert("Error", "Room " + roomNum + " is not eligible (DND or Maintenance)");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid room number");
        }
    }

    private boolean verifyRoomGuest(int roomNum) {
        File f = new File("RoomData.bin");
        if (f.exists()) {
            try {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                while (true) {
                    try {
                        Room room = (Room) ois.readObject();
                        if (room.getRoomNumber() == roomNum) {
                            ois.close();
                            String status = room.getStatus();
                            return !status.equals("DND") && !status.equals("Maintenance");
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
        return false;
    }

    private void processPayment(String method) {
        if (currentOrder == null) {
            showAlert("Error", "No order selected");
            return;
        }

        if (currentOrder.getItems().isEmpty()) {
            showAlert("Error", "Order has no items");
            return;
        }

        currentOrder.setPaymentMethod(method);
        currentOrder.setStatus("Paid");

        orderList.removeIf(o -> o.getOrderId() == currentOrder.getOrderId());
        orderList.add(currentOrder);

        saveOrders();
        loadOrders();

        showAlert("Success", "Payment processed: " + method + "\nTotal: " + String.format("%.2f", currentOrder.getGrandTotal()) + " BDT");

        currentOrder = null;
        orderIdL.setText("-");
        totalL.setText("0.00");
        currentOrderItems.clear();
    }

    @javafx.fxml.FXML
    public void voidItemOA(ActionEvent event) {
        if (currentOrder == null) {
            showAlert("Error", "No order selected");
            return;
        }

        MenuItem selected = itemsTV.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select an item to void");
            return;
        }

        currentOrder.removeItem(selected);
        currentOrderItems.remove(selected);
        totalL.setText(String.format("%.2f", currentOrder.getGrandTotal()));
        showAlert("Success", "Item voided");
    }

    private void saveOrders() {
        try {
            FileOutputStream fos = new FileOutputStream("OrderData.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (Order order : orderList) {
                oos.writeObject(order);
            }
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void backOA(ActionEvent event) throws IOException {
        Parent root = null ;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RestaurantDashboard.fxml"));
        root = loader.load();
        RestaurantDashboardController controller = loader.getController() ;
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
