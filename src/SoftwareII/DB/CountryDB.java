
package SoftwareII.DB;

import static SoftwareII.DB.ConnDB.DB_CONN;
import SoftwareII.Model.Country;
import static SoftwareII.ViewsAndControllers.LoginController.loggedUser;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CountryDB {
    
    public static ObservableList<Country> getAllCountriesList() {
        ObservableList<Country> allCountriesObservableList = FXCollections.observableArrayList();
        String getAllCountriesSQLStatement = "SELECT * FROM country";
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(getAllCountriesSQLStatement);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                Country countryToBeAddedToList = new Country();
                countryToBeAddedToList.setCountryId(rs.getInt("countryId"));
                countryToBeAddedToList.setCountry(rs.getString("country"));
                allCountriesObservableList.add(countryToBeAddedToList);
            }
        }
        catch (SQLException e) {
        }
        return allCountriesObservableList;
    }
    
    public static Country getCountryByIdFromCountry(int countryId) {
        String getCountryByIdSQLStatement = "SELECT * FROM country WHERE countryId = ?";
        Country getCountryById = new Country();
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(getCountryByIdSQLStatement);
            statement.setInt(1, countryId);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                getCountryById.setCountryId(rs.getInt("countryId"));
                getCountryById.setCountry(rs.getString("country"));
            }
        }
        catch (SQLException e) {
        }
        return getCountryById;
    }
    private static int getMaxCountryIdFromCountry() {
        int maxNumCountries = 0;
        String maxCountryIdSQLstatement = "SELECT MAX(countryId) FROM country";
        
        try {
            Statement statement = DB_CONN.createStatement();
            ResultSet rs = statement.executeQuery(maxCountryIdSQLstatement);
            
            if (rs.next()) {
                maxNumCountries = rs.getInt(1);
            }
        }
        catch (SQLException e) {
        }
        return maxNumCountries + 1;
    }
    
    public static int addCountryToDatabase(Country country) {
        String addCountrySQLStatement = String.join(" ",
                "INSERT INTO country (countryId, country, createDate, createdBy, lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, NOW(), ?, NOW(), ?)");
        
        int maxCountryId = getMaxCountryIdFromCountry();
        try {
            PreparedStatement stmt = DB_CONN.prepareStatement(addCountrySQLStatement);
            stmt.setInt(1, maxCountryId);
            stmt.setString(2, country.getCountry());
            stmt.setString(3, loggedUser.getUserName());
            stmt.setString(4, loggedUser.getUserName());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
        }
        return maxCountryId;
    }
    public static void updateCountryInDatabase(Country country) {
        String updateCountrySQLStatement = String.join(" ",
                "UPDATE country",
                "SET country=?, lastUpdate=NOW(), lastUpdateBy=?",
                "WHERE countryId=?");
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(updateCountrySQLStatement);
            statement.setString(1, country.getCountry());
            statement.setString(2, loggedUser.getUserName());
            statement.setInt(3, country.getCountryId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
        }
    }
    public void deleteCountryFromDatabase(Country country) {
        String deleteCountrySQLStatement = "DELETE FROM country WHERE countryId = ?";
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(deleteCountrySQLStatement);
            statement.setInt(1, country.getCountryId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
        }
    }
}
