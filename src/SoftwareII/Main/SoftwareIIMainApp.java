
package SoftwareII.Main;

import SoftwareII.DB.ConnDB;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import SoftwareII.ViewsAndControllers.LoginController;
import java.sql.SQLException;
import javafx.stage.StageStyle;


public class SoftwareIIMainApp extends Application {
    
    @Override
    public void start(Stage stage) throws ClassNotFoundException, SQLException, IOException, Exception {
        FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("Login.fxml"));
        
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        stage.setTitle("CRM Login Screen");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException, Exception {
        ConnDB.openConnection();
        launch(args);
        ConnDB.closeConnection();
    }
}
