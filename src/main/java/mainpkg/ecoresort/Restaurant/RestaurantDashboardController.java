package mainpkg.ecoresort.Restaurant;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.*;

public class RestaurantDashboardController
{
    @javafx.fxml.FXML
    private TextArea dashboardTA;

    RestaurantStaff loggedInStaff ;

    public void setter(RestaurantStaff staff) {
        this.loggedInStaff = staff ;
        loadDashboardInfo();
    }

    @javafx.fxml.FXML
    public void initialize() {
    }

    private void loadDashboardInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Welcome, ").append(loggedInStaff.getName()).append("!\n");
        sb.append("Staff ID: ").append(loggedInStaff.getId()).append("\n\n");
        sb.append("=== Restaurant Summary ===\n\n");

        int pendingOrders = 0;
        int completedOrders = 0;
        double totalSales = 0;

        File f = new File("OrderData.bin");
        if (f.exists()) {
            try {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                while (true) {
                    try {
                        Order order = (Order) ois.readObject();
                        if (order.getStatus().equals("Pending") || order.getStatus().equals("Preparing")) {
                            pendingOrders++;
                        } else if (order.getStatus().equals("Paid")) {
                            completedOrders++;
                            totalSales += order.getGrandTotal();
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

        sb.append("Pending Orders: ").append(pendingOrders).append("\n");
        sb.append("Completed Orders: ").append(completedOrders).append("\n");
        sb.append("Total Sales: ").append(String.format("%.2f", totalSales)).append(" BDT\n");

        dashboardTA.setText(sb.toString());
    }

    @javafx.fxml.FXML
    public void orderManagementOA(ActionEvent event) throws IOException {
        Parent root = null ;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("OrderManagement.fxml"));
        root = loader.load();
        OrderManagementController controller = loader.getController() ;
        controller.setter(loggedInStaff);
        Scene scene = new Scene(root) ;
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @javafx.fxml.FXML
    public void menuStockOA(ActionEvent event) throws IOException {
        Parent root = null ;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuStock.fxml"));
        root = loader.load();
        MenuStockController controller = loader.getController() ;
        controller.setter(loggedInStaff);
        Scene scene = new Scene(root) ;
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @javafx.fxml.FXML
    public void signoutOA(ActionEvent event) throws IOException {
        Parent root = null ;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mainpkg/ecoresort/LoginPage.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root) ;
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login Page");
        stage.show();
    }
}
