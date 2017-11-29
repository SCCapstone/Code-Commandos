/**
 * This class control the login screen. 
 * @author Michael Harlow
 * @version 1 November 22nd, 2017
*/
package dutyroster;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class LoginController implements Initializable {
    
    private SecureFile sc = new SecureFile("Settings");
    String pass;
    @FXML private Label labelPass1,labelPass2;
    @FXML private TextField fieldPass1, fieldPass2;
    @FXML private Button buttonPass1, buttonPass2;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {     
        retrievepassword(); 
    }    

    @FXML
    private void showPassword(){
       buttonPass1.setVisible(true);
       buttonPass2.setVisible(false); 
       fieldPass1.setVisible(true);
       fieldPass2.setVisible(false);
       labelPass1.setVisible(false);
       labelPass2.setVisible(false);     
    }
    
    @FXML
    private void showNewPassword(){
       buttonPass1.setVisible(false);
       buttonPass2.setVisible(true); 
       fieldPass1.setVisible(true);
       fieldPass1.setPromptText("Enter a new Password");
       fieldPass2.setVisible(true);
       labelPass1.setVisible(true);
       labelPass2.setVisible(true);  
    }

    
    @FXML
    private void userLogin(ActionEvent event){
        
        String testPass = fieldPass1.getText();
       
        fieldPass1.clear();
        
        if (!testPass.equals(pass) ) {
            Alert alert = new Alert(AlertType.WARNING, "Wrong password. Please try again.");
            alert.showAndWait();
            return;
        }
        
        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        
         openMainScene();   
    }
    
    
    @FXML
    private void createPassword(ActionEvent event){
        
        if ( !(fieldPass1.getText().equals(fieldPass2.getText())) ){
            Alert alert = new Alert(AlertType.ERROR, "Passords didn't match."
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

        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        
        openMainScene();
    }
    
    
    @FXML
    private void exitProgram(ActionEvent event) {   
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
            //EmployeeController eController = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Duty Roster 1.0");
            stage.setResizable(true);
            stage.setScene(new Scene(root1));
            stage.setMaximized(true);
            stage.show(); 
          
        }
        catch(Exception e){
           System.out.println("Can't load new scene: " + e); 
        }   
        
    }      
    
}



