
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
import static SoftwareII.ViewsAndControllers.AppointmentController.selectedAppointment;

public class EditAppointmentController implements Initializable {
    
    @FXML private Label customerLabel;
    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label locationLabel;
    @FXML private Label contactLabel;
    @FXML private Label typeLabel;
    @FXML private Label urlLabel;
    @FXML private Label startLabel;
    @FXML private Label endLabel;
    @FXML private ComboBox<Customer> customerComboBox;
    @FXML private TextField titleText;
    @FXML private TextField descriptionText;
    @FXML private TextField locationText;
    @FXML private TextField contactText;
    @FXML private TextField typeText;
    @FXML private TextField urlText;
    @FXML private TextField txtStart;
    @FXML private TextField txtEnd;
    @FXML private Button saveButton;
    @FXML private Button exitButton; 
    @FXML private DatePicker startDatePicker;
    @FXML private Spinner<LocalTime> startTimeSpinner;
    @FXML private DatePicker endDatePicker;
    @FXML private Spinner<LocalTime> endTimeSpinner;
    @FXML private final String emptyInputError = new String();
    @FXML private final String validationError = new String();
    @FXML public static Appointment appointment = new Appointment();
    @FXML private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
    @FXML private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
    @FXML private static final ZoneId zoneID = ZoneId.systemDefault();
    
    @FXML
    void ExitButtonWasClicked(ActionEvent eExitAction) {
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.setTitle("Exit Application");
        exitAlert.setHeaderText("Exit?");
        exitAlert.setContentText("OK to proceed. \nCancel to return.");
        exitAlert.showAndWait();
        if (exitAlert.getResult() == ButtonType.OK) {
            try {
                FXMLLoader appointmentCalendarLoader = new FXMLLoader(AppointmentController.class.getResource("Appointment.fxml"));
                Parent appointmentCalendarScreen = appointmentCalendarLoader.load();
                Scene appointmentCalendarScene = new Scene(appointmentCalendarScreen);
                Stage appointmentCalendarStage = new Stage();
                appointmentCalendarStage.setTitle("Appointments");
                appointmentCalendarStage.setScene(appointmentCalendarScene);
                appointmentCalendarStage.show();
                Stage modifyAppointmentStage = (Stage) exitButton.getScene().getWindow();
                modifyAppointmentStage.close();
            }
            catch (IOException e) {
            }
        }
        else {
            exitAlert.close();
        }
    }

    @FXML
    void saveButtonWasClicked(ActionEvent eSaveAction) throws Exception {
        Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION);
        saveAlert.setTitle("Save Screen");
        saveAlert.setHeaderText("Save?");
        saveAlert.setContentText("OK to continue. \nCancel to return.");
        saveAlert.showAndWait();
        if (saveAlert.getResult() == ButtonType.OK) {
            try {
                updateAppointmentInfo();
                appointment.inputCheck();
                appointment.noConcurrentAppointment();
                if (appointment.inputCheck() && appointment.noConcurrentAppointment()) {
                    AppointmentDB.updateAppointmentInDatabase(appointment);
                    FXMLLoader appointmentCalendarLoader = new FXMLLoader(AppointmentController.class.getResource("Appointment.fxml"));
                    Parent appointmentCalendarScreen = appointmentCalendarLoader.load();
                    Scene appointmentCalendarScene = new Scene(appointmentCalendarScreen);
                    Stage appointmentCalendarStage = new Stage();
                    appointmentCalendarStage.setTitle("Appointments");
                    appointmentCalendarStage.setScene(appointmentCalendarScene);
                    appointmentCalendarStage.show();
                    Stage modifyAppointmentStage = (Stage) saveButton.getScene().getWindow();
                    modifyAppointmentStage.close();
                }
            }
            catch (AppointmentClassException e) {
                Alert exAlert = new Alert(Alert.AlertType.ERROR);
                exAlert.setTitle("Exception Encountered");
                exAlert.setHeaderText("Appointment Exception Occurred!");
                exAlert.setContentText(e.getMessage());
                exAlert.showAndWait().filter(response -> response == ButtonType.OK);
            }
        }
        else {
            saveAlert.close();
        }
    }
    
    private final ObservableList<Customer> activeCustomers = CustomerDB.getActiveCustomersInDatabase();
    
    public void setComboBoxActiveCustomers() {
        customerComboBox.setItems(activeCustomers);
    }
    
    public void customerStringConverter() {
        customerComboBox.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer cust) {
                return cust.getCustomerName();
            }

            @Override
            public Customer fromString(String string) {
                return customerComboBox.getValue();
            }
        });
    }
    
    public void setSelectedAppointmentInfo() {
        customerComboBox.setValue(selectedAppointment.getCustomer());
        titleText.setText(selectedAppointment.getTitle());
        descriptionText.setText(selectedAppointment.getDescription());
        locationText.setText(selectedAppointment.getLocation());
        contactText.setText(selectedAppointment.getContact());
        typeText.setText(selectedAppointment.getType());
        urlText.setText(selectedAppointment.getUrl());
        startDatePicker.setValue(selectedAppointment.getStart().toLocalDate());
        startTimeSpinner.setValueFactory(spinnerValueFactoryStart);
        spinnerValueFactoryStart.setValue(selectedAppointment.getStart().toLocalTime());
        endDatePicker.setValue(selectedAppointment.getEnd().toLocalDate());
        endTimeSpinner.setValueFactory(spinnerValueFactoryEnd);
        spinnerValueFactoryEnd.setValue(selectedAppointment.getEnd().toLocalTime());
    }
    
    public void updateAppointmentInfo() {
        appointment.setCustomerId(customerComboBox.getValue().getCustomerId());
        appointment.setUserId(loggedUser.getUserId());
        appointment.setTitle(titleText.getText());
        appointment.setDescription(descriptionText.getText());
        appointment.setLocation(locationText.getText());
        appointment.setContact(contactText.getText());
        appointment.setType(typeText.getText());
        appointment.setUrl(urlText.getText());
        appointment.setStart(ZonedDateTime.of(LocalDate.parse(startDatePicker.getValue().toString(), dateFormat), LocalTime.parse(startTimeSpinner.getValue().toString(), timeFormat), zoneID));
        appointment.setEnd(ZonedDateTime.of(LocalDate.parse(endDatePicker.getValue().toString(), dateFormat), LocalTime.parse(endTimeSpinner.getValue().toString(), timeFormat), zoneID));
        appointment.setAppointmentId(selectedAppointment.getAppointmentId());
    }

    SpinnerValueFactory spinnerValueFactoryStart = new SpinnerValueFactory<LocalTime>() {
        {
            setConverter(new LocalTimeStringConverter(timeFormat,null));
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
    
    SpinnerValueFactory spinnerValueFactoryEnd = new SpinnerValueFactory<LocalTime>() {
        {
            setConverter(new LocalTimeStringConverter(timeFormat,null));
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
        setComboBoxActiveCustomers();
        setSelectedAppointmentInfo();
        customerStringConverter();
    }
    
}
