/**
 * 
 * @author Austin Freed, Tanya Peyush
 * @version 5 12/5/17
 */
package dutyroster;


import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;

import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;


public class BlockoutController implements Initializable {
 
    //for entering a new rank
    //@FXML private ComboBox statusCombo;
   //
    //used for tableview 
    @FXML private TableView<Blockout> tableView;
    @FXML private TableColumn<Blockout,String> status;
    @FXML private TableColumn<Blockout,String> name;
    @FXML private TableColumn<Blockout,LocalDate> fromDate;
    @FXML private TableColumn<Blockout,LocalDate> toDate;
    @FXML private ComboBox nameCombo;
    @FXML private ComboBox statusCombo;
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
    
    // used to import and export data from employee data
    private ObservableList<String> employeeOptions;
     private ObservableList<String> statusOptions;
    // rankOptions is used to pull the rank information  
 
    //rankListing is used to change it in the column  
    private ObservableList<Blockout> blockoutList;
   
    // used to encrypt, decrypt and store the file.
    private String strData;
    
    /**
     * This is used to before the GUI interface is initialize.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
        employeeOptions = FXCollections.observableArrayList();
        statusOptions = FXCollections.observableArrayList();
        blockoutList = FXCollections.observableArrayList();
             
        dateFrom = new DatePicker();
        dateTo = new DatePicker();
        
        // pull ranks from secure file and place them into rank listing.
        loadStatus(); 
        loadEmployees();
        
                nameCombo.getItems().setAll(employeeOptions);
                statusCombo.getItems().setAll(statusOptions);
        
        tableView.setItems(blockoutList);
 
        //used to edit the tables.
        tableView.setEditable(true);
    
        //set proper sorting. 
        name.setSortType(TableColumn.SortType.ASCENDING);
        tableView.getSortOrder().add(name);
        name.setSortable(true);
        
        
    }  
    
    @FXML public void AddBlockout(){
        
        String n;
        String s;
        LocalDate f;
        LocalDate t;
                
        n = nameCombo.getValue().toString();
        s = statusCombo.getValue().toString();

        f = dateFrom.getValue();
        t = dateTo.getValue();

        Blockout temp = new Blockout(n,s,f,t);
        
        blockoutList.add(temp);
        tableView.sort();
    }
                
    
    
    public void shutDown() {  
        //storeData();
    }
 
    public void storeData(){  
        SecureFile scBlockOut = new SecureFile("Blockouts");
        strData = "";
        
        if (blockoutList == null)
            return;
        
        blockoutList.forEach((b) -> { 
            

            strData += b.getName() 
                    + "@" + b.getStatus()
                    + "@" + b.getName() 
                    + "@" + b.getFromDate()
                    + "@" + b.getToDate()
                    + "|";    
        });
            strData = Tools.removeLastChar(strData);
            
        scBlockOut.store(strData);
        
        strData = "";
        
    }
    
    /**
     * This is used to load employees from secure files into the link listing array.
     */
    public void loadEmployees(){
        SecureFile scEmployees = new SecureFile("Employees");
        String a = scEmployees.retrieve();
      
        String aArry[] = a.split("\\|", -1);
        for (String b : aArry){
            
            if (b.length() > 2){
                
                String bArry[] = b.split("\\@", -1);
                
                // getSortIndex pulls updated rank order index. 
                if(bArry[0].length() > 0 && bArry[1].length() > 0){
                    employeeOptions.add(bArry[1]);
                }
            }    
        
        }       
        
    }
    
    /**
     * 
     * This is used to load ranks from secure files into the link listing array.
     */
    public void loadStatus(){
        SecureFile sc = new SecureFile("Status");
        String a = sc.retrieve();
      
        String aArry[] = a.split("\\|", -1);
        for (String b : aArry){
            
            if (b.length() > 2){
                
                String bArry[] = b.split("\\@", -1);
                
                if(bArry[0].length() > 0 && bArry[1].length() > 0)
                    statusOptions.add( bArry[1] );
            }
            
        }

    }
     
}
