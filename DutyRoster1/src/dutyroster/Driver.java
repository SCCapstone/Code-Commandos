/**
 * @author Othen W. Prock, Michael Harlow
 * Version 6 November 28th, 2017
 * Last altered by Michael Harlow to add login
 */
package dutyroster;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Driver extends Application {
    
    @Override
    public void start(Stage stage) {
        
       openLogin();
        
    }

    
    private void openLogin() {
             
        try { 
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginFXML.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            
             Scene sceneLogin = new Scene(root);
            
            stage.setScene(sceneLogin);
       
            stage.setTitle("Login");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
           
            stage.show();  
        } 
        catch (Exception e) {
            Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }

        
}


