package mainpkg.ecoresort;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mainpkg.ecoresort.Housekeeping.HousekeepingStaff;
import mainpkg.ecoresort.Housekeeping.HousekeepingDashboardController;
import mainpkg.ecoresort.Restaurant.RestaurantStaff;
import mainpkg.ecoresort.Restaurant.RestaurantDashboardController;

import java.io.*;

public class LoginPageController
{
    @javafx.fxml.FXML
    private TextField userIDTF;
    @javafx.fxml.FXML
    private PasswordField passwordPF;

    ObservableList<HousekeepingStaff> housekeepingStaffList = FXCollections.observableArrayList() ;
    ObservableList<RestaurantStaff> restaurantStaffList = FXCollections.observableArrayList() ;

    @javafx.fxml.FXML
    public void initialize() {
        HousekeepingStaff staff1 = new HousekeepingStaff("1001", "Noor", "1234") ;
        housekeepingStaffList.add(staff1) ;

        HousekeepingStaff staff2 = new HousekeepingStaff("1002", "Fahim", "1234") ;
        housekeepingStaffList.add(staff2) ;

        RestaurantStaff rstaff1 = new RestaurantStaff("10001", "Rahim", "1234") ;
        restaurantStaffList.add(rstaff1) ;

        RestaurantStaff rstaff2 = new RestaurantStaff("10002", "Karim", "1234") ;
        restaurantStaffList.add(rstaff2) ;
    }

    @javafx.fxml.FXML
    public void loginOA(ActionEvent actionEvent) throws IOException {
        String id, password ;
        boolean flag = true ;

        Alert erroralert = new Alert(Alert.AlertType.ERROR) ;

        id = userIDTF.getText() ;
        password = passwordPF.getText() ;

        if (id.isBlank()) {
            flag = false ;
            erroralert.setTitle("User ID Error");
            erroralert.setContentText("User ID can not be blank.");
            erroralert.showAndWait() ;
        }

        if (password.isBlank()) {
            flag = false ;
            erroralert.setTitle("Password Error");
            erroralert.setContentText("Password can not be blank.");
            erroralert.showAndWait() ;
        }

        if (flag) {
            if (id.length() == 4) {
                for (HousekeepingStaff staff : housekeepingStaffList) {
                    if (staff.login(id, password)) {
                        Parent root = null ;
                        FXMLLoader fxmlLoader = new FXMLLoader(EcoResortApp.class.getResource("Housekeeping/HousekeepingDashboard.fxml"));
                        root = fxmlLoader.load();
                        HousekeepingDashboardController controller = fxmlLoader.getController() ;
                        controller.setter(staff);
                        Scene scene = new Scene(root) ;
                        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.setTitle("Housekeeping Dashboard");
                        stage.show();
                    }
                }
            } else if (id.length() == 5) {
                for (RestaurantStaff staff : restaurantStaffList) {
                    if (staff.login(id, password)) {
                        Parent root = null ;
                        FXMLLoader fxmlLoader = new FXMLLoader(EcoResortApp.class.getResource("Restaurant/RestaurantDashboard.fxml"));
                        root = fxmlLoader.load();
                        RestaurantDashboardController controller = fxmlLoader.getController() ;
                        controller.setter(staff);
                        Scene scene = new Scene(root) ;
                        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.setTitle("Restaurant Dashboard");
                        stage.show();
                    }
                }
            } else {
                erroralert.setTitle("User ID Error");
                erroralert.setContentText("User ID type does not exist.");
                erroralert.showAndWait() ;
            }
        }
        else {
            System.out.println("Flag not work");
        }
    }
}
