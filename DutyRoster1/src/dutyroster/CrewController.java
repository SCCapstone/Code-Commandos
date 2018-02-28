/**
 * 
 * @author Harini, Othen
 * @version 6 2/14/18
 */
package dutyroster;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class CrewController implements Initializable {
 
    //for entering a new rank
    @FXML private ComboBox rankCombo;
    //@FXML private TextField nameField;
    //used for tableview 
    @FXML private TableView<Employee> tableView;
    @FXML private TableColumn<Employee,String> rank;
    @FXML private TableColumn<Employee,String> name;
    @FXML private TableColumn<Employee,Integer> sort;
    @FXML private TableColumn<Employee,Boolean> crew;
    @FXML private Label outputText;
    @FXML private CheckBox selectAll;
    
    
    // used to import and export data from employee data
    private ObservableList<Employee> crewList;
    // rankOptions is used to pull the rank information  
    private ObservableList<Rank> rankOptions;
    //rankListing is used to change it in the column box 
    private ObservableList<String> rankListing;
    // used to encrypt, decrypt and store the file.
    private SecureFile scCrews;
    private SecureFile scEmployees;
    private SecureFile scRanks;
    private String strData;
    
   
    public CrewController(){
        startUp();
    }
    
    public void startUp(){
        rankOptions = FXCollections.observableArrayList();
        rankListing = FXCollections.observableArrayList();
        //instantiates new file, use file name Ranks as storage
        scRanks = new SecureFile("Ranks");
        //pull encrypted info and load into ranked list
        loadRanks();  
    }
    
    /**
     * This is used to before the GUI interface is initialize.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
        crewList = FXCollections.observableArrayList();
        //Sync crewlist with tableview
        tableView.setItems(crewList); 
 
        startUp();
        //  load the rankListing into rankCombo
        rankCombo.getItems().setAll(rankListing);
        
        //pull the employess from secureFile and loads it into crews.
        loadCrews();
        
        //used to edit the tables.
        tableView.setEditable(true); 
        //set proper sorting. 
        sort.setSortType(TableColumn.SortType.ASCENDING);
        tableView.getSortOrder().add(sort);
        sort.setSortable(true);
        name.setSortType(TableColumn.SortType.ASCENDING);
        tableView.getSortOrder().add(name);
        name.setSortable(true);
        
       // selectAll = new CheckBox();
        selectAll.setOnAction((event) -> {
            boolean selected = selectAll.isSelected();
            checkAll(selected);
        });
        statusBar();
        
    }  
    
    
    public void shutDown() {  
        storeData(crewList);
    }
 
   
    public void checkAll(boolean val){
           
        for(Employee f: crewList)
            f.setCrew(val);
    } 
    
    public void statusBar(){
       // outputText.setText("Total employees: " + crewList.size());
    }
    
    
    public void storeData(ObservableList<Employee> cList){   
       
        strData = "";

        if (cList == null || cList.isEmpty()) 
            return;

        cList.forEach((employee) -> { 
            if( employee.getCrew() ){
                strData += employee.getRank() + "@" + employee.getName() + "|"; 
            }
        });
        
        if (!strData.isEmpty())
            strData = Tools.removeLastChar(strData);
            
        scCrews.store(strData);
        
        strData = "";
        
    }
     
    /**
     * This is used to load employees from secure files into the link listing array.
     */
    public void loadCrews(){
        
        scCrews = new SecureFile("Crew_" + MainController.rosterName);      
        ArrayList<Employee> aCrews = getCrewData(scCrews);
        scEmployees = new SecureFile("Employees");
        ArrayList<Employee> aEmployees = getCrewData(scEmployees);
        
        //add members from crews who are not on the employeelist
        boolean inList = false;
        for(Employee e: aCrews){
            for(Employee f: aEmployees){
                if( e.getName().equals(f.getName()) ){
                    inList = true;
                    break;
                }
            }
            if(!inList){
                aEmployees.add(e);
            }
        }
        
        //Now, check checkboxes
        for(Employee f: aEmployees)
            for(Employee e: aCrews)
                if( f.getName().equals(e.getName()) )
                    f.setCrew(true);
     
        crewList.addAll(aEmployees);
        tableView.sort();
       
    }

    
    public ArrayList<Employee> getCrewData(SecureFile sf){
        
        String a = sf.retrieve();
        
        ArrayList<Employee> aReturn = new ArrayList<>();
        
        String aArry[] = a.split("\\|", -1);
        for (String b : aArry){
            if (b.length() > 2){
                String bArry[] = b.split("\\@", -1);
                // getSortIndex pulls updated rank order index. 
                if(bArry[0].length() > 0 && bArry[1].length() > 0){
                    aReturn.add( new Employee( getSortIndex(bArry[0]), bArry[0], bArry[1]));
                }
            }    
        } 
        return aReturn;
    }
    
    /** 
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
        for(Rank currentRank : rankOptions) 
            if ( currentRank.getRank().equals(strRank) )
                    return currentRank.getSort();
        
        return 0;
    }
      
}
