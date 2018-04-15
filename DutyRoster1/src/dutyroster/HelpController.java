/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Michael Harlow
 */
public class HelpController extends MainController implements Initializable {

    @FXML private javafx.scene.control.Button exitButton;
    @FXML private Button gradeButton, employeeButton, statusButton, blockButton, holidayButton, settingsButton;
    
    
    /**
     * Initializes the controller class.
     */
    public HelpController(){
        //startUp();
    }    
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //buildForm();
    }    
    
    @FXML public void exitWindow(ActionEvent event){
        //final Node source = (Node) e.getSource();
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML public void gradeEditor(ActionEvent event){
     
        //DutyRoster.MainControlller().
        
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RankFXML.fxml")); 
            Parent root1 = loader.load();
            RankController eController = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Grade Editor");
            stage.setResizable(false);
            Scene sceneRank = new Scene(root1);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setScene(sceneRank);
            stage.setOnHidden(e -> eController.shutDown());
            stage.show(); 
          
        }
        catch(IOException e){
           System.out.println("Can't load new scene: " + e); 
        }
        
    }
        
    @FXML public void employeeEditor(ActionEvent event){
        
        try{
         FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeFXML.fxml")); 
         Parent root1 = loader.load();
         EmployeeController eController = loader.getController();
         Stage stage = new Stage();
         stage.setTitle("Employee Editor");
         stage.setResizable(false);
         stage.initModality(Modality.APPLICATION_MODAL);
         stage.setScene(new Scene(root1));
         stage.setOnHidden(e -> eController.shutDown());
         stage.show(); 
          
        }
        catch(IOException e){
           System.out.println("Can't load new scene: " + e); 
        }
        
    }
    
    @FXML public void statusEditor(ActionEvent event){
        
        try{
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StatusFXML.fxml")); 
            Parent root1 = loader.load();
            StatusController eController = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Status Editor");
            stage.setResizable(false);
            Scene sceneStatus2 = new Scene(root1);
           
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneStatus2);
            stage.setOnHidden(e -> eController.shutDown());
            stage.show(); 
          
        }
        catch(IOException e){
           System.out.println("Can't load new scene: " + e); 
        }
        
    }

    @FXML public void blockoutEditor(ActionEvent event){
        
        try{
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BlockoutFXML.fxml")); 
            Parent root1 = loader.load();
            BlockoutController BOController = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Blockout Editor");
            stage.setResizable(false);
            Scene sceneStatus2 = new Scene(root1);
            
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneStatus2);
            stage.setOnHidden(e -> {
                BOController.shutDown();
                //loadBlockouts();
                //updateCrew();
                    });
            stage.show(); 
        }
        catch(IOException e){
           System.out.println("Can't load new scene: " + e); 
        }
        
    }
    
    @FXML public void holidayEditor(ActionEvent event){
        
        try{
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HolidayFXML.fxml")); 
            Parent root1 = loader.load();
            HolidayController HController = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Holidays Editor");
            stage.setResizable(false);
            Scene sceneStatus2 = new Scene(root1);
           
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneStatus2);
            stage.setOnHidden(e -> {
                HController.shutDown();
                //loadHolidays();
                //setDate(curYear, curMonth-1);
                }
            );
            stage.show(); 
          
        }
        catch(IOException e){
           System.out.println("Can't load new scene: " + e); 
        }
                
    }
    
    @FXML public void settingsEditor(ActionEvent event){
        
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingsFXML.fxml")); 
            Parent root1 = loader.load();
           // CrewController eController = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Settings Editor");
            stage.setResizable(false);
            Scene scene = new Scene(root1);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.show(); 
        }
        catch(IOException e){
           System.out.println("Can't load new scene: " + e); 
        }
        
    }
}
