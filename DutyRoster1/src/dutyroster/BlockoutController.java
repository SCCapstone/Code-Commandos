/**
 * @author Austin Freed, Tanya Peyush
 * @version 6 3/16/18
 */
package dutyroster;


import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;


public class BlockoutController implements Initializable {
 
    
    @FXML private TableView<Blockout> tableView;
    @FXML private TableColumn<Blockout,String> name;
    @FXML private TableColumn<Blockout,LocalDate> fromDate;
    @FXML private TableColumn<Blockout,LocalDate> toDate;
    @FXML private ComboBox nameCombo;
    @FXML private ComboBox statusCombo;
    @FXML private TextField fieldReason;
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
    @FXML private Button buAdd,buUpdate,buCancel,btnDone;
  
    //Hold localdates from datepickers
    private LocalDate curFrom, curTo;
    private static final String DATE_FORMAT = "d MMM uuuu";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private Blockout oldBlock;
            
    // used to import and export data from employee data
    private ObservableList<String> employeeOptions;
    private ObservableList<String> statusOptions;
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
        Collections.sort(blockoutList); 
       
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
        
        //Hide Update Cancel Buttons
        buUpdate.setVisible(false);
        buCancel.setVisible(false);
        
        tableView.setItems(blockoutList);
 
        //used to edit the tables.
        tableView.setEditable(true);
    
        //Set row double-click for editing
        tableView.setRowFactory( tv -> {
            TableRow<Blockout> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Blockout rowData = row.getItem();
                    setEdit(rowData);
                }
            });
            return row;
        });
  
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
         
        Alert alert;     
        
        if (nameCombo.getValue()==null){
             alert = new Alert(Alert.AlertType.ERROR, "Please select a employee.");
             alert.setTitle("No Employee selected");
             alert.showAndWait();
            return;
        }
        
        if (statusCombo.getValue()==null){
             alert = new Alert(Alert.AlertType.ERROR, "Please selecte a status");
             alert.setTitle("No Status selected");
             alert.showAndWait();
            return;
        }
        
        if (curFrom==null){
            alert = new Alert(Alert.AlertType.ERROR, "Missing \"from\" date");
            alert.setTitle("Incorrect Date");
            alert.showAndWait();
            return;
        }
        
         if (curTo==null){
            alert = new Alert(Alert.AlertType.ERROR, "Missing \"to\" date");
            alert.setTitle("Incorrect Date");
            alert.showAndWait();
            return;
        }
               
        String sName = nameCombo.getValue().toString();
        String sStatus = statusCombo.getValue().toString();
        String sReason = fieldReason.getText();
        String tmpFrom = curFrom.format(formatter);
        String tmpTo = curTo.format(formatter); 
        
        if (curFrom.isAfter(curTo)){
            
            alert = new Alert(Alert.AlertType.ERROR, "The \"From\" date must be a date that is the same as or earlier than the \"To\" date.");
            alert.setTitle("Incorrect Date");
            alert.showAndWait();
            return;
        }


        blockoutList.add( new Blockout(sName,sStatus,sReason,tmpFrom,tmpTo) );
        
        Collections.sort(blockoutList); 
        tableView.setItems(null);
        tableView.setItems(blockoutList);
        
        //Clear form controls
        nameCombo.setValue(null);
        statusCombo.setValue(null);
        fieldReason.setText(null);
        dateFrom.setValue(null);
        dateTo.setValue(null);
        
        //Clear datepicker fields
        curFrom = null;
        curTo = null;
    }
  
    private void setEdit(Blockout block){
         
        oldBlock = block;
        nameCombo.setValue(block.getName());
        statusCombo.setValue(block.getStatus());
        fieldReason.setText(block.getReason());
        dateFrom.setValue(LocalDate.parse(block.getFromDate(), formatter));
        dateTo.setValue(LocalDate.parse(block.getToDate(), formatter));
        buUpdate.setVisible(true);
        buCancel.setVisible(true);
        buAdd.setVisible(false);
    }
    
    
    @FXML private void updateBlockout(){
        
        
        Alert alert;     
        
        if (nameCombo.getValue()==null){
             alert = new Alert(Alert.AlertType.ERROR, "Please select a employee.");
             alert.setTitle("No Employee selected");
             alert.showAndWait();
            return;
        }
        
        if (statusCombo.getValue()==null){
             alert = new Alert(Alert.AlertType.ERROR, "Please selecte a status");
             alert.setTitle("No Status selected");
             alert.showAndWait();
            return;
        }
        
        if (curFrom==null){
            alert = new Alert(Alert.AlertType.ERROR, "Missing \"from\" date");
            alert.setTitle("Incorrect Date");
            alert.showAndWait();
            return;
        }
        
         if (curTo==null){
            alert = new Alert(Alert.AlertType.ERROR, "Missing \"to\" date");
            alert.setTitle("Incorrect Date");
            alert.showAndWait();
            return;
        }
               
        String sName = nameCombo.getValue().toString();
        String sStatus = statusCombo.getValue().toString();
        String sReason = fieldReason.getText();
        String tmpFrom = curFrom.format(formatter);
        String tmpTo = curTo.format(formatter); 
        
        if (curFrom.isAfter(curTo)){
            
            alert = new Alert(Alert.AlertType.ERROR, "The \"From\" date must be a date that is the same as or earlier than the \"To\" date.");
            alert.setTitle("Incorrect Date");
            alert.showAndWait();
            return;
        }     
        
        blockoutList.forEach((b) -> {
          
            if(oldBlock.equals(b)){
                b.setName(sName);
                b.setStatus(sStatus);
                b.setReason(sReason);
                b.setFromDate(tmpFrom);
                b.setToDate(tmpTo);           
                setCancel();
             
                Collections.sort(blockoutList); 
                tableView.setItems(null);
                tableView.setItems(blockoutList);
                tableView.sort();
                tableView.refresh();
            }
            
        });
              
    }
  
    @FXML private void setCancel(){
         
        //Clear form controls
        nameCombo.setValue(null);
        statusCombo.setValue(null);
        fieldReason.setText(null);
        dateFrom.setValue(null);
        dateTo.setValue(null);
        buUpdate.setVisible(false);
        buCancel.setVisible(false);
        buAdd.setVisible(true);
        
        tableView.sort();
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
                if ( tmpList.get(i).equals(blockoutList.get(j)) )  
                    blockoutList.remove(j);
    }
       
    
    public void shutDown() {  
        storeData();
    }
    
    @FXML public void closeScene(){
         Stage stage = (Stage) btnDone.getScene().getWindow();
         stage.close();
        
    } 
    
    public void storeData(){  
        
        SecureFile scBlockOut = new SecureFile("Blockouts");
        strData = "";
        
        if (blockoutList == null)
            return;
        
        blockoutList.forEach((b) -> { 
            String eName = Tools.removeSpecialChars(b.getName());
            String eStatus = Tools.removeSpecialChars(b.getStatus());
            String eReason = (!b.getReason().isEmpty()) ? Tools.removeSpecialChars(b.getReason()) : "";
            strData += eName 
                    + "@" + eStatus
                    + "@" + eReason
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
                    String eName = Tools.replaceSpecialChars(bArry[0]);
                    String eStatus = Tools.replaceSpecialChars(bArry[1]);
                    String eReason = (!bArry[2].isEmpty()) ? Tools.replaceSpecialChars(bArry[2]) : "";
                    blockoutList.add( new Blockout(
                            eName,
                            eStatus,
                            eReason,
                            bArry[3],
                            bArry[4]
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
                     String eName = Tools.replaceSpecialChars(bArry[1]);
                    employeeOptions.add(eName);
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
                
                if(bArry[0].length() > 0 && bArry[1].length() > 0){
                    String sTitle = Tools.replaceSpecialChars(bArry[1]);
                    statusOptions.add( sTitle );
                }
            
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
