/**
 * This class control the login screen. 
 * @author Michael Harlow
 * @version 2 February 22th, 2018
*/
package dutyroster;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class LoginController implements Initializable {
    
    private final SecureFile sc = new SecureFile("Settings");
    String pass;
    @FXML private Label labelPass1,labelPass2;
    @FXML private PasswordField fieldPass1, fieldPass2;
    @FXML private Button buttonPass1, buttonPass2;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {     
        retrievepassword(); 
    }    

    @FXML private void showPassword(){
        buttonPass1.setVisible(true);
        buttonPass2.setVisible(false); 
        fieldPass1.setVisible(true);
        fieldPass2.setVisible(false);
        labelPass1.setVisible(false);
        labelPass2.setVisible(false); 
      
        fieldPass1.setOnKeyPressed((event) -> { 
            if(event.getCode() == KeyCode.ENTER) checkPass(); 
        }); 
          
    }
    
    @FXML private void showNewPassword(){
        buttonPass1.setVisible(false);
        buttonPass2.setVisible(true); 
        fieldPass1.setVisible(true);
        fieldPass1.setPromptText("Enter a new Password");
        fieldPass2.setVisible(true);
        labelPass1.setVisible(true);
        labelPass2.setVisible(true);  
        fieldPass1.setOnKeyPressed((event) -> { 
            if(event.getCode() == KeyCode.ENTER)         
                fieldPass2.requestFocus();
        });
        
        fieldPass2.setOnKeyPressed((event) -> { 
            if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB)
                newPassword();
        }); 
    }
    
    @FXML private void userLogin(ActionEvent event){
        checkPass();
    }
    
    private void checkPass(){
        String testPass = fieldPass1.getText();
        fieldPass1.clear();
        
        if (!testPass.equals(pass) ) {
            Alert alert = new Alert(AlertType.WARNING, "Wrong password. Please try again.");
            alert.showAndWait();
        }
        else{
            Stage stage = (Stage) fieldPass1.getScene().getWindow();
            stage.close();

            openMainScene();
        }
    }
    
    @FXML private void createPassword(ActionEvent event){
        newPassword();
    }
  
    private void newPassword(){
        
        if ( fieldPass1.getText().length()<5 || fieldPass1.getText().length()>16){
            Alert alert = new Alert(AlertType.ERROR, "Password is not between 5-16 characters");
            alert.showAndWait();
            fieldPass1.clear();
            fieldPass2.clear();
            return;
        }
        
        if ( !(fieldPass1.getText().equals(fieldPass2.getText())) ){
            Alert alert = new Alert(AlertType.ERROR, "Passwords didn't match."
            + fieldPass1.getText() + " != " + fieldPass2.getText());
            alert.showAndWait();
            fieldPass1.clear();
            fieldPass2.clear();
            return;
        }
        
        sc.store(fieldPass1.getText());
        fieldPass1.clear();
        fieldPass2.clear();
        
        Alert alert = new Alert(AlertType.INFORMATION, "Password Saved");
        alert.showAndWait();

        Stage stage = (Stage) fieldPass2.getScene().getWindow();
        stage.close();
        
        openMainScene();
    }
    
    @FXML private void exitProgram(ActionEvent event) {   
        System.exit(0);
    }   
    
    public void retrievepassword(){
        
        String a = sc.retrieve();
        String aArry[] = a.split("\\|", -1);
        
        if(aArry[0].length() > 0){
            showPassword();
            pass = aArry[0];
        }
        else{
            showNewPassword();
        }

    }
      
    public void openMainScene(){

         try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainFXML.fxml")); 
            Parent root1 = loader.load();
            MainController mController = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Duty Roster 1.0");
            stage.setResizable(true);
            stage.setScene(new Scene(root1));
            stage.setMaximized(true);
            stage.setOnHidden(e -> mController.shutDown());
            stage.show(); 
          
        }
        catch(IOException e){
           System.out.println("Can't load new scene: " + e); 
        }   
        
    }      
    
}



