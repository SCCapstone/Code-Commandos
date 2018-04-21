/**
 * @author Harini, Othen
 * @version 8 3/23/18
 */
package dutyroster;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;


public final class CrewController implements Initializable {
 
    //for entering a new rank
   // @FXML private ComboBox rankCombo;
    //@FXML private TextField nameField;
    //used for tableview 
    @FXML private TableView<Employee> tableView;
   // @FXML private TableColumn<Employee,String> rank;
    @FXML private TableColumn<Employee,String> name;
    @FXML private TableColumn<Employee,Integer> sort;
    @FXML private CheckBox selectAll;
    @FXML private Label ratio;
    @FXML private Button btnDone;
    
    // used to import and export data from employee data
    private ObservableList<Employee> crewList;
    // rankOptions is used to pull the rank information  
    private ObservableList<Rank> rankOptions;
    //rankListing is used to change it in the column box 
    private ObservableList<String> rankListing;
    // used to encrypt, decrypt and store the file.
    private String strData;
    private String crewLink;
   
    public CrewController(){
        startUp();
    }
    
    public void startUp(){
        rankOptions = FXCollections.observableArrayList();
        rankListing = FXCollections.observableArrayList();
        //instantiates new file, use file name Ranks as storage
        crewLink = "Crew_" + MainController.rosterName;
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
        // rankCombo.getItems().setAll(rankListing);
        
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
        
        selectAll.setOnAction((event) -> {
            boolean selected = selectAll.isSelected();
            checkAll(selected);
            statusBar();
        });
        statusBar();   
    }  
    
    
    public void shutDown() {  
        storeData(crewList);
    }
    
    @FXML public void closeScene(){
         Stage stage = (Stage) btnDone.getScene().getWindow();
         stage.close();
        
    }
    public void checkAll(boolean val){
           
        for(Employee f: crewList)
            f.setCrew(val);
    } 
    
    public void statusBar(){
        ratio.setText("# " + getSelected() + "/" + crewList.size() + " are selected.");
    }
    
    public int getSelected(){
           int x=0;
            for(Employee f: crewList){
                if(f.getCrew())
                    x++;
            }
            return x;
    }
    
    public void storeData(ObservableList<Employee> cList){   
       SecureFile scCrews = new SecureFile(crewLink);  
        strData = "";

        if (cList == null || cList.isEmpty()) 
            return;

        cList.forEach((e) -> { 
            if( e.getCrew() ){
                String eRank = Tools.removeSpecialChars(e.getRank());
                String eName = Tools.removeSpecialChars(e.getName());
                strData += eRank + "@" + eName + "|"; 
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
        
        SecureFile scCrews = new SecureFile(crewLink);      
        ArrayList<Employee> aCrews = getCrewData(scCrews);
        SecureFile scEmployees = new SecureFile("Employees");
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
                   
                    String eRank = Tools.replaceSpecialChars(bArry[0]);                    
                   
                    int rankSort = getSortIndex(eRank);

                     eRank = (rankSort < 1000)? eRank : "No rank";
                    
                    String eName = Tools.replaceSpecialChars(bArry[1]);
                  
                    
                    aReturn.add(  new Employee( rankSort, eRank, eName) );
                }
            }    
        } 
        return aReturn;
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
                   String rName = Tools.replaceSpecialChars(bArry[1]);
                   
                   rankOptions.add( new Rank(Integer.parseInt(bArry[0]), rName ) );
                   rankListing.add(rName);
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
        for(Rank r : rankOptions) 
            if ( r.getRank().equals(strRank) )
                    return r.getSort();
        
        return 1000;
    }
      
}
