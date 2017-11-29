/**
 * @authors Austin Freed, Tanya Peyush
 * @version 1
 * 11/20/17
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

    //GUI
    @FXML private TableView<Rank> tableView;
    @FXML private TextField rankField;
    
    //List of ranks
    private ObservableList<Rank> rankList;
    
    //Extracting Data from encrypted file
    private SecureFile sc;
    private String strData;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        rankList = tableView.getItems();
        
        //instantiates new file, use file name Ranks as storage
        sc = new SecureFile("Ranks");
        
        //pull encrypted info and load into ranked list
        retrieveData();
          
        tableView.sort();
    }    

        public void shutDown() {
        
        storeData();

    }
    //Converting store data into an array string
    public void storeData(){
        
        strData = "";
        rankList.forEach((rank) -> {  
            strData +=  rank.getSort() + "@" +  rank.getRank() + "|";    
        });
            strData = removeLastChar(strData);
        
        //Store string array into secure file
        sc.store(strData);
        
        //clear strData
        strData = "";
        
    }
    
    //remove last delimeter from storage array
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
    
    //retrieve data from secure file
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
      
      
    @FXML
    protected void addRank(ActionEvent event) {
            
        rankList.add(new Rank(
                highestRank(),
            rankField.getText()
        ));
         
        tableView.setItems(rankList);
        tableView.sort();
        
        rankField.setText("");
       
    }  

    private int highestRank(){
            return tableView.getItems().size() + 1;
    }
  
    //Changes index to the next higher value
    @FXML
    private void moveSortUp(ActionEvent event){
        
        Rank selectedRank = tableView.getSelectionModel().getSelectedItem();
        //if no rank selected
        if (selectedRank == null)
            return;
        
        int oldSort = selectedRank.getSort();
        
        //if already at the top of the list
        if (oldSort == 1)
            return;
        //move index up
        for(Rank currentRank : rankList) {

            int currentSort = currentRank.getSort();

            if(currentSort == (oldSort - 1) ) 
               currentRank.setSort(oldSort);

        }
        
        //if two duplicate indexes, set older above newer and move both up
        for(Rank currentRank : rankList) {
            
            if ( currentRank.getRank().equals( selectedRank.getRank() ) )
                currentRank.setSort(oldSort - 1);

        }
        tableView.sort();
    }
    
    //Change index to the next lower value
    @FXML
    private void moveSortDown(ActionEvent event){
        
        Rank selectedRank = tableView.getSelectionModel().getSelectedItem();
        //if rank value then return nothing
        if (selectedRank == null)
            return;
        
        int oldSort = selectedRank.getSort();
          
        //At the bottom already
        if (oldSort == tableView.getItems().size())
            return;
        //move index down
        for(Rank currentRank : rankList) {

            int currentSort = currentRank.getSort();

            if(currentSort == (oldSort + 1) ) 
               currentRank.setSort(oldSort);

        }
        //if two duplicate indexes, set older above newer and move both down
        for(Rank currentRank : rankList) {
 
            if ( currentRank.getRank().equals( selectedRank.getRank() ) )
                currentRank.setSort(oldSort + 1);

        }
         tableView.sort();
    }    
    
}
