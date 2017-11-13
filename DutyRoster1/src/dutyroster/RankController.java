
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
        sc = new SecureFile("Ranks");
           
        retrieveData();
          
        tableView.sort();
    }    

        public void shutDown() {
        
        storeData();

    }
        
    public void storeData(){
        
        strData = "";
        rankList.forEach((rank) -> {  
            strData +=  rank.getSort() + "@" +  rank.getRank() + "|";    
        });
            strData = removeLastChar(strData);
            
        sc.store(strData);
        
        strData = "";
        
    }
    
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
    
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
