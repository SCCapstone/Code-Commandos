/**
 * 
 * @author Harini
 * @version 5 12/5/17
 */
package dutyroster;

import java.net.URL;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
//import javafx.stage.WindowEvent;
import javafx.util.converter.DefaultStringConverter;


public class EmployeeController implements Initializable {
 
    //for entering a new rank
    @FXML private ComboBox rankCombo;
    @FXML private TextField nameField;
    @FXML private TextField fileAddress;
    //used for tableview 
    @FXML private TableView<Employee> tableView;
    @FXML private TableColumn<Employee,String> rank;
    @FXML private TableColumn<Employee,String> name;
    @FXML private TableColumn<Employee,Integer> sort;
    
    
    // used to import and export data from employee data
    private ObservableList<Employee> employeeList;
    // rankOptions is used to pull the rank information  
    private ObservableList<Rank> rankOptions;
    //rankListing is used to change it in the column box 
    private ObservableList<String> rankListing;
    // used to encrypt, decrypt and store the file.
    private SecureFile scEmployees;
    private SecureFile scRanks;
    private String strData;
    
    /**
     * This is used to before the GUI interface is initialize.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
        employeeList = FXCollections.observableArrayList();
        rankOptions = FXCollections.observableArrayList();
        rankListing = FXCollections.observableArrayList();
        // using file name ranks for secure files
        scRanks = new SecureFile("Ranks");  
        // pull ranks from secure file and place them into rank listing.
        loadRanks(); 
 
        // used to make each cell in the rank column in the editable.
        rank.setCellValueFactory(new PropertyValueFactory<>("rank"));
        rank.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), rankListing));
        rank.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Employee, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Employee, String> t) {
                    String newName = t.getNewValue();
                    ObservableList<Employee> items = t.getTableView().getItems();
                    TablePosition<Employee, String> tablePosition = t.getTablePosition();

                    Employee fieldRow = items.get(tablePosition.getRow());
                    fieldRow.setRank(newName);
                    fieldRow.setSort( getSortIndex(newName) );
                    tableView.sort();
            }
        });
        // used to make each cell in the rank column in the editable.
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(
                (TableColumn.CellEditEvent<Employee, String> t) ->
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setName(t.getNewValue())
                );       
            
       
        MenuItem mi1 = new MenuItem("Delete");
            mi1.setOnAction((ActionEvent event) -> { 
                ObservableList<Employee> items = tableView.getSelectionModel().getSelectedItems();
                deleteEmployee(items);
            });

        ContextMenu menu = new ContextMenu();
        menu.getItems().add(mi1);
        tableView.setContextMenu(menu);

        //  load the rankListing into rankCombo
        rankCombo.getItems().setAll(rankListing);
        
       // Creating a new secure files employees and load it into secure files.
        scEmployees = new SecureFile("Employees");
        //pull the ranks from secureFile and loads it into employees.
        loadEmployees();
        
        //add multi select on the table. (more than one row at atime).
        tableView.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        
        //used to edit the tables.
        tableView.setEditable(true);
    
        //set proper sorting. 
        sort.setSortType(TableColumn.SortType.ASCENDING);
        tableView.getSortOrder().add(sort);
        sort.setSortable(true);
        name.setSortType(TableColumn.SortType.ASCENDING);
        tableView.getSortOrder().add(name);
        name.setSortable(true);
        
        
    }  
    
    public void shutDown() {  
        storeData();
    }
 
    public void storeData(){   
        strData = "";
        
        if (employeeList == null)
            return;
        
        employeeList.forEach((employee) -> {  
            strData += employee.getRank() + "@" + employee.getName() + "|";    
        });
            strData = Tools.removeLastChar(strData);
            
        scEmployees.store(strData);
        
        strData = "";
        
    }
    

    /**
     * This is used to load employees from secure files into the link listing array.
     */
    public void loadEmployees(){
        
        String a = scEmployees.retrieve();
      
        String aArry[] = a.split("\\|", -1);
        for (String b : aArry){
            
            if (b.length() > 2){
                
                String bArry[] = b.split("\\@", -1);
                
                // getSortIndex pulls updated rank order index. 
                if(bArry[0].length() > 0 && bArry[1].length() > 0){
                    employeeList.add( new Employee( getSortIndex(bArry[0]), bArry[0], bArry[1]) );
                }
            }    
        }       
        tableView.setItems(employeeList);  
        tableView.sort();
    }
    
    /**
     * 
     * This is used to load ranks from secure files into the link listing array.
     */
    public void loadRanks(){
        
        String a = scRanks.retrieve();
      
        String aArry[] = a.split("\\|", -1);
        for (String b : aArry){
            
            if (b.length() > 2){
                
                String bArry[] = b.split("\\@", -1);
                
                if(bArry[0].length() > 0 && bArry[1].length() > 0){
                   
                   rankOptions.add( new Rank(Integer.parseInt(bArry[0]), bArry[1] ) );
                   rankListing.add(bArry[1]);
                }
            }
 
        }   
        
    }
        
    @FXML
    public void importEmployee() {
        //Still need to handle csv file
        ImportFile importF = new ImportFile();
        fileAddress .setText(importF.getFilePath());
        
        
    }
    
    @FXML
    protected void addEmployee(ActionEvent event) {
       Alert alert;
        
        if (rankCombo.getValue()==null){
             alert = new Alert(Alert.AlertType.ERROR, "Each employee must have a rank."
                     + " If there are no ranks avaiable, you can add ranks using Rank "
                     + "editor in the Tools menu.");
             alert.setTitle("No Ranks Exists");
             alert.showAndWait();
            return;
        }
        if (nameField.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR, "Must Enter an Employee Name.");
            alert.setTitle("Missing Name");
            alert.showAndWait();
            return;
        }
                
        employeeList.add(new Employee(
            getSortIndex(rankCombo.getValue().toString()),
            rankCombo.getValue().toString(),
            nameField.getText()
        ));
      
        tableView.setItems(employeeList);
        tableView.sort();
        nameField.setText("");  
 
    }  

    /**
     * getSortIndex pulls the index number for the rank.
     * @param strRank
     * @return 
     */
    private int getSortIndex(String strRank) {
            
        //pulling from the rank, in rankOption pull the current rank to get the index number.
        for(Rank currentRank : rankOptions) {
          
            if (currentRank.getRank().equals(strRank) ){
                    return currentRank.getSort();
            }
        }
        
        return 0;
    }
    
    
    //Change index to the next lower value
    private void deleteEmployee(ObservableList<Employee> tmpList){
                                   
        if (tmpList==null)
            return;
 
        for(int i = 0; i<tmpList.size(); i++)
            for(int j = 0; j<employeeList.size(); j++)  
                if (tmpList.get(i).equals(employeeList.get(j)))  
                    employeeList.remove(j);
         
        tableView.setItems(employeeList);  
        tableView.sort();
    
    }
    
}
