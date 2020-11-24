
package SoftwareII.ViewsAndControllers;

import SoftwareII.DB.CustomerDB;
import SoftwareII.Model.Customer;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import static SoftwareII.ViewsAndControllers.AppointmentController.isChanged;

public class CustomerSelectionController implements Initializable {
    
    @FXML private ListView<Customer> customerListView;
    @FXML private Button exitButton;
    @FXML private Button selectButton;  
    @FXML public static Customer selectedCustomer;

    @FXML
    void exitEvent(ActionEvent event) {
         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Customer Addition");
        alert.setHeaderText("exit?");
        alert.setContentText("OK to proceed. \nCancel to return.");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            try {
                FXMLLoader appointmentCalendarLoader = new FXMLLoader(AppointmentController.class.getResource("Appointment.fxml"));
                Parent appointmentCalendarScreen = appointmentCalendarLoader.load();
                Scene appointmentCalendarScene = new Scene(appointmentCalendarScreen);
                Stage appointmentCalendarStage = new Stage();
                appointmentCalendarStage.setTitle("Appointments");
                appointmentCalendarStage.setScene(appointmentCalendarScene);
                appointmentCalendarStage.show();
                Stage modifyCustomerStage = (Stage) exitButton.getScene().getWindow();
                modifyCustomerStage.close();
            }
            catch (IOException e) {
            }
        }
        else {
            alert.close();
        }
    }

    @FXML
    void selectWasClicked(ActionEvent event) {
        if (customerListView.getSelectionModel().getSelectedItem() == null) {
            Alert nullAlert = new Alert(AlertType.ERROR);
            nullAlert.setTitle("Error");
            nullAlert.setHeaderText("Customer Can't Be Modified");
            nullAlert.setContentText("Select a customer");
            nullAlert.showAndWait();
        }
        else if (isChanged) {
            Alert modificationAlert = new Alert(AlertType.CONFIRMATION);
            modificationAlert.setTitle("Customer Mod");
            modificationAlert.setHeaderText("Modify the customer?");
            modificationAlert.setContentText("OK to proceed. \nCancel to return.");
            modificationAlert.showAndWait();
            if (modificationAlert.getResult() == ButtonType.OK) {
                try {
                    selectedCustomer = customerListView.getSelectionModel().getSelectedItem();
                    FXMLLoader modifiedCustomerLoader = new FXMLLoader(EditCustomerController.class.getResource("EditCustomer.fxml"));
                    Parent modifyCustomerScreen = modifiedCustomerLoader.load();
                    Scene modifyCustomerScene = new Scene(modifyCustomerScreen);
                    Stage modifyCustomerStage = new Stage();
                    modifyCustomerStage.setTitle("Customer Mod");
                    modifyCustomerStage.setScene(modifyCustomerScene);
                    modifyCustomerStage.setResizable(false);
                    modifyCustomerStage.show();
                    Stage customerSelectionStage = (Stage) selectButton.getScene().getWindow();
                    customerSelectionStage.close();
                }
                catch (IOException e) {
                }
            }
        }
        else {
            Alert deleteAlert = new Alert(AlertType.CONFIRMATION);
            deleteAlert.setTitle("Delete The Customer");
            deleteAlert.setHeaderText("Would you like to delete a customer?");
            deleteAlert.setContentText("Push OK to proceed. \nPush Cancel to return.");
            deleteAlert.showAndWait();
            if (deleteAlert.getResult() == ButtonType.OK) {
                selectedCustomer = customerListView.getSelectionModel().getSelectedItem();
                try {
                    CustomerDB.deleteCustomerFromDatabase(selectedCustomer);
                    startCustomerList();
                }
                catch (Exception e) {
                }
            }
        }
    }
    
    public void convertCustomerString() {
        customerListView.setCellFactory(c -> new ListCell<Customer>() {
            @Override
            protected void updateItem(Customer item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item.getCustomerName() == null) {
                    setText("");
                }
                else {
                    setText(item.getCustomerName());
                }
            }
        });
    }
    
    public void startCustomerList() {
        customerListView.setItems(CustomerDB.getActiveCustomersInDatabase());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startCustomerList();
        convertCustomerString();
    }    
    
}
