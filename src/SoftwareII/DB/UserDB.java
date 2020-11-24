
package SoftwareII.DB;

import SoftwareII.Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class UserDB {
    
    private final static Connection DB_CONN = ConnDB.DB_CONN;
    
    public UserDB () {
    }
    
    public static ObservableList<User> getCurrentActiveUsers() {
        ObservableList<User> currentActiveUsers = FXCollections.observableArrayList();
        String getActiveUsersSQLQuery = "SELECT * FROM user WHERE active = 1";
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(getActiveUsersSQLQuery);
            ResultSet resultset = statement.executeQuery();
            
            while (resultset.next()) {
                User curActiveUser = new User();
                curActiveUser.setUserId(resultset.getInt("userId"));
                curActiveUser.setUserName(resultset.getString("userName"));
                curActiveUser.setPassword(resultset.getString("password"));
                curActiveUser.setActive(resultset.getBoolean("active"));
                
                currentActiveUsers.add(curActiveUser);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return currentActiveUsers;
    }
    
    public static User getUserById(int userId) {
        String getUserByIdSQLStatement = "SELECT * FROM user WHERE userId=?";
        User userToBeReturned = new User();
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(getUserByIdSQLStatement);
            statement.setInt(1, userId);
            ResultSet resultset = statement.executeQuery();
            
            if (resultset.next()) {
                userToBeReturned.setUserId(resultset.getInt("userId"));
                userToBeReturned.setUserName(resultset.getString("userName"));
            }
        }
        catch (SQLException e) {
        }
        return userToBeReturned;
    }
}
