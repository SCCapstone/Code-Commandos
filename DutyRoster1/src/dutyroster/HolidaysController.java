/**
 * This class will allow user to setup holiday dates for employees, which will 
 * excuse them from the duty assignment for that period of time.
 * @author Tanya Peyush
 * @assisted by Austin Freed
 * @version 2 3/14/2018
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class HolidaysController implements Initializable {
    
    @FXML private TableView<Holidays> tableView;
    @FXML private TableColumn<Holidays,String> name;
    @FXML private TableColumn<Holidays,String> fromDate;
    @FXML private TableColumn<Holidays,String> toDate;
    @FXML private TextField dateName;
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
    
    //Hold localdates from datepickers
    private LocalDate curFrom, curTo;
    private static final String DATE_FORMAT = "d MMM uuuu";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    
    //Used to import and export data from employee data
    private ObservableList<String> employeeOptions;
    private ObservableList<String> statusOptions;
    // rankOptions is used to pull the rank information  
 
    //rankListing is used to change it in the column  
    private ObservableList<Holidays> HolidaysList;
   
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
        HolidaysList = FXCollections.observableArrayList();
             
        loadHolidays();
        
        //Scene formatting
        setDatePickers();

        //This completely attaches the Holiday List to the table. 
        tableView.setItems(HolidaysList);

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
        
        //Create the Delete menu item
        MenuItem mi1 = new MenuItem("Delete");
            mi1.setOnAction((ActionEvent event) -> { 
                ObservableList<Holidays> items = tableView.getSelectionModel().getSelectedItems();
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
        
        String tmpName = dateName.getText();
        String tmpFrom = curFrom.format(formatter);
        String tmpTo = curTo.format(formatter);
                
        //Add new block out instance to the table
 
        HolidaysList.add(new Holidays(tmpName, tmpFrom, tmpTo));
        tableView.sort();
        
        //Clear form controls
        dateName.clear();
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
    public void deleteHoliday(ObservableList<Holidays> tmpList){
                if (tmpList==null)
            return;
 
        for(int i = 0; i<tmpList.size(); i++)
            for(int j = 0; j<HolidaysList.size(); j++)  
                if (tmpList.get(i).getName().equals(HolidaysList.get(j).getName()))  
                    HolidaysList.remove(j);
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
        
        if (HolidaysList == null)
            return;
        
        HolidaysList.forEach((b) -> { 
           
            strData += b.getName() 
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
                    
                    HolidaysList.add( new Holidays(
                            bArry[0],
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
