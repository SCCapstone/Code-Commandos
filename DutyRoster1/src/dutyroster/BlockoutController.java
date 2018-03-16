/**
 * @author Austin Freed, Tanya Peyush
 * @version 5 3/15/18
 */
package dutyroster;


import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;


public class BlockoutController implements Initializable {
 
    
    @FXML private TableView<Blockout> tableView;
    @FXML private TableColumn<Blockout,String> name;
    @FXML private TableColumn<Blockout,LocalDate> fromDate;
    @FXML private TableColumn<Blockout,LocalDate> toDate;
    @FXML private ComboBox nameCombo;
    @FXML private ComboBox statusCombo;
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
  
    //Hold localdates from datepickers
    private LocalDate curFrom, curTo;
    private static final String DATE_FORMAT = "d MMM uuuu";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    
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
             
        loadBlockouts();

        //Scene formatting
        setDatePickers();

        // pull ranks from secure file and place them into rank listing.
        loadStatus(); 
        loadEmployees();
        
        //Create the Delete menu item
        MenuItem mi1 = new MenuItem("Delete");
        mi1.setOnAction((ActionEvent event) -> { 
            ObservableList<Blockout> items = tableView.getSelectionModel().getSelectedItems();
            deleteBlockout(items);
        });
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(mi1);
        tableView.setContextMenu(menu);   
        
        //Load up Combos
        nameCombo.getItems().setAll(employeeOptions);
        statusCombo.getItems().setAll(statusOptions);
        
        tableView.setItems(blockoutList);
 
        //used to edit the tables.
        tableView.setEditable(true);
    
        //set proper sorting.      
        fromDate.setSortType(TableColumn.SortType.ASCENDING);
        tableView.getSortOrder().add(fromDate);
        fromDate.setSortable(true);
        
        toDate.setSortType(TableColumn.SortType.ASCENDING);
        tableView.getSortOrder().add(toDate);
        toDate.setSortable(true);
        
        name.setSortType(TableColumn.SortType.ASCENDING);
        tableView.getSortOrder().add(name);
        name.setSortable(true); 
    } 
    
    
    /**
     * This will add a new block out date to the list
     */
    @FXML public void AddBlockout(){
             
        String sName = nameCombo.getValue().toString();
        String sStatus = statusCombo.getValue().toString();
        String tmpFrom = curFrom.format(formatter);
        String tmpTo = curTo.format(formatter);
        
        blockoutList.add( new Blockout(sName,sStatus,tmpFrom,tmpTo) );
        tableView.sort();
        
        //Clear form controls
        nameCombo.setValue(null);
        statusCombo.setValue(null);
        dateFrom.setValue(null);
        dateTo.setValue(null);
        
        //Clear datepicker fields
        curFrom = null;
        curTo = null;
    }

    /**
     * This will delete selected rows from the block out list
     * @param tmpList
     */
    public void deleteBlockout(ObservableList<Blockout> tmpList){
        
        if (tmpList==null)
            return;
 
        for(int i = 0; i<tmpList.size(); i++)
            for(int j = 0; j<blockoutList.size(); j++)  
                if (tmpList.get(i).getName().equals(blockoutList.get(j).getName()))  
                    blockoutList.remove(j);
    }
       
    
    public void shutDown() {  
        storeData();
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
     * This is used to load block out data from secure files into the link listing array.
     */
    public void loadBlockouts(){
        
        SecureFile scBO = new SecureFile("Blockouts");
        String a = scBO.retrieve();
      
        String aArry[] = a.split("\\|", -1);
        for (String b : aArry){
            
            if (b.length() > 2){
                
                String bArry[] = b.split("\\@", -1);
                
                // getSortIndex pulls updated rank order index. 
                if(bArry[0].length() > 0 && bArry[1].length() > 0){
                    
                    blockoutList.add( new Blockout(
                            bArry[0],
                            bArry[1],
                            bArry[2],
                            bArry[3]
                    ));
                }
            }    
        }           
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
    
    public void setDatePickers(){
        dateFrom.setConverter(new StringConverter<LocalDate>(){
           
            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate==null)
                    return "";
                return formatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString==null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString,formatter);
            }
        });
        
        dateFrom.valueProperty().addListener((ObservableValue<? extends LocalDate> ov, LocalDate oldValue, LocalDate newValue) -> {
            curFrom = newValue;
        });
        dateTo.setConverter(new StringConverter<LocalDate>(){
 
            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate==null)
                    return "";
                return formatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString==null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString,formatter);
            }
        });
        dateTo.valueProperty().addListener((ObservableValue<? extends LocalDate> ov, LocalDate oldValue, LocalDate newValue) -> {
            curTo = newValue;
        }); 
          
    }
     
}
