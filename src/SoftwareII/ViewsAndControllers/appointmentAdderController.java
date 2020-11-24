
package SoftwareII.ViewsAndControllers;

import SoftwareII.DB.AppointmentDB;
import SoftwareII.DB.CustomerDB;
import SoftwareII.Exceptions.AppointmentClassException;
import SoftwareII.Model.Appointment;
import SoftwareII.Model.Customer;
import static SoftwareII.ViewsAndControllers.LoginController.loggedUser;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;

public class appointmentAdderController implements Initializable {
    
    @FXML private Label customerLabel;  
    @FXML private Label titleLabel;  
    @FXML private Label descriptionLabel;  
    @FXML private Label locationLabel;    
    @FXML private Label contactLabel;   
    @FXML private Label typeLabel;    
    @FXML private Label urlLabel;   
    @FXML private Label startLabel;   
    @FXML private Label endLabel;   
    @FXML private TextField titleText;  
    @FXML private TextField descriptionText;  
    @FXML private TextField locationText;    
    @FXML private TextField contactText;     
    @FXML private TextField typeText;     
    @FXML private TextField urlText;   
    @FXML private Button saveButton;     
    @FXML private Button exitButton;       
    @FXML private ComboBox<Customer> customerComboBox;   
    @FXML private DatePicker startDatePicker;   
    @FXML private DatePicker endDatePicker;    
    @FXML private Spinner<LocalTime> startTimeSpinner;    
    @FXML private Spinner<LocalTime> endTimeSpinner;     
    @FXML public static Appointment appointment = new Appointment();   
    @FXML private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
    @FXML private final DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm"); 
    @FXML private static final ZoneId zoneID = ZoneId.systemDefault();  

    @FXML
    void getExitAction(ActionEvent eExitAction) {
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.setTitle("Exit Appointment Screen");
        exitAlert.setHeaderText("Proceed to exit?");
        exitAlert.setContentText("OK to exit adding appointment. \nPush Cancel to remain adding appointment.");
        exitAlert.showAndWait();
        if (exitAlert.getResult() == ButtonType.OK) {
            try {
                FXMLLoader appointmentCalendarLoader = new FXMLLoader(AppointmentController.class.getResource("Appointment.fxml"));
                Parent appointmentCalendarScreen = appointmentCalendarLoader.load();
                Scene appointmentCalendarScene = new Scene(appointmentCalendarScreen);
                Stage appointmentCalendarStage = new Stage();
                appointmentCalendarStage.setTitle("Appointment Adder");
                appointmentCalendarStage.setScene(appointmentCalendarScene);
                appointmentCalendarStage.show();
                Stage addAppointmentStage = (Stage) exitButton.getScene().getWindow();
                addAppointmentStage.close();
            }
            catch (IOException e) {
            }
        }
        else {
            exitAlert.close();
        }
    }

    @FXML
    void getSaveAction(ActionEvent eSaveAction) throws Exception {
        Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION);
        saveAlert.setTitle("Appt Save");
        saveAlert.setHeaderText("Save?");
        saveAlert.setContentText("OK to continue. \nCancel to stay.");
        saveAlert.showAndWait();
        if (saveAlert.getResult() == ButtonType.OK) {
            try {
                getApptInfo();
                appointment.inputCheck();
                appointment.noConcurrentAppointment();
                if (appointment.inputCheck() && appointment.noConcurrentAppointment()) {
                    AppointmentDB.addAppointmentToDatabase(appointment);
                    FXMLLoader appointmentCalendarLoader = new FXMLLoader(AppointmentController.class.getResource("Appointment.fxml"));
                    Parent appointmentCalendarScreen = appointmentCalendarLoader.load();
                    Scene appointmentCalendarScene = new Scene(appointmentCalendarScreen);
                    Stage appointmentCalendarStage = new Stage();
                    appointmentCalendarStage.setTitle("Appointments");
                    appointmentCalendarStage.setScene(appointmentCalendarScene);
                    appointmentCalendarStage.show();
                    Stage addAppointmentStage = (Stage) saveButton.getScene().getWindow();
                    addAppointmentStage.close();
                }
            }
            catch (AppointmentClassException e) {
                Alert exAlert = new Alert(Alert.AlertType.ERROR);
                exAlert.setTitle("Exception Encountered");
                exAlert.setHeaderText("Appt Exception");
                exAlert.setContentText(e.getMessage());
                exAlert.showAndWait().filter(response -> response == ButtonType.OK);
            }
        }
        else {
            saveAlert.close();
        }
    }
    
    public void getApptInfo() throws AppointmentClassException {
        try {
            appointment.setCustomer(customerComboBox.getValue());
            appointment.setCustomerId(customerComboBox.getValue().getCustomerId());
            appointment.setUserId(loggedUser.getUserId());
            appointment.setTitle(titleText.getText());
            appointment.setDescription(descriptionText.getText());
            appointment.setLocation(locationText.getText());
            appointment.setContact(contactText.getText());
            appointment.setType(typeText.getText());
            appointment.setUrl(urlText.getText());
            appointment.setStart(ZonedDateTime.of(LocalDate.parse(startDatePicker.getValue().toString(), formatDate), LocalTime.parse(startTimeSpinner.getValue().toString(), formatTime), zoneID));
            appointment.setEnd(ZonedDateTime.of(LocalDate.parse(endDatePicker.getValue().toString(), formatDate), LocalTime.parse(endTimeSpinner.getValue().toString(), formatTime), zoneID));
        }
        catch (NullPointerException e) {
            Alert nullAlert = new Alert(Alert.AlertType.ERROR);
            nullAlert.setTitle("Error");
            nullAlert.setHeaderText("appointment cannot be added to DB");
            nullAlert.setContentText("please adjust selection");
            nullAlert.showAndWait();
        }
    }
    
    public void getActiveCustomers() {
        ObservableList<Customer> activeCustomers = CustomerDB.getActiveCustomersInDatabase();
        customerComboBox.setItems(activeCustomers);
        customerComboBox.setPromptText("Select customer");
    }
    
    public void convertCustomerString() {
        customerComboBox.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer customer) {
                return customer.getCustomerName();
            }

            @Override
            public Customer fromString(String string) {
                return customerComboBox.getValue();
            }
        });
    }
    
    public void setDefaultDateTime() {
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now());
        startTimeSpinner.setValueFactory(spinnerValueFactoryStart);
        spinnerValueFactoryStart.setValue(LocalTime.of(8, 00));
        endTimeSpinner.setValueFactory(spinnerValueFactoryEnd);
        spinnerValueFactoryEnd.setValue(LocalTime.of(17, 00));
    }
    
    SpinnerValueFactory spinnerValueFactoryStart = new SpinnerValueFactory<LocalTime>() {
        {
            setConverter(new LocalTimeStringConverter(formatTime,null));
        }
        @Override public void decrement(int steps) {
            LocalTime time = (LocalTime) getValue();
            setValue(time.minusHours(steps));
            setValue(time.minusMinutes(16 - steps));
        }
        @Override public void increment(int steps) {
            LocalTime time = (LocalTime) getValue();
            setValue(time.plusHours(steps));
            setValue(time.plusMinutes(steps + 14));
        }
    };
    
    SpinnerValueFactory spinnerValueFactoryEnd = new SpinnerValueFactory<LocalTime>() {
        {
            setConverter(new LocalTimeStringConverter(formatTime,null));
        }
        @Override
        public void decrement(int steps) {
            LocalTime time = (LocalTime) getValue();
            setValue(time.minusHours(steps));
            setValue(time.minusMinutes(16 - steps));
        }
        @Override
        public void increment(int steps) {
            LocalTime time = (LocalTime) getValue();
            setValue(time.plusHours(steps));
            setValue(time.plusMinutes(steps + 14));
        }
    };


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getActiveCustomers();
        convertCustomerString();
        setDefaultDateTime();
    }    
    
}
