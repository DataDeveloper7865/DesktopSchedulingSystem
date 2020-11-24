
package SoftwareII.ViewsAndControllers;

import SoftwareII.DB.AppointmentDB;
import SoftwareII.Model.Appointment;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import static SoftwareII.DB.AppointmentDB.getAppointmentsByWeek;
import static SoftwareII.DB.AppointmentDB.getAppointmentsByMonth;

public class AppointmentController implements Initializable {
    
    @FXML private Button newAppointmentButton;  
    @FXML private Button modifyAppointmentButton;  
    @FXML private Button deleteAppointmentButton;  
    @FXML private Button newCustomerButton;  
    @FXML private Button modifyCustomerButton;  
    @FXML private Button deleteCustomerButton; 
    @FXML private Button reportsButton;  
    @FXML private Button userLogsButton;  
    @FXML private Button exitButton;  
    @FXML private Tab weeklyAppointmentsTab;   
    @FXML private TableView<Appointment> weeklyAppointmentsTableView;  
    @FXML private TableColumn<Appointment, String> weeklyCustomerNameTableColumn;  
    @FXML private TableColumn<Appointment, String> weeklyAppointmentTitleTableColumn; 
    @FXML private TableColumn<Appointment, String> weeklyAppointmentDescriptionTableColumn; 
    @FXML private TableColumn<Appointment, String> weeklyAppointmentLocationTableColumn; 
    @FXML private TableColumn<Appointment, String> weeklyAppointmentContactTableColumn; 
    @FXML private TableColumn<Appointment, String> weeklyAppointmentStartTableColumn; 
    @FXML private Tab monthlyAppointmentsTab; 
    @FXML private TableView<Appointment> monthlyAppointmentsTableView; 
    @FXML private TableColumn<Appointment, String> monthlyCustomerNameTableColumn;
    @FXML private TableColumn<Appointment, String> monthlyAppointmentTitleTableColumn; 
    @FXML private TableColumn<Appointment, String> monthlyAppointmentDescriptionTableColumn; 
    @FXML private TableColumn<Appointment, String> monthlyAppointmentLocationTableColumn;
    @FXML private TableColumn<Appointment, String> monthlyAppointmentContactTableColumn;
    @FXML private TableColumn<Appointment, String> monthlyAppointmentStartTableColumn; 
    @FXML private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a z");
    @FXML public static Appointment selectedAppointment; 
    @FXML public static boolean isChanged; 
    
    @FXML
    void getNewAppointment(ActionEvent event) {
        Alert addAlert = new Alert(AlertType.CONFIRMATION);
        addAlert.setTitle("apptment add");
        addAlert.setHeaderText("Add New appointment?");
        addAlert.setContentText("OK to continue. \nCancel to return.");
        addAlert.showAndWait();
        if (addAlert.getResult() == ButtonType.OK) {
            try {
                FXMLLoader addAppointmentLoader = new FXMLLoader(appointmentAdderController.class.getResource("appointmentAdder.fxml"));
                Parent addAppointmentScreen = addAppointmentLoader.load();
                Scene addAppointmentScene = new Scene(addAppointmentScreen);
                Stage addAppointmentStage = new Stage();
                addAppointmentStage.setTitle("Add Appointment");
                addAppointmentStage.setScene(addAppointmentScene);
                addAppointmentStage.setResizable(false);
                addAppointmentStage.show();
                Stage appointmentCalendarStage = (Stage) newAppointmentButton.getScene().getWindow();
                appointmentCalendarStage.close();
            }
            catch (IOException e) {
            }
        }
    }
    
    @FXML
    void getModifyAppointment(ActionEvent event) {
        if (weeklyAppointmentsTab.isSelected()) {
            selectedAppointment = weeklyAppointmentsTableView.getSelectionModel().getSelectedItem();
            if (selectedAppointment == null) {
                Alert nullAlert = new Alert(AlertType.ERROR);
                nullAlert.setTitle("Appointment Mod Error");
                nullAlert.setHeaderText("No Appointment modified");
                nullAlert.setContentText("select apptment");
                nullAlert.showAndWait();
            }
            else {
                    Alert modAlert = new Alert(AlertType.CONFIRMATION);
                    modAlert.setTitle("Modify Appointment");
                    modAlert.setHeaderText("Would you like to modify the appointment?");
                    modAlert.setContentText("Push OK to modify. \nPress Cancel to return.");
                    modAlert.showAndWait();
                    if (modAlert.getResult() == ButtonType.OK) {
                        try {
                            FXMLLoader modifyAppointmentLoader = new FXMLLoader(EditAppointmentController.class.getResource("EditAppointment.fxml"));
                            Parent modifyAppointmentScreen = modifyAppointmentLoader.load();
                            Scene modifyAppointmentScene = new Scene(modifyAppointmentScreen);
                            Stage modifyAppointmentStage = new Stage();
                            modifyAppointmentStage.setTitle("Modify Appointment");
                            modifyAppointmentStage.setScene(modifyAppointmentScene);
                            modifyAppointmentStage.setResizable(false);
                            modifyAppointmentStage.show();
                            Stage appointmentCalendarStage = (Stage) modifyAppointmentButton.getScene().getWindow();
                            appointmentCalendarStage.close();
                        }
                        catch (IOException e) {
                        }
                    }
                }
            }
        else if (monthlyAppointmentsTab.isSelected()) {
            selectedAppointment = monthlyAppointmentsTableView.getSelectionModel().getSelectedItem();
            if (selectedAppointment == null) {
                Alert nullAlert = new Alert(AlertType.ERROR);
                nullAlert.setTitle("Appointment Error");
                nullAlert.setHeaderText("No appointment modified");
                nullAlert.setContentText("Select an appointment");
                nullAlert.showAndWait();
            }
            else {
                    Alert modAlert = new Alert(AlertType.CONFIRMATION);
                    modAlert.setTitle("Modify Appointment");
                    modAlert.setHeaderText("Would you like to modify the appointment?");
                    modAlert.setContentText("OK to proceed. \nCancel to return.");
                    modAlert.showAndWait();
                    if (modAlert.getResult() == ButtonType.OK) {
                        try {
                            FXMLLoader modifyAppointmentLoader = new FXMLLoader(EditAppointmentController.class.getResource("EditAppointment.fxml"));
                            Parent modifyAppointmentScreen = modifyAppointmentLoader.load();
                            Scene modifyAppointmentScene = new Scene(modifyAppointmentScreen);
                            Stage modifyAppointmentStage = new Stage();
                            modifyAppointmentStage.setTitle("Modify Appointment");
                            modifyAppointmentStage.setScene(modifyAppointmentScene);
                            modifyAppointmentStage.setResizable(false);
                            modifyAppointmentStage.show();
                            Stage appointmentCalendarStage = (Stage) modifyAppointmentButton.getScene().getWindow();
                            appointmentCalendarStage.close();
                        }
                        catch (IOException e) {
                        }
                    }
                }
        }
    }
    
    @FXML
    void getDeleteAppointment(ActionEvent event) {
        if (weeklyAppointmentsTab.isSelected()) {
            Alert delAlert = new Alert(AlertType.CONFIRMATION);
            delAlert.setTitle("Delete Appointment");
            delAlert.setHeaderText("Would you like to delete the appointment?");
            delAlert.setContentText("Push OK to proceed. \nPress Cancel to return.");
            delAlert.showAndWait();
            if (delAlert.getResult() == ButtonType.OK) {
                try {
                    Appointment appointment = weeklyAppointmentsTableView.getSelectionModel().getSelectedItem();
                    AppointmentDB.deleteAppointmentFromDatabase(appointment);
                    getAppointments();
                }
                catch (NullPointerException e) {
                    Alert nullAlert = new Alert(AlertType.ERROR);
                    nullAlert.setTitle("Appointment Error");
                    nullAlert.setHeaderText("The appointment cannot be deleted");
                    nullAlert.setContentText("Please select an appointment");
                    nullAlert.showAndWait();
                }
            }
            else {
                delAlert.close();
            }
        }
        else if (monthlyAppointmentsTab.isSelected()) {
            Alert delAlert = new Alert(AlertType.CONFIRMATION);
            delAlert.setTitle("Delete Appointment");
            delAlert.setHeaderText("Would you like to delete the appointment?");
            delAlert.setContentText("Press OK to proceed. \nPress Cancel to return.");
            delAlert.showAndWait();
            if (delAlert.getResult() == ButtonType.OK) {
                try {
                    Appointment appt = monthlyAppointmentsTableView.getSelectionModel().getSelectedItem();
                    AppointmentDB.deleteAppointmentFromDatabase(appt);
                    getAppointments();
                }
                catch (NullPointerException e) {
                    Alert nullAlert = new Alert(AlertType.ERROR);
                    nullAlert.setTitle("Appointment Error");
                    nullAlert.setHeaderText("The appointment cannot be deleted");
                    nullAlert.setContentText("Please select an appointment");
                    nullAlert.showAndWait();
                }
            }
            else {
                delAlert.close();
            }
        }
    }

    @FXML
    void getNewCustomer(ActionEvent event) {
        Alert addAlert = new Alert(AlertType.CONFIRMATION);
        addAlert.setTitle("Add Customer");
        addAlert.setHeaderText("Would you like to add a new customer?");
        addAlert.setContentText("Push OK to proceed. \nPress Cancel to return.");
        addAlert.showAndWait();
        if (addAlert.getResult() == ButtonType.OK) {
            try {
                FXMLLoader addCustomerLoader = new FXMLLoader(customerAdderController.class.getResource("customerAdder.fxml"));
                Parent addCustomerScreen = addCustomerLoader.load();
                Scene addCustomerScene = new Scene(addCustomerScreen);
                Stage addCustomerStage = new Stage();
                addCustomerStage.setTitle("Add Customer");
                addCustomerStage.setScene(addCustomerScene);
                addCustomerStage.setResizable(false);
                addCustomerStage.show();
                Stage appointmentCalendarStage = (Stage) newCustomerButton.getScene().getWindow();
                appointmentCalendarStage.close();
            }
            catch (IOException e) {
            }
        }
    }
    
    @FXML
    void getModifyCustomer(ActionEvent event) {
        Alert modifyAlert = new Alert(AlertType.CONFIRMATION);
        modifyAlert.setTitle("Modify Customer");
        modifyAlert.setHeaderText("Would you like to modify a customer?");
        modifyAlert.setContentText("Push OK to proceed. \nPush Cancel to return.");
        modifyAlert.showAndWait();
        if (modifyAlert.getResult() == ButtonType.OK) {
            try {
                isChanged=true;
                FXMLLoader selectCustomerLoader = new FXMLLoader(CustomerSelectionController.class.getResource("CustomerSelection.fxml"));
                Parent selectCustomerScreen = selectCustomerLoader.load();
                Scene selectCustomerScene = new Scene(selectCustomerScreen);
                Stage selectCustomerStage = new Stage();
                selectCustomerStage.setTitle("Customer Selection Screen");
                selectCustomerStage.setScene(selectCustomerScene);
                selectCustomerStage.setResizable(false);
                selectCustomerStage.show();
                Stage appointmentCalendarStage = (Stage) modifyCustomerButton.getScene().getWindow();
                appointmentCalendarStage.close();
            }
            catch (IOException e) {
            }
        }
        else {
            modifyAlert.close();
        }
    }     
    
    @FXML
    void getDeleteCustomer(ActionEvent event) {
        Alert deleteAlert = new Alert(AlertType.CONFIRMATION);
        deleteAlert.setTitle("Delete Customer");
        deleteAlert.setHeaderText("Delete the customer?");
        deleteAlert.setContentText("OK to proceed. \nCancel to return");
        deleteAlert.showAndWait();
        if (deleteAlert.getResult() == ButtonType.OK) {
            try {
                isChanged=false;
                FXMLLoader selectCustomerLoader = new FXMLLoader(CustomerSelectionController.class.getResource("CustomerSelection.fxml"));
                Parent selectCustomerScreen = selectCustomerLoader.load();
                Scene selectCustomerScene = new Scene(selectCustomerScreen);
                Stage selectCustomerStage = new Stage();
                selectCustomerStage.setTitle("Customer Selection");
                selectCustomerStage.setScene(selectCustomerScene);
                selectCustomerStage.setResizable(false);
                selectCustomerStage.show();
                Stage appointmentCalendarStage = (Stage) deleteCustomerButton.getScene().getWindow();
                appointmentCalendarStage.close();
            }
            catch (IOException e) {
            }
        }
        else {
            deleteAlert.close();
        }
    }

    @FXML
    void returnReports(ActionEvent event) {
        try {
            FXMLLoader reportLoader = new FXMLLoader(ReportController.class.getResource("Report.fxml"));
            Parent reportScreen = reportLoader.load();
            Scene reportScene = new Scene(reportScreen);
            Stage reportStage = new Stage();
            reportStage.setTitle("Reports");
            reportStage.setScene(reportScene);
            reportStage.setResizable(false);
            reportStage.show();
            Stage appointmentCalendarStage = (Stage) modifyCustomerButton.getScene().getWindow();
            appointmentCalendarStage.close();
        }
        catch (IOException e) {
        }
    }

    @FXML
    void returnUserLogs(ActionEvent event) {
        try {
            ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "userlogging.txt");
            pb.start();
        }
        catch (IOException e) {
        }
    }
    
    @FXML
    void exitAction(ActionEvent eExitButton) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Scheduling App");
        alert.setHeaderText("Would you like to exit the program?");
        alert.setContentText("Push OK to exit. \nPress Cancel to return.");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            Stage winMainScreen = (Stage)((Node)eExitButton.getSource()).getScene().getWindow();
            winMainScreen.close();
        }
        else {
            alert.close();
        }
    }
    
    public void getAppointments() {
        //Use of lambda efficiently sets the values of the table column
        //Writing separate functions for each of the columns would be tedious and unnecessary
        //Thus, not only does it consolidate the code but it makes it more readable for others
        weeklyCustomerNameTableColumn.setCellValueFactory(cellData -> { return cellData.getValue().getCustomer().customerNameProperty(); });
        weeklyAppointmentTitleTableColumn.setCellValueFactory(cellData -> { return cellData.getValue().titleProperty(); });
        weeklyAppointmentDescriptionTableColumn.setCellValueFactory(cellData -> { return cellData.getValue().descriptionProperty(); });
        weeklyAppointmentLocationTableColumn.setCellValueFactory(cellData -> { return cellData.getValue().locationProperty(); });
        weeklyAppointmentContactTableColumn.setCellValueFactory(cellData -> { return cellData.getValue().contactProperty(); });
        weeklyAppointmentStartTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStart().format(dateTimeFormatter)));
        weeklyAppointmentsTableView.setItems(getAppointmentsByWeek());
        monthlyCustomerNameTableColumn.setCellValueFactory(cellData -> { return cellData.getValue().getCustomer().customerNameProperty(); });
        monthlyAppointmentTitleTableColumn.setCellValueFactory(cellData -> { return cellData.getValue().titleProperty(); });
        monthlyAppointmentDescriptionTableColumn.setCellValueFactory(cellData -> { return cellData.getValue().descriptionProperty(); });
        monthlyAppointmentLocationTableColumn.setCellValueFactory(cellData -> { return cellData.getValue().locationProperty(); });
        monthlyAppointmentContactTableColumn.setCellValueFactory(cellData -> { return cellData.getValue().contactProperty(); });
        monthlyAppointmentStartTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStart().format(dateTimeFormatter)));
        monthlyAppointmentsTableView.setItems(getAppointmentsByMonth());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getAppointments();
    }
    
}
