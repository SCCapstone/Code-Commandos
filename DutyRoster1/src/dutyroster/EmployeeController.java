/**
 * This class is the controller for the Employee editor
 * @author Harini
 * @version 6 3/23/18
 */
package dutyroster;

import java.net.URL;

import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
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
    @FXML private Button btnDone;
    
    // used to import and export data from employee data
    private ObservableList<Employee> employeeList;
    // rankOptions is used to pull the rank information  
    private ObservableList<Rank> rankOptions;
    //rankListing is used to change it in the column  
    private ObservableList<String> rankListing;
    // used to encrypt, decrypt and store the file.
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
        // pull ranks from secure file and place them into rank listing.
        loadRanks(); 
 
        // used to make each cell in the rank column in the editable.
        rank.setCellValueFactory(new PropertyValueFactory<>("rank"));
        rank.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), rankListing));
        rank.setOnEditCommit((TableColumn.CellEditEvent<Employee, String> t) -> {
            String newName = t.getNewValue();
            ObservableList<Employee> items = t.getTableView().getItems();
            TablePosition<Employee, String> tablePosition = t.getTablePosition();
            
            Employee fieldRow = items.get(tablePosition.getRow());
            fieldRow.setRank(newName);
            fieldRow.setSort( Tools.getSortIndex(rankOptions, newName) );
            tableView.sort();
        });
        // used to make each cell in the rank column in the editable.
        nameField.setOnKeyPressed((event) -> { 
            if(event.getCode() == KeyCode.ENTER)         
                if ( !( rankCombo.getValue()==null || nameField.getText().isEmpty()) ) 
                    employeeAdd();
             
        });
       
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
        rankCombo.setItems(rankListing);
       
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
        storeRanks();
    }
 
    public void storeData(){   
        
        // Creating a new secure files employees and load it into secure files.
        SecureFile scEmployees = new SecureFile("Employees");
        
        strData = "";
        
        if (employeeList == null)
            return;
        
        employeeList.forEach((e) -> {  
            String eName = Tools.removeSpecialChars(e.getName());
            String eRank = Tools.removeSpecialChars(e.getRank());

            strData += eRank + "@" + eName + "|";    
        });
            strData = Tools.removeLastChar(strData);
            
        scEmployees.store(strData);
        
        strData = "";
        
    }
    
    /**
     * This is used to load employees from secure files into the link listing array.
     */
    public void loadEmployees(){
        
         // Creating a new secure files employees and load it into secure files.
        SecureFile scEmployees = new SecureFile("Employees");
        String a = scEmployees.retrieve();
      
        String aArry[] = a.split("\\|", -1);
        for (String b : aArry){
            
            if (b.length() > 2){
                
                String bArry[] = b.split("\\@", -1);
                
                // getSortIndex pulls updated rank order index. 
                if(bArry[0].length() > 0 && bArry[1].length() > 0){                   
                    
                    String eRank = Tools.replaceSpecialChars(bArry[0]);
                    int rankSort = Tools.getSortIndex(rankOptions, eRank);

                    eRank = (rankSort < 1000)? eRank : "No grade";

                    String eName = Tools.replaceSpecialChars(bArry[1]);
                    
                    employeeList.add( new Employee( rankSort , eRank, eName) );
                }
            }    
        }       
        tableView.setItems(employeeList);  
        tableView.sort();
    }
    
    /**
     * This is used to load ranks from secure files into the link listing array.
     */
    public void loadRanks(){
        
        SecureFile scRanks = new SecureFile("Ranks");
        String a = scRanks.retrieve();
      
        String aArry[] = a.split("\\|", -1);
        for (String b : aArry){
            
            if (b.length() > 2){
                
                String bArry[] = b.split("\\@", -1);
                
                if(bArry[0].length() > 0 && bArry[1].length() > 0){
                   String rRank = Tools.replaceSpecialChars(bArry[1]);
                   
                   rankOptions.add( new Rank(Integer.parseInt(bArry[0]), rRank) );
                   rankListing.add(rRank);
                }
            
            }
 
        }   
        
    }
    
        //Converting store data into an array string
    public void storeRanks(){

        SecureFile sc = new SecureFile("Ranks");
        strData = "";
        rankOptions.forEach((r) -> {  
            String sRank = Tools.removeSpecialChars(r.getRank());

            strData +=  r.getSort() + "@" +  sRank + "|";    
            
        });
            strData = Tools.removeLastChar(strData);
        
        //Store string array into secure file
        sc.store(strData);
        //clear strData
        strData = "";
        
    }
        
    @FXML
    public void importEmployee() {
        //Still need to handle csv file
        ImportFile importF = new ImportFile(rankOptions);
        fileAddress.setText(importF.getFilePath());
        
        ObservableList<Employee> importList = FXCollections.observableArrayList();

        try{
            importList = importF.getData();
        }
        catch (Exception e) {
            System.out.print(e);
        }

        if(importList==null || importList.isEmpty())
            return;


        boolean eExists;
        
        for(Employee eThis: importList ){ //Cycle trough current import list
            
            eExists = false;
            for(Employee eThat: employeeList){ //Cycle trough current employee list
            
                if(eThis.getName().equals(eThat.getName())){
                    eExists = true;
                    break;
                }
                    
            }
            
            if(!eExists){
                
                String chRank = eThis.getRank();
                
                employeeList.add(
                        new Employee(
                        eThis.getSort(),
                        chRank,
                        eThis.getName()
                ));
            
                if(!rankExists(chRank)){
                    rankOptions.add(new Rank(rankOptions.size() + 1,chRank));
                    rankListing.add(chRank);
                }            
            
            }

        }
       
        Comparator<Employee> comparator; 
        comparator = Comparator.comparingInt(Employee::getSort);
        FXCollections.sort(employeeList, comparator);
        
        int sizeImports = importList.size();
        
        String msg1 = (sizeImports==1)? "employee" : "employees";
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION,"Import file added to Employee list");
        alert.setTitle(sizeImports + " " + msg1 + " added");
        alert.showAndWait();       
        
        fileAddress.clear();
    }
       
    @FXML
    protected void addEmployee(ActionEvent event) {

        employeeAdd();
    }
    
    @FXML
    private void comboAdd(ActionEvent event){
        if ( !( rankCombo.getValue()==null || nameField.getText().isEmpty()) ) 
                    employeeAdd();
    }

    public void employeeAdd(){
           Alert alert;
        
        if (rankCombo.getValue()==null){
             alert = new Alert(Alert.AlertType.ERROR, "Each employee must have a grade."
                     + " If there are no grades avaiable, you can add grades using Grade "
                     + "editor in the Tools menu.");
             alert.setTitle("No Grades Exists");
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
            Tools.getSortIndex(rankOptions,rankCombo.getValue().toString()),
            rankCombo.getValue().toString(),
            nameField.getText()
        ));
      
        tableView.setItems(employeeList);
        tableView.sort();
        nameField.setText("");      
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
    
    //Check to see if the rank already exitst
    public boolean rankExists (String strIn) {
        
        for(Rank currentRank : rankOptions)
            if(currentRank.getRank().equalsIgnoreCase(strIn)) 
                return true;
        
        return false;    
        
    }
    
    @FXML public void closeScene(){
         Stage stage = (Stage) btnDone.getScene().getWindow();
         stage.close();
        
    }
    
}
