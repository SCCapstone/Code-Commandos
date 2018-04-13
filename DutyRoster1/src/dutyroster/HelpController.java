/**
 * FXML Controller class
  * @author Othen, Austin, Tanya, Harini, Michael
 * @version 1, 4/13/2018
 */

package dutyroster;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;



public class HelpController implements Initializable {

    @FXML private TabPane tabPane;
    
    private static final String SETTINGS_FILE = "Help";
    private final static String[] CATEGORYS = {"About","Rosters", "Employees","Blackouts","Holidays","Settings"};
    private final static String[][] SETTING_IDS = {{"fOrg","fAdd","fPhone","fWeb"},
        {"fTitle","fRef","fVer","fNote"},
        {"fFirst","fConc","fSig"}};
    private final static String[][] SETTINGS = {{"Organization","Address","Phone","Website"},
        {"Title","Reference","Version","Notice"},
        {"First Paragraph","Conclusion","Signature Block"}};
    private final static String[] ISTEXTAREA = {"fFirst","fConc", "fSig"};
    private ArrayList<Setting> sList;

    
    public HelpController(){
        startUp();
    }    
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buildForm();
    } 

    public void startUp(){
        sList = new ArrayList<>();
        setupArray();
        retrieveData();
    }   
       
    @FXML public void updateSave(){
          
        for(Setting s: sList){
                        
            if(s.isTextArea()){
                
                TextArea textA = (TextArea) tabPane.lookup("#" + s.getId());
                
                if( textA!=null && !(textA.getText().equals( s.getText() ) ) )
                        s.setText(textA.getText());   

            }
            else{
                
                TextField textF = (TextField) tabPane.lookup("#" + s.getId());
            
                if( textF!=null && !(textF.getText().equals( s.getText() ) ) )
                    s.setText(textF.getText()); 

            }
        }
        
        storeData();
    }
   
    public void setupArray(){
     
        for(int i = 0; i < SETTINGS.length; i++)
            for (String item : SETTING_IDS[i]) 
                sList.add(new Setting(i, item, "", isTextArea(item)));

    }
    
    private void buildForm(){
      
        for(int i = 0; i < SETTINGS.length; i++)
            tabPane.getTabs().add( newTab(i) ); 
        
    }
    
    private Tab newTab(int tabIndex){
      
        //Create Tabs
        Tab tab = new Tab();
        tab.setText(CATEGORYS[tabIndex]);
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(5);
          
        int i = 0;
        for(Setting s: sList){
            if(s.getCat()==tabIndex){
                HBox hbox = new HBox();
                hbox.setPadding(new Insets(10, 10, 10, 10));
                hbox.setSpacing(5);
                hbox.setAlignment(Pos.TOP_RIGHT);
                String text = s.getText();
                
                TextField textField = new TextField();
                    textField.setId(s.getId());
                    textField.setText(text);
                    textField.setMinWidth(300);
 
                    TextArea textArea = new TextArea(s.getId());
                    textArea.setId(s.getId());
                    textArea.setText(text);
                    textArea.setMaxSize(300, 150);
                
                    hbox.getChildren().addAll(new Label(SETTINGS[tabIndex][i++]), (s.isTextArea())? textArea : textField);
                vbox.getChildren().add(hbox); 
            }
        }
     
        tab.setContent(vbox);
        return tab;
    }
   
    private boolean isTextArea(String id){
        
        for (String b : ISTEXTAREA) {
            if (id.equals(b)) {
                return true; 
            }
        }
     return false;   
    }
    
    //Converting store data into an array string
    public void storeData(){

        SecureFile sc = new SecureFile(SETTINGS_FILE);
        
        String strData = "";
        
        for(Setting s : sList)
            strData +=  s.getText() + "|";          
        
        strData = Tools.removeLastChar(strData);
        
        sc.store(strData);

    }
    
    //retrieve data from secure file
    public void retrieveData(){
        
        SecureFile sc = new SecureFile(SETTINGS_FILE);
        String a = sc.retrieve();
        String aArry[] = a.split("\\|", -1);
        
        if(aArry.length < 1)
            return;
      
        for (int i = 0; i < aArry.length; i++)
            sList.get(i).setText(aArry[i]);             

    }
   
    public String getSetting(String sID){ 
        
        for(Setting s : sList){
            
            if(s.getId().equals(sID)){
              
                if(s.getText().isEmpty()){
                    
                    //Set defaults for "fTitle","fRef","fVer","fNote"
                    switch (sID) {
                        case "fTitle":  return "DA FORM 6, JUL 1974";
                        case "fRef":  return "For use of this form, see AR 220-45; the proponent agency is DCS, G-1.";
                        case "fVer":  return "APD ALD v1.02";
                        case "fNote":  return "PREVIOUS EDITIONS OF THIS FORM ARE OBSOLETE.";
                        default: return "";
                    }
                
                } 
                else{
                    return s.getText(); 
                }
                    
            }

        }

        return "";

    }

}
