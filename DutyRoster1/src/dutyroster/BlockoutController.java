/**
 * 
 * @author Austin Freed, Tanya Peyush
 * @version 5 12/5/17
 */
package dutyroster;


import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;


public class BlockoutController implements Initializable {
 
    //for entering a new rank
    //@FXML private ComboBox statusCombo;
   //
    //used for tableview 
    @FXML private TableView<Blockout> tableView;
    @FXML private TableColumn<Blockout,String> status;
    @FXML private TableColumn<Blockout,String> name;
    @FXML private TableColumn<Blockout,String> fromDate;
    @FXML private TableColumn<Blockout,String> toDate;
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
 
        /*
        // used to make each cell in the rank column in the editable.
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        status.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), blockoutList));
        status.setOnEditCommit((TableColumn.CellEditEvent<Blockout, String> t) -> {
            String newStatus = t.getNewValue();
            ObservableList<Blockout> items = t.getTableView().getItems();
            TablePosition<Blockout, String> tablePosition = t.getTablePosition();
            
            Blockout fieldRow = items.get(tablePosition.getRow());
            fieldRow.setStatus(newStatus);
            tableView.sort();
        });
       
        
        // used to make each cell in the rank column in the editable.
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
            name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(
                (TableColumn.CellEditEvent<Blockout, String> t) ->
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setName(t.getNewValue())
                );       
            
       
       
        
        MenuItem mi1 = new MenuItem("Delete");
            mi1.setOnAction((ActionEvent event) -> { 
                ObservableList<Blockout> items = tableView.getSelectionModel().getSelectedItems();
                //deleteEmployee(items);
            });

        ContextMenu menu = new ContextMenu();
        menu.getItems().add(mi1);
        tableView.setContextMenu(menu);

         //pull the ranks from secureFile and loads it into employees.
        
        
        //add multi select on the table. (more than one row at atime).
        tableView.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        */
        
        //used to edit the tables.
      //  tableView.setEditable(true);
    
        //set proper sorting. 
       // name.setSortType(TableColumn.SortType.ASCENDING);
        //tableView.getSortOrder().add(name);
       // name.setSortable(true);
        
        
    }  
    
    @FXML public void AddBlockout(){
        
        String n, s, f, t;
        
        n = nameCombo.getValue().toString();
        s = statusCombo.getValue().toString();

       SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

         f = dateformat.format(new Date(dateFrom.getYear(), dateFrom.getMonth(), dateFrom.getDayOfMonth()));
        
        LocalDate from = dateFrom.getValue();
        f = dateFormatter.format(from);
        
        LocalDate to = dateTo.getValue();
        t = dateFormatter.format(to);
        
        Blockout temp = new Blockout(n,s,f,t);
        
        blockoutList.add(temp);
        tableView.sort();
        }
                
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
