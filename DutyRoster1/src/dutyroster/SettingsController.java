/**
 * FXML Controller class
 *
 * @author ISHU
 */

package dutyroster;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class SettingsController implements Initializable {

  /*
    @FXML private final TextField fOrg = new TextField(); 
    @FXML private final TextField fAddress = new TextField();
    @FXML private final TextField fPhone = new TextField(); 
    @FXML private final TextField fWebsite = new TextField();
    @FXML private final TextField fTitle = new TextField();
    @FXML private final TextField fReference = new TextField();
    @FXML private final TextField fVers = new TextField();
    @FXML private final TextField fNotice = new TextField();
    @FXML private final TextArea fPara = new TextArea();
    @FXML private final TextArea fConc = new TextArea();
    @FXML private final TextArea fSign = new TextArea(); 
*/
    
   private ArrayList<Setting> settingsList;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       settingsList = new ArrayList();
       // retrieveData();
    } 
    
   /* 
   @FXML public void updateSave(){
     
        settingsList.get(0).setTitle("Org"); settingsList.get(0).setText( fOrg.getText() );
        settingsList.get(1).setTitle("Address"); settingsList.get(1).setText( fAddress.getText() );
        settingsList.get(2).setTitle("Phone"); settingsList.get(2).setText( fPhone.getText() );
        settingsList.get(3).setTitle("Website"); settingsList.get(3).setText( fWebsite.getText() );
        settingsList.get(4).setTitle("Title"); settingsList.get(4).setText( fTitle.getText() );
        settingsList.get(5).setTitle("Reference"); settingsList.get(5).setText( fReference.getText() );
        settingsList.get(6).setTitle("Vers"); settingsList.get(6).setText( fVers.getText() );
        settingsList.get(7).setTitle("Notice"); settingsList.get(7).setText( fNotice.getText() );
        settingsList.get(8).setTitle("Para"); settingsList.get(8).setText( fPara.getText() );
        settingsList.get(9).setTitle("Conc"); settingsList.get(9).setText( fConc.getText() );
        settingsList.get(10).setTitle("Sign"); settingsList.get(10).setText( fSign.getText() );
    
        storeData();
    }
   
   
    public void loadFields(){
     
        
        if( settingsList.isEmpty() )
            return;
        
        fOrg.setText( settingsList.get(0).getText() );
        fAddress.setText( settingsList.get(1).getText() );
        fPhone.setText( settingsList.get(2).getText() );
        fWebsite.setText( settingsList.get(3).getText() );
        fTitle.setText( settingsList.get(4).getText() );
        fReference.setText( settingsList.get(5).getText() );
        fVers.setText( settingsList.get(6).getText() );
        fNotice.setText( settingsList.get(7).getText() );
        fPara.setText( settingsList.get(8).getText() );
        fConc.setText( settingsList.get(9).getText() );
        fSign.setText( settingsList.get(10).getText() );

   
   }   
   
    
    //Converting store data into an array string
    public void storeData(){
        
        SecureFile sc = new SecureFile("Settings2");
        String strData = "";
        
        if (settingsList == null || settingsList.isEmpty())
            return;
       
        for(Setting s: settingsList)  
            strData +=  s.getTitle() + "@" +  s.getText() + "|"; 
        
        strData = Tools.removeLastChar(strData);
        
        //Store string array into secure file
        sc.store(strData);

    }   
 */    
    public void retrieveData(){
        SecureFile sc = new SecureFile("Settings2");
        String a = sc.retrieve();
      
        String aArry[] = a.split("\\|", -1);
        for (String b : aArry){
            
            if (b.length() > 2){
                
                String bArry[] = b.split("\\@", -1);
                
                if(bArry[0].length() > 0 && bArry[1].length() > 0){
                    settingsList.add(  new Setting(bArry[0], bArry[1])  );  
                }
            }
        }
    }

}
