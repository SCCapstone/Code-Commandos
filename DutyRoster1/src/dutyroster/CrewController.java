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
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;




public class CrewController implements Initializable {
 
    //for entering a new rank
    @FXML private ComboBox rankCombo;
    @FXML private TextField nameField;
    //used for tableview 
    @FXML private TableView<Employee> tableView;
    @FXML private TableColumn<Employee,String> rank;
    @FXML private TableColumn<Employee,String> name;
    @FXML private TableColumn<Employee,Integer> sort;
    @FXML private TableColumn<Employee,Boolean> crew;
    
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
 
        //  load the rankListing into rankCombo
        rankCombo.getItems().setAll(rankListing);
        
       // Creating a new secure files employees and load it into secure files.
        scEmployees = new SecureFile("Employees");
        //pull the ranks from secureFile and loads it into employees.
        loadEmployees();
        
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
      
}
