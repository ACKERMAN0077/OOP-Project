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
import mainpkg.ecoresort.Housekeeping.StockItem;

import java.io.*;

public class MenuStockController
{
    @javafx.fxml.FXML
    private TableView<MenuItem> menuTV;
    @javafx.fxml.FXML
    private TableColumn<MenuItem, String> menuNameTC;
    @javafx.fxml.FXML
    private TableColumn<MenuItem, Double> menuPriceTC;
    @javafx.fxml.FXML
    private TableColumn<MenuItem, String> menuCategoryTC;
    @javafx.fxml.FXML
    private TableView<StockItem> stockTV;
    @javafx.fxml.FXML
    private TableColumn<StockItem, String> stockNameTC;
    @javafx.fxml.FXML
    private TableColumn<StockItem, Integer> stockQtyTC;
    @javafx.fxml.FXML
    private TableColumn<StockItem, Integer> stockReorderTC;
    @javafx.fxml.FXML
    private TableColumn<StockItem, String> stockCategoryTC;
    @javafx.fxml.FXML
    private TextArea reportTA;

    RestaurantStaff loggedInStaff ;
    ObservableList<MenuItem> menuList ;
    ObservableList<StockItem> stockList ;

    public void setter(RestaurantStaff staff) {
        this.loggedInStaff = staff ;
        loadData();
    }

    @javafx.fxml.FXML
    public void initialize() {
        menuNameTC.setCellValueFactory(new PropertyValueFactory<>("name"));
        menuPriceTC.setCellValueFactory(new PropertyValueFactory<>("price"));
        menuCategoryTC.setCellValueFactory(new PropertyValueFactory<>("category"));

        stockNameTC.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        stockQtyTC.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        stockReorderTC.setCellValueFactory(new PropertyValueFactory<>("reorderLevel"));
        stockCategoryTC.setCellValueFactory(new PropertyValueFactory<>("category"));
    }

    private void loadData() {
        loadMenu();
        loadStock();
    }

    private void loadMenu() {
        menuList = FXCollections.observableArrayList();

        File f = new File("MenuData.bin");
        if (f.exists()) {
            try {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                while (true) {
                    try {
                        MenuItem item = (MenuItem) ois.readObject();
                        menuList.add(item);
                    } catch (EOFException e) {
                        break;
                    }
                }
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        menuTV.setItems(menuList);
    }

    private void loadStock() {
        stockList = FXCollections.observableArrayList();

        File f = new File("StockData.bin");
        if (f.exists()) {
            try {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                while (true) {
                    try {
                        StockItem item = (StockItem) ois.readObject();
                        if (item.getCategory().equals("Food")) {
                            stockList.add(item);
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
    }

    @javafx.fxml.FXML
    public void generateReportOA(ActionEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== End-of-Shift Sales Report ===\n");

        int totalOrders = 0;
        double totalSales = 0;
        int cashPayments = 0;
        int cardPayments = 0;
        int roomCharges = 0;

        File f = new File("OrderData.bin");
        if (f.exists()) {
            try {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                while (true) {
                    try {
                        Order order = (Order) ois.readObject();
                        if (order.getStatus().equals("Paid")) {
                            totalOrders++;
                            totalSales += order.getGrandTotal();

                            if (order.getPaymentMethod().equals("Cash")) {
                                cashPayments++;
                            } else if (order.getPaymentMethod().equals("Card")) {
                                cardPayments++;
                            } else if (order.getPaymentMethod().equals("Room Charge")) {
                                roomCharges++;
                            }
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

        sb.append("Total Orders: ").append(totalOrders).append("\n");
        sb.append("Total Sales: ").append(String.format("%.2f", totalSales)).append(" BDT\n");
        sb.append("Cash: ").append(cashPayments).append(" | Card: ").append(cardPayments).append(" | Room: ").append(roomCharges);

        reportTA.setText(sb.toString());
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
}
