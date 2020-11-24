
package SoftwareII.DB;

import static SoftwareII.DB.ConnDB.DB_CONN;
import SoftwareII.Model.Customer;
import static SoftwareII.ViewsAndControllers.LoginController.loggedUser;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomerDB {
    
    public static ObservableList<Customer> getActiveCustomersInDatabase() {
        ObservableList<Customer> currentActiveCustomers = FXCollections.observableArrayList();
        String getActiveCustomersSQLStatment = "SELECT * FROM customer WHERE active = 1";
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(getActiveCustomersSQLStatment);
            ResultSet resultset = statement.executeQuery();
            
            while (resultset.next()) {
                Customer curActiveCustomer = new Customer();
                curActiveCustomer.setCustomerId(resultset.getInt("customerId"));
                curActiveCustomer.setCustomerName(resultset.getString("customerName"));
                curActiveCustomer.setAddressId(resultset.getInt("addressId"));
                curActiveCustomer.setActive(resultset.getBoolean("active"));
                currentActiveCustomers.add(curActiveCustomer);
            }
        }
        catch (SQLException e) {
        }
        return currentActiveCustomers;
    }
    
    public static ObservableList<Customer> getAllCustomersFromDatabase() {
        ObservableList<Customer> allCustomersObservableList = FXCollections.observableArrayList();
        String getAllCustomersSQLStatement = "SELECT * FROM customer";
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(getAllCustomersSQLStatement);
            ResultSet resultset = statement.executeQuery();
            
            while (resultset.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(resultset.getInt("customerId"));
                customer.setCustomerName(resultset.getString("customerName"));
                customer.setAddressId(resultset.getInt("addressId"));
                customer.setActive(resultset.getBoolean("active"));
                allCustomersObservableList.add(customer);
            }
        }
        catch (SQLException e) {
        }
        return allCustomersObservableList;
    }
    
    public static Customer getActiveCustomerById(int customerId) {
        String getCustomerByIdSQLStatement = "SELECT * FROM customer WHERE customerId = ? AND active=1";
        Customer curSearchedCustomerById = new Customer();
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(getCustomerByIdSQLStatement);
            statement.setInt(1, customerId);
            ResultSet resultset = statement.executeQuery();
            
            if (resultset.next()) {
                curSearchedCustomerById.setCustomerId(resultset.getInt("customerId"));
                curSearchedCustomerById.setCustomerName(resultset.getString("customerName"));
                curSearchedCustomerById.setAddressId(resultset.getInt("addressId"));
                curSearchedCustomerById.setActive(resultset.getBoolean("active"));
                curSearchedCustomerById.setLastUpdate(resultset.getTimestamp("lastUpdate"));
                curSearchedCustomerById.setLastUpdateBy(resultset.getString("lastUpdateBy"));
            }
        }
        catch (SQLException e) {
        }
        return curSearchedCustomerById;
    }
    
    private static int getMaxFromCustomer() {
        int maxCustomer = 0;
        String maxCustomerIdSQLStatement = "SELECT MAX(customerId) FROM customer";
        
        try {
            Statement statement = DB_CONN.createStatement();
            ResultSet resultset = statement.executeQuery(maxCustomerIdSQLStatement);
            
            if (resultset.next()) {
                maxCustomer = resultset.getInt(1);
            }
        }
        catch (SQLException e) {
        }
        return maxCustomer + 1;
    }
    
    public static Customer addCustomerToDatabase(Customer customer) {
        String addCustomerSQLStatement = String.join(" ", 
                "INSERT INTO customer (customerId, customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, ?, 1, NOW(), ?, NOW(), ?)");
        
        int maxCustomerId = getMaxFromCustomer();
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(addCustomerSQLStatement);
            statement.setInt(1, maxCustomerId);
            statement.setString(2, customer.getCustomerName());
            statement.setInt(3, customer.getAddressId());
            statement.setString(4, loggedUser.getUserName());
            statement.setString(5, loggedUser.getUserName());
            statement.executeUpdate();
        }
        catch (SQLException e) {
        }
        return customer;
    }
    
    public static void updateCustomerInDatabase(Customer customer) {
        String updateCustomerSQLStatement = String.join(" ", 
                "UPDATE customer",
                "SET customerName=?, addressId=?, lastUpdate=NOW(), lastUpdateBy=?",
                "WHERE customerId = ?");
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(updateCustomerSQLStatement);
            statement.setString(1, customer.getCustomerName());
            statement.setInt(2, customer.getAddressId());
            statement.setString(3, loggedUser.getUserName());
            statement.setInt(4, customer.getCustomerId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
        }
    }
    

    public static void deleteCustomerFromDatabase(Customer customer) {
        String deleteCustomerSQL = "UPDATE customer SET active=0 WHERE customerId = ?";
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(deleteCustomerSQL);
            statement.setInt(1, customer.getCustomerId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
        }
    }
}