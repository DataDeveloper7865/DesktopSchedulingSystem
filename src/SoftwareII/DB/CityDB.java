
package SoftwareII.DB;

import static SoftwareII.DB.ConnDB.DB_CONN;
import SoftwareII.Model.City;
import static SoftwareII.ViewsAndControllers.LoginController.loggedUser;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CityDB {
    
    public static int getCityId(String city) {
        String getCitySQLStatement = "SELECT cityId FROM city WHERE city = ?";
        int cityId = 0;
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(getCitySQLStatement);
            statement.setString(1, city);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                cityId = rs.getInt("cityId");
            }
        }
        catch (SQLException e) {
        }
        return cityId;
    }
    
    public static City getCityByIdFromCity(int cityId) {
        String getCityByIdSQLStatement = "SELECT * FROM city WHERE cityId = ?";
        City getCityById = new City();
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(getCityByIdSQLStatement);
            statement.setInt(1, cityId);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                getCityById.setCityId(rs.getInt("cityId"));
                getCityById.setCity(rs.getString("city"));
                getCityById.setCountryId(rs.getInt("countryId"));
            }
        }
        catch (SQLException e) {
        }
        return getCityById;
    }
    
    private static int getMaximumCities() {
        int maxCityId = 0;
        String maxCitySQLStatement = "SELECT MAX(cityId) FROM city";
        
        try {
            Statement statement = DB_CONN.createStatement();
            ResultSet rs = statement.executeQuery(maxCitySQLStatement);
            
            if (rs.next()) {
                maxCityId = rs.getInt(1);
            }
        }
        catch (SQLException e) {
        }
        return maxCityId + 1;
    }

    public static City addCityToDatabase(City city) {
        String addCitySQLStatement = String.join(" ",
                "INSERT INTO city (cityId, city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, ?, NOW(), ?, NOW(), ?)");
        
        int cityId = getMaximumCities();
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(addCitySQLStatement);
            statement.setInt(1, cityId);
            statement.setString(2, city.getCity());
            statement.setInt(3, city.getCountryId());
            statement.setString(4, loggedUser.getUserName());
            statement.setString(5, loggedUser.getUserName());
            statement.executeUpdate();
        }
        catch (SQLException e) {
        }
        return city;
    }
    public static void updateCityInDatabase(City city) {
        String updateCitySQLStatement = String.join(" ",
                "UPDATE city",
                "SET city=?, countryId=?, lastUpdate=NOW(), lastUpdateBy=?",
                "WHERE cityId=?");
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(updateCitySQLStatement);
            statement.setString(1, city.getCity());
            statement.setInt(2, city.getCountryId());
            statement.setString(3, loggedUser.getUserName());
            statement.setInt(4, city.getCityId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
        }
    }
    public void deleteCityFromDatabase(City city) {
        String deleteCitySQLstatement = "DELETE FROM city WHERE cityId = ?";
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(deleteCitySQLstatement);
            statement.setInt(1, city.getCityId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
        }
    }
}
