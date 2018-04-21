/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dutyroster;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Michael Harlow
 */
public class AboutController extends MainController implements Initializable {

    @FXML private Button btnDone;

    /**
     * Initializes the controller class.
     */
    public AboutController(){}    
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {}    
    
    @FXML public void closeScene(){
         Stage stage = (Stage) btnDone.getScene().getWindow();
         stage.close();
        
    }
    
}
