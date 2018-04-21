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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginController implements Initializable {
    
    String pass;
    @FXML private Label labelPass1,labelPass2;
    @FXML private PasswordField fieldPass1, fieldPass2;
    @FXML private TextField fieldText1, fieldText2;
    @FXML private Button buttonPass1, buttonPass2;
    @FXML private CheckBox chkShow;
    private boolean isCreate;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        isCreate = false;    
        retrievepassword(); 
        fieldText1.setVisible(false);
        fieldText2.setVisible(false);

    }    

    @FXML private void showPassword(){
        buttonPass1.setVisible(true);
        fieldPass1.setVisible(true);
        labelPass2.setVisible(true);
        labelPass1.setText("Enter Your Password");
        
        buttonPass2.setVisible(false); 
        fieldPass2.setVisible(false);
        labelPass2.setVisible(false);
          
        fieldText1.setOnKeyPressed((event) -> { 
            if(event.getCode() == KeyCode.ENTER) checkPass(); 
        });
          
        fieldPass1.setOnKeyPressed((event) -> { 
            if(event.getCode() == KeyCode.ENTER) checkPass(); 
        }); 
          
    }
    
    @FXML private void showNewPassword(){
        isCreate = true;
        
        buttonPass1.setVisible(false);
        labelPass1.setVisible(true);
        fieldPass1.setVisible(true);
        fieldPass1.setPromptText("Enter a new Password");
        fieldText1.setPromptText("Enter a new Password");
        labelPass2.setVisible(true);          
        buttonPass2.setVisible(true); 
        fieldPass2.setVisible(true);
        
        fieldPass1.setOnKeyPressed((event) -> { 
            if(event.getCode() == KeyCode.ENTER)         
                fieldPass2.requestFocus();
        });
 
        fieldText1.setOnKeyPressed((event) -> { 
            if(event.getCode() == KeyCode.ENTER)         
                fieldText2.requestFocus();
        });      
        
        fieldPass2.setOnKeyPressed((event) -> { 
            if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB)
                newPassword();
        }); 
        
        fieldText2.setOnKeyPressed((event) -> { 
            if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB)
                newPassword();
        }); 
    }
    
    @FXML private void userLogin(ActionEvent event){
        checkPass();
    }
    
    private void checkPass(){
        
        String passW1 = (chkShow.isSelected())? fieldText1.getText() : fieldPass1.getText();
        
        fieldPass1.clear();
        
        if (!passW1.equals(pass) ) {
            Alert alert = new Alert(AlertType.WARNING, "Wrong password. Please try again.");
            alert.showAndWait();
            fieldPass1.clear();
            fieldText1.clear();
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
        
        SecureFile sc = new SecureFile("Settings");
        
        String passW1 = (chkShow.isSelected())? fieldText1.getText() : fieldPass1.getText();
        String passW2 = (chkShow.isSelected())? fieldText2.getText() : fieldPass2.getText();
        
        if ( passW1.length()<5 || passW1.length()>16){
            Alert alert = new Alert(AlertType.ERROR, "Password is not between 5-16 characters");
            alert.showAndWait();
            fieldPass1.clear();
            fieldPass2.clear();
            fieldText1.clear();
            fieldText2.clear();
            return;
        }
        
        if ( !(passW1.equals(passW2)) ){
            Alert alert = new Alert(AlertType.ERROR, "Passwords didn't match.");
            alert.showAndWait();
            fieldPass1.clear();
            fieldPass2.clear();
            fieldText1.clear();
            fieldText2.clear();
            return;
        }
 
        if ( passW2.equals("@") ){
            Alert alert = new Alert(AlertType.ERROR, "Password cannot contain the @ symbol.");
            alert.showAndWait();
            fieldPass1.clear();
            fieldPass2.clear();
            fieldText1.clear();
            fieldText2.clear();
            return;
        }       
        
        
        sc.store(passW1);
        fieldPass1.clear();
        fieldPass2.clear();
        Stage stage = (Stage) fieldPass2.getScene().getWindow();
        stage.close();
        
       openMainScene();
      
    }
    
    @FXML private void exitProgram(ActionEvent event) {   
        System.exit(0);
    }   
    
    public void retrievepassword(){
        
        SecureFile sc = new SecureFile("Settings");
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
    
    public void togglevisiblePassword(ActionEvent event) {
        
        if (chkShow.isSelected()) {
            
            fieldText1.setText(fieldPass1.getText());
            fieldText1.setVisible(true);
            fieldPass1.setVisible(false);
            
            if(isCreate){
                fieldText2.setText(fieldPass2.getText());
                fieldText2.setVisible(true);
                fieldPass2.setVisible(false);
            }
            
            return;
        }
        
        fieldPass1.setText(fieldText1.getText());
        fieldPass1.setVisible(true);
        fieldText1.setVisible(false);
        
         if(isCreate){
            fieldPass2.setText(fieldText2.getText());
            fieldPass2.setVisible(true);
            fieldText2.setVisible(false);
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
            
             openWizard(); 
        }
        catch(IOException e){
           System.out.println("Can't load new scene: " + e); 
        }   
        
    } 
     public void openWizard(){

         try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("WizardFXML.fxml")); 
            Parent root1 = loader.load();
            WizardFXMLController mController = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Setup");
            stage.setResizable(false);           
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root1));
            stage.setMaximized(false);
            stage.setOnHidden(e -> mController.shutDown());
            stage.show(); 
          
        }
        catch(IOException e){
           System.out.println("Can't load new scene: " + e); 
        }      
    
    }

}