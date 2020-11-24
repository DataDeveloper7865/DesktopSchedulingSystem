
package SoftwareII.DB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnDB {
    private static final String DB_NAME = "U05iYT";
    private static final String DB_URL = "jdbc:mysql://3.227.166.251/"+DB_NAME;
    private static final String USERNAME = "U05iYT";
    private static final String PASSWORD = "53688516272";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
            
    static Connection DB_CONN;
    
    public static void openConnection() throws ClassNotFoundException, SQLException, Exception
    {
        Class.forName(DRIVER);
        DB_CONN = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        System.out.println("Database Connection Opened");
    }
    public static void closeConnection() throws ClassNotFoundException, SQLException, Exception
    {
        DB_CONN.close();
        System.out.println("Database Connection Closed");
    }
}
