/**
 * This class will allow user to setup holiday dates for employees, which will 
 * excuse them from the duty assignment for that period of time.
 * @author Tanya Peyush
 * @assisted by Austin Freed
 * @version 3 3/16/2018
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class HolidayController implements Initializable {
    
    @FXML private TableView<Holiday> tableView;
    @FXML private TableColumn<Holiday,String> fromDate;
    @FXML private TableColumn<Holiday,String> toDate;
    @FXML private TextField dateName;
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
    @FXML private Button buAdd, buUpdate, buCancel, btnDone;
    
    private Holiday oldHoliday;
    
    //Hold localdates from datepickers
    private LocalDate curFrom, curTo;
    private static final String DATE_FORMAT = "d MMM uuuu";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    
    //Used to import and export data from employee data
    private ObservableList<String> employeeOptions;
    private ObservableList<String> statusOptions;
    // rankOptions is used to pull the rank information  
 
    //rankListing is used to change it in the column  
    private ObservableList<Holiday> HolidayList;
   
    //Used to encrypt, decrypt and store the file.
    private String strData;
    
    /**
     * This is used to before the GUI interface is initialized.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
        employeeOptions = FXCollections.observableArrayList();
        HolidayList = FXCollections.observableArrayList();
             
        loadHolidays();
        
        //Scene formatting
        setDatePickers();

        //This completely attaches the Holiday List to the table. 
        tableView.setItems(HolidayList);

        //used to edit the tables.
        tableView.setEditable(true);
    
        //set proper sorting for fromDate. 
        fromDate.setSortType(TableColumn.SortType.ASCENDING);
        tableView.getSortOrder().add(fromDate);
        fromDate.setSortable(true);
        
        //set proper sorting for toDate.
        toDate.setSortType(TableColumn.SortType.ASCENDING);
        tableView.getSortOrder().add(toDate);
        toDate.setSortable(true);
        
        //Hide Update Cancel Buttons
        buUpdate.setVisible(false);
        buCancel.setVisible(false);        

        //Set row double-click for editing
        tableView.setRowFactory( tv -> {
            TableRow<Holiday> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Holiday rowData = row.getItem();
                    setEdit(rowData);
                }
            });
            return row;
        });       


        //Create the Delete menu item
        MenuItem mi1 = new MenuItem("Delete");
            mi1.setOnAction((ActionEvent event) -> { 
                ObservableList<Holiday> items = tableView.getSelectionModel().getSelectedItems();
                deleteHoliday(items);
            });

        ContextMenu menu = new ContextMenu();
        menu.getItems().add(mi1);
        tableView.setContextMenu(menu);
    }
    
    /**
     * This will add a new block out date to the list
     */
    @FXML public void addHolidays(){
        
         Alert alert;     
        
        if (dateName.getText()==null ||  dateName.getText().isEmpty() ) {
             alert = new Alert(Alert.AlertType.ERROR, "Please enter a new title.");
             alert.setTitle("Missing Holiday Title");
             alert.showAndWait();
            return;
        }
        
        String tmpName = dateName.getText();
        
        
        if (curFrom==null){
            alert = new Alert(Alert.AlertType.ERROR, "Missing \"from\" date");
            alert.setTitle("Incorrect Date");
            alert.showAndWait();
            return;
        }
        
        String tmpFrom = curFrom.format(formatter);
        
         if (curTo==null){
            alert = new Alert(Alert.AlertType.ERROR, "Missing \"to\" date");
            alert.setTitle("Incorrect Date");
            alert.showAndWait();
            return;
        }
         
        String tmpTo = curTo.format(formatter); 

        if (curFrom.isAfter(curTo)){

            alert = new Alert(Alert.AlertType.ERROR, "The \"From\" date must be a date that is the same as or earlier than the \"To\" date.");
            alert.setTitle("Incorrect Date");
            alert.showAndWait();
            return;
        }
        
        HolidayList.add(new Holiday(tmpName, tmpFrom, tmpTo));
        tableView.sort();
        
        //Clear form controls
        dateName.clear();
        dateFrom.setValue(null);
        dateTo.setValue(null);

        //Clear datepicker fields
        curFrom = null;
        curTo = null;
    }
    @FXML public void closeScene(){
         Stage stage = (Stage) btnDone.getScene().getWindow();
         stage.close();
        
    }
    private void setEdit(Holiday holiday){
         
        oldHoliday = holiday;
        
        dateName.setText( holiday.getName() );
        dateFrom.setValue(LocalDate.parse(holiday.getFromDate(), formatter));
        dateTo.setValue(LocalDate.parse(holiday.getToDate(), formatter));
        buUpdate.setVisible(true);
        buCancel.setVisible(true);
        buAdd.setVisible(false);
    }  
   
      @FXML private void updateBlockout(){
        
        Alert alert; 
        
        if (dateName.getText().isEmpty() ) {
             alert = new Alert(Alert.AlertType.ERROR, "Please enter a new title.");
             alert.setTitle("Missing Holiday Title");
             alert.showAndWait();
            return;
        }
        
        String tmpName = dateName.getText();
        
        if (curFrom==null){
            alert = new Alert(Alert.AlertType.ERROR, "Missing \"from\" date");
            alert.setTitle("Incorrect Date");
            alert.showAndWait();
            return;
        }
        
        String tmpFrom = curFrom.format(formatter);
        
         if (curTo==null){
            alert = new Alert(Alert.AlertType.ERROR, "Missing \"to\" date");
            alert.setTitle("Incorrect Date");
            alert.showAndWait();
            return;
        }
         
        String tmpTo = curTo.format(formatter);       
          
        if ( curFrom.isAfter(curTo)){
            
            alert = new Alert(Alert.AlertType.ERROR, "The \"From\" date must be a date that is the same as or earlier than the \"To\" date.");
            alert.setTitle("Incorrect Date");
            alert.showAndWait();
            return;
        }

       HolidayList.forEach((b) -> {
          
            if(oldHoliday.equals(b)){
            b.setName(tmpName);
            b.setFromDate(tmpFrom);
            b.setToDate(tmpTo);           
            setCancel();
            
            tableView.setItems(HolidayList);
            tableView.refresh();
            tableView.sort();
            
            }
            
        });
              
    }
  
    @FXML private void setCancel(){
         
        //Clear form controls
        dateName.setText(null);
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
    public void deleteHoliday(ObservableList<Holiday> tmpList){
                if (tmpList==null)
            return;
 
        for(int i = 0; i<tmpList.size(); i++)
            for(int j = 0; j<HolidayList.size(); j++)  
                if (tmpList.get(i).getName().equals(HolidayList.get(j).getName()))  
                    HolidayList.remove(j);
    }
    
    /**
     * This function is called when the block out window is closed.
     * It is set up to be called from the open command in the MainController 
     */
    public void shutDown() {  
        storeData();
    }
 
    /**
      * This will store block out data to file
      */
    public void storeData(){  
        SecureFile scHoliday = new SecureFile("Holidays");
        strData = "";
        
        if (HolidayList == null)
            return;
        
        HolidayList.forEach((b) -> { 
           String hName = Tools.removeSpecialChars(b.getName());

            strData += hName 
                    + "@" + b.getFromDate()
                    + "@" + b.getToDate()
                    + "|";    
        });
        strData = Tools.removeLastChar(strData);
            
        scHoliday.store(strData);
        
        strData = "";
        
    }
 
    /**
     * This is used to load block out data from secure files into the link listing array.
     */
    public void loadHolidays(){
        
        SecureFile scBO = new SecureFile("Holidays");
        String a = scBO.retrieve();
      
        String aArry[] = a.split("\\|", -1);
        for (String b : aArry){
            
            if (b.length() > 2){
                
                String bArry[] = b.split("\\@", -1);
                
                // getSortIndex pulls updated rank order index. 
                if(bArry[0].length() > 0 && bArry[1].length() > 0){
                    String hName = Tools.replaceSpecialChars(bArry[0]);

                    HolidayList.add( new Holiday(
                            hName,
                            bArry[1],
                            bArry[2]
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
                
                if(bArry[0].length() > 0 && bArry[1].length() > 0) {
                    String sName = Tools.replaceSpecialChars(bArry[1]);
                     
                    statusOptions.add(sName);
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
