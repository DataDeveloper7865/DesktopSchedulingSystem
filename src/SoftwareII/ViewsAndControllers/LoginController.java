
package SoftwareII.ViewsAndControllers;

import SoftwareII.DB.AppointmentDB;
import SoftwareII.DB.UserDB;
import SoftwareII.Model.Appointment;
import SoftwareII.Model.User;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable {
    ResourceBundle resourceBundle;
    Locale userLocale;
    Logger userLog = Logger.getLogger("userlogging.txt");
    
    @FXML private Label alertLabel;
    @FXML private TextField passwordText;
    @FXML private TextField usernameText;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Button loginButton;
    @FXML private Button exitButton;

    @FXML
    void exitButtonWasClicked(ActionEvent eExitButton) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Exit the program?");
        alert.setContentText("OK to proceed. \nCancel to return.");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            Stage winMainScreen = (Stage)((Node)eExitButton.getSource()).getScene().getWindow();
            winMainScreen.close();
        }
        else {
            alert.close();
        }
    }

    @FXML
    void loginButtonWasClicked(ActionEvent eLogin) throws IOException, Exception {
        String username = usernameText.getText();
        String password = passwordText.getText();
        loggedUser.setUserName(username);
        loggedUser.setPassword(password);
        
        FileHandler userLogFileHandler = new FileHandler("userlogging.txt", true);
        SimpleFormatter simpleFormatter = new SimpleFormatter();
        userLogFileHandler.setFormatter(simpleFormatter);
        userLog.addHandler(userLogFileHandler);
        userLog.setLevel(Level.INFO);
        
        try {
            ObservableList<User> userLoginInfo = UserDB.getCurrentActiveUsers();
            //use of lambda makes code more readable.
            userLoginInfo.forEach((u) -> {
                try {
                    assert loggedUser.getUserName().equals(u.getUserName()) && loggedUser.getPassword().equals(u.getPassword()) : "Enter correct login information";
                    loggedUser.setUserId(u.getUserId());
                    try {
                        Appointment upcomingAppt = AppointmentDB.getUpcomingAppointment15min();
                        if (!(upcomingAppt.getAppointmentId() == 0)) {
                            Alert apptAlert = new Alert(Alert.AlertType.INFORMATION);
                            apptAlert.setTitle("Alert");
                            apptAlert.setHeaderText("Upcoming Appointment");
                            apptAlert.setContentText("You have an appointment!" 
                                    + "\nON " + upcomingAppt.getStart().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
                                    + "\nAT " + upcomingAppt.getStart().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL))
                                    + " with " + upcomingAppt.getCustomer().getCustomerName() + ".");
                            apptAlert.showAndWait();
                            if (apptAlert.getResult() == ButtonType.OK) {
                                userLog.log(Level.INFO, "{0} logged in.", loggedUser.getUserName());
                                Stage loginStage = (Stage) loginButton.getScene().getWindow();
                                loginStage.close();
                                FXMLLoader appointmentCalendarLoader = new FXMLLoader(AppointmentController.class.getResource("Appointment.fxml"));
                                Parent appointmentCalendarScreen = appointmentCalendarLoader.load();
                                Scene appointmentCalendarScene = new Scene(appointmentCalendarScreen);
                                Stage appointmentCalendarStage = new Stage();
                                appointmentCalendarStage.setTitle("Appointments");
                                appointmentCalendarStage.setScene(appointmentCalendarScene);
                                appointmentCalendarStage.show();
                            }
                            else {
                                apptAlert.close();
                            }
                        }
                        else {
                            userLog.log(Level.INFO, "{0} logged in.", loggedUser.getUserName());
                            FXMLLoader appointmentCalendarLoader = new FXMLLoader(AppointmentController.class.getResource("Appointment.fxml"));
                            Parent appointmentCalendarScreen = appointmentCalendarLoader.load();
                            Scene appointmentCalendarScene = new Scene(appointmentCalendarScreen);
                            Stage appointmentCalendarStage = new Stage();
                            appointmentCalendarStage.setTitle("Appointments");
                            appointmentCalendarStage.setScene(appointmentCalendarScene);
                            appointmentCalendarStage.show();
                            Stage loginStage = (Stage) loginButton.getScene().getWindow();
                            loginStage.close();
                        }
                    }
                    catch (IOException e) {
                    }
                }
                catch (AssertionError e) {
                    System.out.println(e.getMessage());
                    this.alertLabel.setText(this.resourceBundle.getString("userNameErrorAlert") + ".");
                    userLog.log(Level.WARNING, "Wrong User/Password Combo, User: {0}", loggedUser.getUserName());
                }
            });
        }
        catch (Exception e) {
        }
    }
    
    public static User loggedUser = new User();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.userLocale = Locale.getDefault();
        this.resourceBundle = ResourceBundle.getBundle("SoftwareII.LocaleLanguages/Languages", this.userLocale); 
        this.usernameLabel.setText(this.resourceBundle.getString("username") + ":");
        this.passwordLabel.setText(this.resourceBundle.getString("password") + ":");
        this.usernameText.setPromptText(this.resourceBundle.getString("usernamePrompt"));
        this.passwordText.setPromptText(this.resourceBundle.getString("passwordPrompt"));
        this.loginButton.setText(this.resourceBundle.getString("loginButtonText"));
        this.exitButton.setText(this.resourceBundle.getString("exitButtonText"));
    }
}
