
package dutyroster;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
//import javafx.stage.WindowEvent;
import javafx.util.converter.DefaultStringConverter;


public class EmployeeController implements Initializable {

    
    @FXML private TableView<Employee> tableView;
    @FXML private ComboBox rankCombo = new ComboBox();
    @FXML private TextField nameField;
    @FXML private TableColumn<Employee,String> rank;
    @FXML private TableColumn<Employee,String> name;
    @FXML private TableColumn<Employee,Integer> sort;
    private ObservableList<Employee> employeeList;
    private ObservableList<Rank> rankOptions;
    private ObservableList<String> rankListing;
    private SecureFile scEmployees;
    private SecureFile scRanks;
    private String strData;

        
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
      
        
        employeeList = FXCollections.observableArrayList();
        rankOptions = FXCollections.observableArrayList();
        rankListing = FXCollections.observableArrayList();
        scRanks = new SecureFile("Ranks");  
        loadRanks(); 
 
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

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(
                (TableColumn.CellEditEvent<Employee, String> t) ->
                    ( t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setName(t.getNewValue())
                );       
        
        /*
        name.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Employee, String>>() {
                @Override
                public void handle(CellEditEvent<Employee, String> t) {
                    ((Employee) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                    ).setName(t.getNewValue());
                    tableView.sort();   
                }
            }
        );       
        */
       
       rankCombo.getItems().setAll(rankListing);
        
        scEmployees = new SecureFile("Employees");
        loadEmployess();
        
        tableView.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        
        tableView.setEditable(true);
        
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
            strData = removeLastChar(strData);
            
        scEmployees.store(strData);
        
        strData = "";
        
    }
    
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
    
    public void loadEmployess(){
        
        String a = scEmployees.retrieve();
      
        String aArry[] = a.split("\\|", -1);
        for (String b : aArry){
            
            if (b.length() > 2){
                
                String bArry[] = b.split("\\@", -1);
                
                if(bArry[0].length() > 0 && bArry[1].length() > 0)
                    employeeList.add( new Employee( getSortIndex(bArry[0]), bArry[0], bArry[1]) );
            
            }
            
        }
        
        tableView.setItems(employeeList);  
        tableView.sort();
    }
    
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
    protected void addEmployee(ActionEvent event) {
       
        if (rankCombo.getValue()==null)
            return;
        
        employeeList.add(new Employee(
            getSortIndex(rankCombo.getValue().toString()),
            rankCombo.getValue().toString(),
            nameField.getText()
        ));
      
       tableView.setItems(employeeList);
       tableView.sort();
        
        nameField.setText("");  
 
    }  

    
    private int getSortIndex(String strRank) {
            
        for(Rank currentRank : rankOptions) {
          
            if (currentRank.getRank().equals(strRank) ){
                    return currentRank.getSort();
            }
        }
        
        return 0;
    }
    
}
