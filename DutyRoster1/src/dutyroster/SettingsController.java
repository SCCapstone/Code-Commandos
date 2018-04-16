/**
 * FXML Controller class
  * @author ISHU, Othen
 * @version 8, 4/13/2018
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



public class SettingsController implements Initializable {

    @FXML private TabPane tabPane;
    
    private static final int CONTROL_WIDTH = 500;
    private static final String SETTINGS_FILE = "Settings2";
    private final static String[] CATEGORYS = {"Roster", "Memorandum"};
    private final static String[][] SETTING_IDS = {{"fOrg","fTitle","fNote","fRef","fVer"},{"fSymbol", "fHead","fFirst","fConc","fSig"}};
    private final static String[][] SETTINGS = {{"Organization", "Title","Notice","Reference","Version"},
        {"Office Symbol","Header","First Paragraph","Conclusion","Signature Block"}};
    private final static String[] ISTEXTAREA = {"fHead","fFirst","fConc","fSig"};
    

    private final static String[][] SETTING_DEFAULTS = {
        {
        "",
        "DA FORM 6, JUL 1974",
        "PREVIOUS EDITIONS OF THIS FORM ARE OBSOLETE.",
        "For use of this form, see AR 220-45; the proponent agency is DCS, G-1.",
        "APD ALD v1.02"
        },
        {
        "OFFICE-SYMBOL",
        "Department of the Army",
        "1. The following personnel have been assigned duty.",
        "2. For futher information contact our office.",
        "SIGNATURE BLOCK"
        }};
    
    
    
    private ArrayList<Setting> sList;
 
    public SettingsController(){
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
     
        for(int i = 0; i < SETTINGS.length; i++){
            for (int j = 0; j < SETTING_IDS[i].length; j++){
                String setID = SETTING_IDS[i][j];
                sList.add(new Setting(i, setID, SETTING_DEFAULTS[i][j], isTextArea(setID))); 
            }
        }
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
                    
                    textField.setMinWidth(CONTROL_WIDTH);
 
                    TextArea textArea = new TextArea(s.getId());
                    textArea.setId(s.getId());
                    textArea.setText(text);
                    textArea.setMaxSize(CONTROL_WIDTH, 150);
                
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
        
        for(Setting s : sList){
            String tmp = Tools.removeSpecialChars(s.getText());
            strData +=  tmp + "|";          
        
        }
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
      
        for (int i = 0; i < aArry.length; i++){
            String tmp = Tools.replaceSpecialChars(aArry[i]);
            sList.get(i).setText(tmp);             
        }
    }
   
    public String getSetting(String sID){ 
        
        for(Setting s : sList)
            if(s.getId().equals(sID))
                return s.getText(); 


        return "";

    }

}
