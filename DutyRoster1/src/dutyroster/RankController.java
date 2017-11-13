/**
 * This class is used to handle all rank related data and control
 * the FMXL GUI interface.
 * @Author Othen W. Prock
 * @Version 1.2, 11 November, 2017
 * 
 */
package dutyroster;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


public class RankController implements Initializable {

    @FXML private TableView<Rank> tableView;
    @FXML private TextField rankField;
    private ObservableList<Rank> rankList;
    private SecureFile sc;
   
    private String strData;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        rankList = tableView.getItems();
        
        //Instantiate a new securefile instance of the ranks storage
        sc = new SecureFile("Ranks");
        
        //This will load data from file and place it into the active rank table
        retrieveData();
        
        //This will resort table to the sort set in the rank FXML
        tableView.sort();
    }    

    /**
     * This function is connected to a scene listener in the driver class and 
     * it is called whenever the scene is closed.
     */
    public void shutDown() {
        storeData();
    }
    
    /**
     * This function delimits all active data currently in the table and 
     * and stores it to an encrypted file.
     */
    public void storeData(){
        
        //This must be included or javaFx add the word null to the string.
        //IMPORTANT! This string cannot be create locally because 
        //local variables referenced from a lambda expressions must be final or
        //effectively final
        strData = "";  
                        
        //Loop through the current active rank table and delimit each tupple
        // into a string
        rankList.forEach((rank) -> {  
            strData +=  rank.getSort() + "@" +  rank.getRank() + "|";    
        });  
        //Strip the last unneed delimiter off the last delimiter added during the loop
        strData = removeLastChar(strData); 
        
        //Store file using SecureFile store fucntion
        sc.store(strData);
        
        //Clear the string and remove it from memory
        strData = "";
        
    }
    
    //TODO: This will eventually be moved into a tools class it will be 
    //needed in other class files.
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
    
    /**
     * Retrieves string and loads it to the active table.
     *
     */
    public void retrieveData(){
        
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
      
    /**
     * This function is attached to the "add" button in the Rank scene, and it
     * adds a rank to the table.
     * @param event 
     */  
    @FXML
    protected void addRank(ActionEvent event) {
        
        //return if the user presses add  without entering anything
        //if (rankField.getText().isEmpty())
           // return;

        //Adds new rank to the rank list
        rankList.add(new Rank(
                highestRank(),
            rankField.getText()
        ));
        
        //Reloads the table with the additional row and sorts it.
        tableView.setItems(rankList);
        tableView.sort();
        
        //Empties the rank field
        rankField.setText("");
       
    }  

    /**
     * highestRank. used to determine the next sort number.
     * @return 
     */
    private int highestRank(){
            return tableView.getItems().size() + 1;
    }
  
    
    /**
     * That function is attached to the "move up" button in the GUI. It switches
     * the index of the currently selected rank with the one directly above.
     * 
     * @param event 
     */
    @FXML
    private void moveSortUp(ActionEvent event){
        
        Rank selectedRank = tableView.getSelectionModel().getSelectedItem();
        
        if (selectedRank == null)
            return;
        
        int oldSort = selectedRank.getSort();
          
        if (oldSort == 1)
            return;
        
        for(Rank currentRank : rankList) {

            int currentSort = currentRank.getSort();

            if(currentSort == (oldSort - 1) ) 
               currentRank.setSort(oldSort);

        }
        
        for(Rank currentRank : rankList) {
 
            if ( currentRank.getRank().equals( selectedRank.getRank() ) )
                currentRank.setSort(oldSort - 1);

        }
        tableView.sort();
    }
    
    /**
     * This function is attached the move down button in the GUI interface.
     * 
     * @param event 
     */
    @FXML
    private void moveSortDown(ActionEvent event){
        
        Rank selectedRank = tableView.getSelectionModel().getSelectedItem();
        
        if (selectedRank == null)
            return;
        
        int oldSort = selectedRank.getSort();
          
        if (oldSort == tableView.getItems().size())
            return;
        
        for(Rank currentRank : rankList) {

            int currentSort = currentRank.getSort();

            if(currentSort == (oldSort + 1) ) 
               currentRank.setSort(oldSort);

        }
        
        for(Rank currentRank : rankList) {
 
            if ( currentRank.getRank().equals( selectedRank.getRank() ) )
                currentRank.setSort(oldSort + 1);

        }
         tableView.sort();
    }    
    
}
