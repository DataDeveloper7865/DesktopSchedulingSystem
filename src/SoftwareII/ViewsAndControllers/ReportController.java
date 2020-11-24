
package SoftwareII.ViewsAndControllers;

import SoftwareII.DB.AppointmentDB;
import SoftwareII.DB.CustomerDB;
import SoftwareII.DB.UserDB;
import SoftwareII.Model.Appointment;
import SoftwareII.Model.Customer;
import SoftwareII.Model.User;
import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class ReportController implements Initializable {
    
    @FXML private ToggleGroup reportsToggleGroup; 
    @FXML private RadioButton appointmentTypesToggleButton;
    @FXML private RadioButton scheduleToggleButton;
    @FXML private RadioButton customerToggleButton; 
    @FXML private TextArea reportFieldTextArea;  
    @FXML private Button exitButton;  
    @FXML private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    @FXML private final DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("hh:mm a z");

    @FXML
    void getAppointmentTypes(ActionEvent event) {
        if (appointmentTypesToggleButton.isSelected()) {
            try {
                reportFieldTextArea.clear();
                ObservableList<Appointment> appointmentTypes = AppointmentDB.getAppointmentsByMonth();
                Integer value = 1;
                Map<String, Integer> map = new HashMap<>();
                for (Appointment a : appointmentTypes) {
                    if (map.containsKey(a.getType())) {
                        map.put(a.getType(), map.get(a.getType())+1);
                    }
                    else{
                        map.put(a.getType(), value);
                    }
                }
                for (String s : map.keySet()) {
                    reportFieldTextArea.appendText(map.get(s) + " appointment of " + s + "occur this month.\n");
                }
            }
            catch (Exception e) {
            }
        }
    }

    @FXML
    void retrieveCustomerList(ActionEvent event) {
        if (customerToggleButton.isSelected()) {
            try {
                reportFieldTextArea.clear();
                ObservableList<Customer> allCustomers = CustomerDB.getAllCustomersFromDatabase();
                String customerName = "";
                Integer customerId;
                String newline = "\n";
                for (Customer c : allCustomers) {
                    customerName = c.getCustomerName();
                    customerId = c.getCustomerId();
                    reportFieldTextArea.appendText("Customer " + customerName + "'s customer ID is " + customerId + " in the database." + newline);
                }
                
            }
            catch (Exception e) {
            }
        }
    }

    @FXML
    void retrieveSchedule(ActionEvent event) {
        if (scheduleToggleButton.isSelected()) {
            try {
                reportFieldTextArea.clear();
                ObservableList<Appointment> userSchedule = AppointmentDB.getAppointmentsByUser();
                String userName = "";
                String customerName = "";
                ZonedDateTime startZDT;
                
                for (Appointment a : userSchedule) {
                    int u = a.getUserId();
                    int c = a.getCustomerId();
                    startZDT = a.getStart();
                    User userById = UserDB.getUserById(u);
                    userName = userById.getUserName();
                    Customer activeCustomerById = CustomerDB.getActiveCustomerById(c);
                    customerName = activeCustomerById.getCustomerName();
                    reportFieldTextArea.appendText("Consultant " + userName + " has an appointment with " + customerName + " on " + startZDT.format(formatDate) + " at " + startZDT.format(formatTime) + ".\n");
                }
            }
            catch (Exception e) {
            }
        }
    }

    @FXML
    void exitButtonWasClicked(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Customer Additon");
        alert.setHeaderText("Would you like to exit?");
        alert.setContentText("Push OK to proceed. \nPress Cancel to stay on this screen.");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            try {
                FXMLLoader apptCalLoader = new FXMLLoader(AppointmentController.class.getResource("Appointment.fxml"));
                Parent apptCalScreen = apptCalLoader.load();
                Scene apptCalScene = new Scene(apptCalScreen);
                Stage apptCalStage = new Stage();
                apptCalStage.setTitle("Appointments");
                apptCalStage.setScene(apptCalScene);
                apptCalStage.show();
                Stage modCustStage = (Stage) exitButton.getScene().getWindow();
                modCustStage.close();
            }
            catch (IOException e) {
            }
        }
        else {
            alert.close();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
