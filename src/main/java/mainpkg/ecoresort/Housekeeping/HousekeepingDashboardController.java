package mainpkg.ecoresort.Housekeeping;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.*;

public class HousekeepingDashboardController
{
    @javafx.fxml.FXML
    private TextArea dashboardTA;

    HousekeepingStaff loggedInStaff ;

    public void setter(HousekeepingStaff staff) {
        this.loggedInStaff = staff ;
        loadDailySchedule();
    }

    @javafx.fxml.FXML
    public void initialize() {
    }

    private void loadDailySchedule() {
        StringBuilder sb = new StringBuilder();
        sb.append("Welcome, ").append(loggedInStaff.getName()).append("!\n");
        sb.append("Staff ID: ").append(loggedInStaff.getId()).append("\n\n");
        sb.append("=== Daily Room Cleaning Schedule ===\n\n");

        File f = new File("RoomData.bin");
        if (f.exists()) {
            try {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                while (true) {
                    try {
                        Room room = (Room) ois.readObject();
                        if (room.getAssignedTo() != null && room.getAssignedTo().equals(loggedInStaff.getId())) {
                            sb.append("Room ").append(room.getRoomNumber());
                            sb.append(" - Status: ").append(room.getStatus());
                            sb.append(" - Linen Change: ").append(room.getNextLinenChange());
                            sb.append("\n");
                        }
                    } catch (EOFException e) {
                        break;
                    }
                }
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            sb.append("No rooms assigned yet.\n");
        }

        dashboardTA.setText(sb.toString());
    }

    @javafx.fxml.FXML
    public void roomManagementOA(ActionEvent event) throws IOException {
        Parent root = null ;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RoomManagement.fxml"));
        root = loader.load();
        RoomManagementController controller = loader.getController() ;
        controller.setter(loggedInStaff);
        Scene scene = new Scene(root) ;
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @javafx.fxml.FXML
    public void inventoryMaintenanceOA(ActionEvent event) throws IOException {
        Parent root = null ;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InventoryMaintenance.fxml"));
        root = loader.load();
        InventoryMaintenanceController controller = loader.getController() ;
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
