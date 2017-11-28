
package dutyroster;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author othen
 */
public class MainController implements Initializable {
    
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    


  @FXML
    private void exitProgram(ActionEvent event) {   
    System.exit(0);
    }

    @FXML
    private void openNameEditor(ActionEvent event) {
         
        try{
         FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeFXML.fxml")); 
         Parent root1 = loader.load();
         EmployeeController eController = loader.getController();
         Stage stage = new Stage();
         stage.setTitle("Employee Editor");
         stage.setResizable(false);
         stage.setScene(new Scene(root1));
         stage.setOnHidden(e -> eController.shutDown());
         stage.show(); 
          
        }
        catch(Exception e){
           System.out.println("Can't load new scene: " + e); 
        }
    }
    
    
     @FXML
    private void openRankEditor(ActionEvent event) {
         
        try{
         FXMLLoader loader = new FXMLLoader(getClass().getResource("RankFXML.fxml")); 
         Parent root1 = loader.load();
         RankController eController = loader.getController();
         Stage stage = new Stage();
         stage.setTitle("Rank Editor");
         stage.setResizable(false);
         stage.setScene(new Scene(root1));
         stage.setOnHidden(e -> eController.shutDown());
         stage.show(); 
          
        }
        catch(Exception e){
           System.out.println("Can't load new scene: " + e); 
        }
    }  
    

    
    
    
}

