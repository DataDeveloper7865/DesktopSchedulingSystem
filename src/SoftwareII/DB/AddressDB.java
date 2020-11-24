
package SoftwareII.DB;

import static SoftwareII.DB.ConnDB.DB_CONN;
import SoftwareII.Model.Address;
import static SoftwareII.ViewsAndControllers.LoginController.loggedUser;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddressDB {
    
    public static int getAddressIdFromAddress(String address) {
        String getAddressIdSQLStatement = "SELECT addressId FROM address WHERE address = ?";
        int startingAddressId = 0;
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(getAddressIdSQLStatement);
            statement.setString(1, address);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                startingAddressId = rs.getInt("addressId");
            }
        }
        catch (SQLException e) {
        }
        return startingAddressId;
    }
    
    public static Address getAddressFromAddressId(int addressId) {
        String getAddressByIdSQLStatement = "SELECT * FROM address WHERE addressId = ?";
        Address getAddressById = new Address();
        
        try {
            PreparedStatement stmt = DB_CONN.prepareStatement(getAddressByIdSQLStatement);
            stmt.setInt(1, addressId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                getAddressById.setAddressId(rs.getInt("addressId"));
                getAddressById.setAddress(rs.getString("address"));
                getAddressById.setAddress2(rs.getString("address2"));
                getAddressById.setCityId(rs.getInt("cityId"));
                getAddressById.setPostalCode(rs.getString("postalCode"));
                getAddressById.setPhone(rs.getString("phone"));
            }
        }
        catch (SQLException e) {
        }
        return getAddressById;
    }
    private static int getMaxNumberOfAddressId() {
        int maxAddressIdToBeReturned = 0;
        String maxAddressIdSQLStatement = "SELECT MAX(addressId) FROM address";
        
        try {
            Statement statement = DB_CONN.createStatement();
            ResultSet returnedResultSet = statement.executeQuery(maxAddressIdSQLStatement);
            
            if (returnedResultSet.next()) {
                maxAddressIdToBeReturned = returnedResultSet.getInt(1);
            }
        }
        catch (SQLException e) {
        }
        return maxAddressIdToBeReturned + 1;
    }
    public static int addAddressToDatabase(Address address) {
        String addAddressSQLToBeInserted = String.join(" ",
                "INSERT INTO address (addressId, address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?)");
        
        int incrementedAddressId = getMaxNumberOfAddressId();
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(addAddressSQLToBeInserted);
            statement.setInt(1, incrementedAddressId);
            statement.setString(2, address.getAddress());
            statement.setString(3, address.getAddress2());
            statement.setInt(4, address.getCityId());
            statement.setString(5, address.getPostalCode());
            statement.setString(6, address.getPhone());
            statement.setString(7, loggedUser.getUserName());
            statement.setString(8, loggedUser.getUserName());
            statement.executeUpdate();
        }
        catch (SQLException e) {
        }
        return incrementedAddressId;
    }
    public static void updateAddressInDatabase(Address address) {
        String updatedAddressSQLStatement = String.join(" ",
                "UPDATE address",
                "SET address=?, address2=?, cityId=?, postalCode=?, phone=?, lastUpdate=NOW(), lastUpdateBy=?",
                "WHERE addressId=?");
        
        try{
            PreparedStatement statement = DB_CONN.prepareStatement(updatedAddressSQLStatement);
            statement.setString(1, address.getAddress());
            statement.setString(2, address.getAddress2());
            statement.setInt(3, address.getCityId());
            statement.setString(4, address.getPostalCode());
            statement.setString(5, address.getPhone());
            statement.setString(6, loggedUser.getUserName());
            statement.setInt(7, address.getAddressId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
        }
    }
    public void deleteAddressFromDatabase(Address address) {
        String deleteAnAddressSQLStatement = "DELETE FROM address WHERE addressId = ?";
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(deleteAnAddressSQLStatement);
            statement.setInt(1, address.getAddressId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
        }
    }
}
