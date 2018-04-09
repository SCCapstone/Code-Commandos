/**
 * @authors Austin Freed, Tanya Peyush
 * @version 2
 * 3/11/18
 */
package dutyroster;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class RankController implements Initializable {

    //GUI
    @FXML private TableView<Rank> tableView;
    @FXML private TextField rankField;
    @FXML private TableColumn<Rank,Integer> sort;
 
    //List of rankssor
    private ObservableList<Rank> rankList;
    
    //Extracting Data from encrypted file
    private String strData;
    
    public RankController(){
    startUp();
    }
    
    public void startUp(){
        rankList = FXCollections.observableArrayList();

        //pull encrypted info and load into ranked list
        retrieveData();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        rankField.setOnKeyPressed((event) -> { 
            if(event.getCode() == KeyCode.ENTER)         
                if ( !( rankExists(rankField.getText()) || rankField.getText().isEmpty()) ) 
                    rankAdd();
             
        }
        );
        //Add multi select to table
        tableView.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        //Create the Delete menu item
        MenuItem mi1 = new MenuItem("Delete");
            mi1.setOnAction((ActionEvent event) -> { 
                ObservableList<Rank> items = tableView.getSelectionModel().getSelectedItems();
                deleteRank(items);
            });

        ContextMenu menu = new ContextMenu();
        menu.getItems().add(mi1);
        tableView.setContextMenu(menu);
       
        startUp();
        
        updateSort();
    }    

    public void shutDown() {
        
        storeData();
    }
    
    //Converting store data into an array string
    public void storeData(){

        SecureFile sc = new SecureFile("Ranks");
        
        strData = "";
        rankList.forEach((rank) -> {  
            strData +=  rank.getSort() + "@" +  rank.getRank() + "|";    
        });
        strData = Tools.removeLastChar(strData);
        
        //Store string array into secure file
        sc.store(strData);
        //clear strData
        strData = "";
        
    }
    
    //retrieve data from secure file
    public void retrieveData(){
        SecureFile sc = new SecureFile("Ranks");
        
        String a = sc.retrieve();
      
        String aArry[] = a.split("\\|", -1);
        for (String b : aArry){
            
            if (b.length() > 2){
                
                String bArry[] = b.split("\\@", -1);
                
                if(bArry[0].length() > 0 && bArry[1].length() > 0)
                    rankList.add( new Rank(Integer.parseInt(bArry[0]),bArry[1]) );
            }
            
        }

    }
      
    @FXML
    protected void addRank(ActionEvent event){
        rankAdd();
    }
    
    @FXML
    private void comboAdd(ActionEvent event){
        if ( !( rankExists(rankField.getText()) || rankField.getText().isEmpty()) ) 
                    rankAdd();
    }
    
    //The add rank action checks to make sure the rank doesn't already exits
    @FXML
    public void rankAdd() {
        
        Alert alert;
        
        if (rankExists(rankField.getText())){
             alert = new Alert(Alert.AlertType.ERROR, "Each grade must be a unique value.");
             alert.setTitle("Grade Already Exists");
             alert.showAndWait();
          return;
        }
        if (rankField.getText().isEmpty()){
         alert = new Alert(Alert.AlertType.ERROR, " Enter a Grade ");
         alert.setTitle("Missing Grade");
         alert.showAndWait();
      return;
        }
        
        rankList.add(new Rank(
                highestIndexRank(),
            rankField.getText()
        ));
         
         updateSort();
        
        rankField.setText("");
    
    } 
    
    //Returns highest Rank
    public int highestIndexRank(){
            return tableView.getItems().size() + 1;
    }
  
    //Changes index to the next higher value
    @FXML
    private void moveSortUp(ActionEvent event){
        
        Rank selectedRank = tableView.getSelectionModel().getSelectedItem();
        
        if (selectedRank == null)
            return;
        
        int oldSort = selectedRank.getSort();
        
        //If already at the top of the list
        if (oldSort == 1)
            return;
        
        int currentSort = 0;
        for(Rank currentRank : rankList) {

            currentSort = currentRank.getSort();

            if(currentSort == (oldSort - 1) ) {
               currentRank.setSort(oldSort);
               break;
            }
        }

        if(currentSort > 0)
            selectedRank.setSort(currentSort);

      
        updateSort();
    }
    
    //Change index to the next lower value
    @FXML
    public void moveSortDown(ActionEvent event){
        
        Rank selectedRank = tableView.getSelectionModel().getSelectedItem();
        
        if (selectedRank == null)
            return;
        
        int oldSort = selectedRank.getSort();
          
        //At the bottom already
        if (oldSort == tableView.getItems().size())
            return;
        
        int currentSort = 0;
        for(Rank currentRank : rankList) {

            currentSort = currentRank.getSort();

            if(currentSort == (oldSort + 1) ) {
                currentRank.setSort(oldSort);
                break;
            }

        }
        if(currentSort > 0)
            selectedRank.setSort(currentSort);
        
         updateSort();
    }    
 
    //Check to see if the rank already exitst
    public boolean rankExists (String strIn) {
        
        for(Rank currentRank : rankList)
            if(currentRank.getRank().equalsIgnoreCase(strIn)) 
                return true;
        
        return false;    
        
    }
    
    //Delete rank function
    public void deleteRank(ObservableList<Rank> tmpList){
                                   
        if (tmpList==null)
            return;
 
        for(int i = 0; i<tmpList.size(); i++)
            for(int j = 0; j<rankList.size(); j++)  
                if (tmpList.get(i).equals(rankList.get(j)))  
                    rankList.remove(j);
        
        updateSort();
    
    }
    
    private void updateSort(){
        sort.setSortType(TableColumn.SortType.ASCENDING);
        tableView.setItems(rankList);
        tableView.getSortOrder().add(sort);
        tableView.refresh();
        tableView.sort();
    }
    
    //Returns highest Rank
    public int countRanks(){
            return rankList.size();
    }
    
    public void addNewRank(String rank) {
        
        for(Rank currentRank : rankList)
            if(currentRank.getRank().equalsIgnoreCase(rank)) 
                throw new IllegalArgumentException("Grade already exists");
        
        if (rank.isEmpty()){
          throw new IllegalArgumentException("No grade entered");
        }
        
        rankList.add(new Rank(countRanks()+1,rank));
             
    } 
    
    //Delete rank function
    public void delRanks(String[] ranks){
                                   
        if (ranks==null)
            throw new IllegalArgumentException("No grade entered");
        
        for (String rank : ranks) {
            for (int j = 0; j<rankList.size(); j++) {
                if (rank.equals(rankList.get(j).getRank())) {  
                    rankList.remove(j);
                }
            
            }
        
        }

    }

}
