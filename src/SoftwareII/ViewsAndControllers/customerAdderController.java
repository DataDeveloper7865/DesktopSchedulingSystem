
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

public class customerAdderController implements Initializable {
    
    @FXML private Label customerNameLabel;  
    @FXML private Label customerAddressLabel;   
    @FXML private Label customerAddress2Label;  
    @FXML private Label cityLabel;    
    @FXML private Label countryLabel;  
    @FXML private Label customerPostalCodeLabel;  
    @FXML private Label customerPhoneLabel;   
    @FXML private TextField customerNameText; 
    @FXML private TextField customerAddressText;   
    @FXML private TextField customerAddress2Text;   
    @FXML private TextField customerCityText;   
    @FXML private ComboBox<Country> countryComboBox;  
    @FXML private TextField customerPostalCodeText; 
    @FXML private TextField customerPhoneText;   
    @FXML private Button saveButton;  
    @FXML private Button exitButton;  
    
    @FXML
    private final Customer newCustomer = new Customer();
    private final Address customerAddress = new Address();
    private final City customerCity = new City();
    private final Country customerCountry = new Country();

    @FXML
    void exitButtonWasClicked(ActionEvent eExit) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Customer Add");
        alert.setHeaderText("exit?");
        alert.setContentText("Push OK to exit. \nPress Cancel to remain on screen.");
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

    @FXML
    void saveButtonWasClicked(ActionEvent event) {
        Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION);
        saveAlert.setTitle("Save Customer Modifications");
        saveAlert.setHeaderText("Would you like to save cusotmer?");
        saveAlert.setContentText("Press OK to save. \nPress Cancel to remain.");
        saveAlert.showAndWait();
        if (saveAlert.getResult() == ButtonType.OK) {
            try {
                getCustomerInfo();
                if (Customer.customerInputCheck(newCustomer, customerAddress, customerCity, customerCountry)) {
                    try {
                        CityDB.addCityToDatabase(customerCity);
                        customerAddress.setCityId(CityDB.getCityId(customerCity.getCity()));
                        AddressDB.addAddressToDatabase(customerAddress);
                        newCustomer.setAddressId(AddressDB.getAddressIdFromAddress(customerAddress.getAddress()));
                        CustomerDB.addCustomerToDatabase(newCustomer);
                        FXMLLoader appointmentCalendarLoader = new FXMLLoader(AppointmentController.class.getResource("Appointment.fxml"));
                        Parent appointmentCalendarScreen = appointmentCalendarLoader.load();
                        Scene appointmentCalendarScene = new Scene(appointmentCalendarScreen);
                        Stage appointmentCalendarStage = new Stage();
                        appointmentCalendarStage.setTitle("Appointments");
                        appointmentCalendarStage.setScene(appointmentCalendarScene);
                        appointmentCalendarStage.show();
                        Stage addCustStage = (Stage) saveButton.getScene().getWindow();
                        addCustStage.close();
                    }
                    catch (IOException e) {
                    }
                }
            }
            catch (CustomerClassException e) {
                Alert exAlert = new Alert(Alert.AlertType.ERROR);
                exAlert.setTitle("Exception Found");
                exAlert.setHeaderText("The program encountered an exception");
                exAlert.setContentText(e.getMessage());
                exAlert.showAndWait().filter(response -> response == ButtonType.OK);
            }
        }
        else {
            saveAlert.close();
        }
    }
    
    public void getCustomerInfo() {
        try {
            newCustomer.setCustomerName(customerNameText.getText());
            customerAddress.setAddress(customerAddressText.getText());
            customerAddress.setAddress2(customerAddress2Text.getText());
            customerAddress.setPostalCode(customerPostalCodeText.getText());
            customerAddress.setPhone(customerPhoneText.getText());
            customerCity.setCity(customerCityText.getText());
            customerCity.setCountryId(countryComboBox.getSelectionModel().getSelectedItem().getCountryId());
            customerCountry.setCountry(countryComboBox.getSelectionModel().getSelectedItem().getCountry());
        }
        catch (NullPointerException e) {
            Alert nullAlert = new Alert(Alert.AlertType.ERROR);
            nullAlert.setTitle("Can't Add Customer");
            nullAlert.setHeaderText("Can't addd to database");
            nullAlert.setContentText("");
            nullAlert.showAndWait();
        }
    }
    
     public void setCountries() {
        countryComboBox.setItems(CountryDB.getAllCountriesList());
    }
    
    public void convertCountryString() {
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
        convertCountryString();
        setCountries();
    }    
    
}
