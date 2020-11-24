package SoftwareII.Model;

import SoftwareII.Exceptions.CustomerClassException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customer {
    private final IntegerProperty customerId = new SimpleIntegerProperty();
    private final StringProperty customerName = new SimpleStringProperty();
    private final IntegerProperty addressId = new SimpleIntegerProperty();
    private final BooleanProperty active = new SimpleBooleanProperty();
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    
    public Customer () {
    }

    public int getCustomerId() {
        return customerId.get();
    }

    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }
    
    public IntegerProperty customerIdProperty() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }
    
    public StringProperty customerNameProperty() {
        return customerName;
    }

    public int getAddressId() {
        return addressId.get();
    }

    public void setAddressId(int addressId) {
        this.addressId.set(addressId);
    }
    
    public IntegerProperty addressIdProperty() {
        return addressId;
    }

    public boolean isActive() {
        return active.get();
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }
    
    public BooleanProperty activeProperty() {
        return active;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
    
    public static boolean customerInputCheck(Customer customer, Address customerAddress, City customerCity, Country customerCountry) throws CustomerClassException {
        if (customer.getCustomerName().equals("")) {
            throw new CustomerClassException("Check customer name");
        }
        if (customerAddress.getAddress().equals("")) {
            throw new CustomerClassException("Check valid address");
        }
        if (customerAddress.getPhone().equals("")) {
            throw new CustomerClassException("Check phone number");
        }
        if (customerAddress.getPostalCode().equals("")) {
            throw new CustomerClassException("Check postal code");
        }
        if (customerCity.getCity().equals("")) {
            throw new CustomerClassException("Check city");
        }
        if (customerCountry.getCountry().equals("")) {
            throw new CustomerClassException("Check country");
        }
        return true;
    }
}