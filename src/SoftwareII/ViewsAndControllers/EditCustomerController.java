
package SoftwareII.ViewsAndControllers;

import SoftwareII.DB.AddressDB;
import SoftwareII.DB.CityDB;
import SoftwareII.DB.CountryDB;
import SoftwareII.DB.CustomerDB;
import SoftwareII.Exceptions.CustomerClassException;
import SoftwareII.Model.Address;
import SoftwareII.Model.City;
import SoftwareII.Model.Country;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import static SoftwareII.ViewsAndControllers.CustomerSelectionController.selectedCustomer;

public class EditCustomerController implements Initializable {

    @FXML private Label customerNameLabel;
    @FXML private Label customerAddressLabel;
    @FXML private Label customerAddress2Label;
    @FXML private Label cityLabel;
    @FXML private Label customerPostalCodeLabel;
    @FXML private Label customerPhoneLabel;
    @FXML private TextField customerAddressText; 
    @FXML private TextField customerAddress2Text;
    @FXML private TextField customerCityText;
    @FXML private TextField customerPostalCodeText;
    @FXML private TextField customerPhoneText;
    @FXML private Button saveButton;
    @FXML private Button exitButton;
    @FXML private TextField customerNameText;
    @FXML private Label countryLabel;
    @FXML private ComboBox<Country> countryComboBox;
    
    @FXML
    private Address customerAddress = AddressDB.getAddressFromAddressId(selectedCustomer.getAddressId());
    private City customerCity = CityDB.getCityByIdFromCity(customerAddress.getCityId());
    private Country customerCountry = CountryDB.getCountryByIdFromCountry(customerCity.getCountryId());

    @FXML
    void exitButtonWasPushed(ActionEvent eExitButton) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Exit?");
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
    void saveButtonWasPushed(ActionEvent event) {
        Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION);
        saveAlert.setTitle("Save");
        saveAlert.setHeaderText("Save?");
        saveAlert.setContentText("OK to proceed. \nCancel to return.");
        saveAlert.showAndWait();
        if (saveAlert.getResult() == ButtonType.OK) {
            try {
                updateCustomerInformation();
                if (Customer.customerInputCheck(selectedCustomer, customerAddress, customerCity, customerCountry)) {
                    try {
                        CustomerDB.updateCustomerInDatabase(selectedCustomer);
                        AddressDB.updateAddressInDatabase(customerAddress);
                        CityDB.updateCityInDatabase(customerCity);
                        FXMLLoader appointmentCalendarLoader = new FXMLLoader(AppointmentController.class.getResource("Appointment.fxml"));
                        Parent appointmentCalendarScreen = appointmentCalendarLoader.load();
                        Scene appointmentCalendarScene = new Scene(appointmentCalendarScreen);
                        Stage appointmentCalendarStage = new Stage();
                        appointmentCalendarStage.setTitle("Appointments");
                        appointmentCalendarStage.setScene(appointmentCalendarScene);
                        appointmentCalendarStage.show();
                        Stage modifyCustomerStage = (Stage) saveButton.getScene().getWindow();
                        modifyCustomerStage.close();
                    }
                    catch (IOException e) {
                    }
                }
            }
            catch (CustomerClassException e) {
                Alert exAlert = new Alert(Alert.AlertType.ERROR);
                exAlert.setTitle("Customer Exception Encountered");
                exAlert.setHeaderText("Customer Exception Encountered.");
                exAlert.setContentText(e.getMessage());
                exAlert.showAndWait().filter(response -> response == ButtonType.OK);
            }
        }
        else {
            saveAlert.close();
        }
    }
    
    public void updateCustomerInformation() {
        try {
            selectedCustomer.setCustomerName(customerNameText.getText());
            customerAddress.setAddress(customerAddressText.getText());
            customerAddress.setAddress2(customerAddress2Text.getText());
            customerAddress.setPostalCode(customerPostalCodeText.getText());
            customerAddress.setPhone(customerPhoneText.getText());
            customerCity.setCity(customerCityText.getText());
            customerCity.setCountryId(countryComboBox.getSelectionModel().getSelectedItem().getCountryId());
        }
        catch (NullPointerException e) {
            Alert nullAlert = new Alert(Alert.AlertType.ERROR);
            nullAlert.setTitle("Customer Error");
            nullAlert.setHeaderText("Customer cannot be added");
            nullAlert.setContentText("Error:" + "\n" + e.getCause().toString());
            nullAlert.showAndWait();
        }
    }
    
    public void setCountries() {
        countryComboBox.setItems(CountryDB.getAllCountriesList());
    }
    
    public void setSelectedCustomerInformation() {
        customerNameText.setText(selectedCustomer.getCustomerName());
        customerAddressText.setText(customerAddress.getAddress());
        customerAddress2Text.setText(customerAddress.getAddress2());
        customerPostalCodeText.setText(customerAddress.getPostalCode());
        customerPhoneText.setText(customerAddress.getPhone());
        customerCityText.setText(customerCity.getCity());
        countryComboBox.setValue(customerCountry);
    }
    
    public void countryToStringConverter() {
        countryComboBox.setConverter(new StringConverter<Country>() {
            @Override
            public String toString(Country country) {
                return country.getCountry();
            }

            @Override
            public Country fromString(String string) {
                return countryComboBox.getValue();
            }
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setCountries();
        countryToStringConverter();
        setSelectedCustomerInformation();
    }    
    
}
