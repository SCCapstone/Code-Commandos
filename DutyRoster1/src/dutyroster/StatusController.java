/**
 * @authors Austin Freed, Tanya Peyush, Harini Karnati
 * 02/12/2017
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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


public class StatusController implements Initializable {

    //GUI
     //@FXML private TextField statusField;
    //used for tableview 
    @FXML private TableView<Status> tableView;
   // @FXML private TableColumn<Status,String> rank;
   // @FXML private TableColumn<Status,String> name;
    @FXML private TableColumn<Status,Integer> sort;
    //@FXML private TableView<Status> tableView;
    @FXML private TextField statusField;
    @FXML private TextField statusField2;
    @FXML private TextField titleField;
    //@FXML private TableColumn<Status,Integer> sort;
     
    //List of rankssor
    private ObservableList<Status> statusList;
     private ObservableList<Status> titleList;
     private ObservableList<String> statusListing;
    // used to encrypt, decrypt and store the file.
  //  private SecureFile scStatus;
    
    //Extracting Data from encrypted file
    private SecureFile sc;
    private String strData;
    
    public StatusController(){
    startUp();
    }
    
    
    
    public void startUp(){
        statusList = FXCollections.observableArrayList();
        //instantiates new file, use file name Ranks as storage
        sc = new SecureFile("Status");

        //pull encrypted info and load into ranked list
        retrieveData();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Add multi select to table
        tableView.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        //Create the Delete menu item
        MenuItem mi1 = new MenuItem("Delete");
            mi1.setOnAction((ActionEvent event) -> { 
                ObservableList<Status> items = tableView.getSelectionModel().getSelectedItems();
                deleteStatus(items);
            });

        ContextMenu menu = new ContextMenu();
        menu.getItems().add(mi1);
        tableView.setContextMenu(menu);
       
        startUp();
        
//        updateSort();
    }    

    public void shutDown() {
        
        storeData();
    }
    
    //Converting store data into an array string
    public void storeData(){
        
        strData = "";
        
        if (statusList == null)
            return;
        statusList.forEach((status) -> {  
            strData +=  status.getSort() + "@" +  status.getStatus() + "|";    
        });
            strData = removeLastChar(strData);
        
        //Store string array into secure file
        sc.store(strData);
        
        //clear strData
        strData = "";
        
    }
    
    //remove last delimeter from storage array
    private static String removeLastChar(String str) {
        if(str.length() <= 1 )
            return "";
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
                    statusList.add( new Status((bArry[0]),bArry[1]) );
            }
            
        }

    }
      
    //The add rank action checks to make sure the rank doesn't already exits
    @FXML
    public void addStatus(ActionEvent event) {
        
        Alert alert;
        
        if (statusExists(statusField.getText())){
             alert = new Alert(Alert.AlertType.ERROR, "Each status must be a unique value.");
             alert.setTitle("Status Already Exists");
             alert.showAndWait();
          return;
        }
        if (statusField.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR, " Enter a Status ");
            alert.setTitle("Missing Status");
            alert.showAndWait();
        return;
        }
        
        if (titleExists(statusField2.getText())){
            alert = new Alert(Alert.AlertType.ERROR, "Each status must be a unique value.");
            alert.setTitle("Title Already Exists");
            alert.showAndWait();
        return;
        }
        if (statusField2.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR, " Enter a Status ");
            alert.setTitle("Missing Status");
            alert.showAndWait();
        return;
        }
        
        statusList.add(new Status(statusField.getText(), statusField2.getText()
        ));
         
        // updateSort();
        
        //statusField.setText("");
        //statusField2.setText("");
    
    } 
    /*
    public void addTitle(ActionEvent event) {
        
        Alert alert;
        
        if (titleExists(statusField.getText())){
             alert = new Alert(Alert.AlertType.ERROR, "Each status must be a unique value.");
             alert.setTitle("Title Already Exists");
             alert.showAndWait();
          return;
        }
        if (statusField.getText().isEmpty()){
         alert = new Alert(Alert.AlertType.ERROR, " Enter a Status ");
         alert.setTitle("Missing Status");
         alert.showAndWait();
        return;
        }
        
       // ObservableList<Status> add = titleList.add(new Title(highestIndexStatus(),titleField.getText()
        //));
        statusList.add(new Status(statusField.getText(), statusField2.getText()
        )); 
       //  updateSort();
        
        statusField.setText("");
        statusField2.setText("");
    
    } 
    */
   /* //Returns highest Rank
    public int highestIndexStatus(){
            return tableView.getItems().size() + 1;
    }
  
    //Changes index to the next higher value
    @FXML
    private void moveSortUp(ActionEvent event){
        
        Status selectedStatus = tableView.getSelectionModel().getSelectedItem();
        
        if (selectedStatus == null)
            return;
        
        int oldSort = selectedStatus.getSort();
        
        //If already at the top of the list
        if (oldSort == 1)
            return;
        
        int currentSort = 0;
        for(Status currentStatus : statusList) {

            currentSort = currentStatus.getSort();

            if(currentSort == (oldSort - 1) ) {
               currentStatus.setSort(oldSort);
               break;
            }
        }

        if(currentSort > 0)
            selectedStatus.setSort(currentSort);

      
        updateSort();
    }
    
    //Change index to the next lower value
    @FXML
    public void moveSortDown(ActionEvent event){
        
        Status selectedStatus = tableView.getSelectionModel().getSelectedItem();
        
        if (selectedStatus == null)
            return;
        
        int oldSort = selectedStatus.getSort();
          
        //At the bottom already
        if (oldSort == tableView.getItems().size())
            return;
        
        int currentSort = 0;
        for(Status currentStatus : statusList) {

            currentSort = currentStatus.getSort();

            if(currentSort == (oldSort + 1) ) {
                currentStatus.setSort(oldSort);
                break;
            }

        }
        if(currentSort > 0)
            selectedStatus.setSort(currentSort);
        
  
         updateSort();
    }    
 */
    //Check to see if the rank already exitst
    public boolean statusExists (String strIn) {
        
        for(Status currentStatus : statusList)
            if(currentStatus.getStatus().equalsIgnoreCase(strIn)) 
                return true;
        
        return false;    
        
    }
    
     public boolean titleExists (String strIn) {
        
        for(Status currentStatus : titleList)
            if(currentStatus.getStatus().equalsIgnoreCase(strIn)) 
                return true;
        
        return false;    
        
    }
    
    //Delete rank function
    public void deleteStatus(ObservableList<Status> tmpList){
                                   
        if (tmpList==null)
            return;
 
        for(int i = 0; i<tmpList.size(); i++)
            for(int j = 0; j<statusList.size(); j++)  
                if (tmpList.get(i).equals(statusList.get(j)))  
                    statusList.remove(j);
        
      //  updateSort();
    
    }
    
  /*  private void updateSort(){
        sort.setSortType(TableColumn.SortType.ASCENDING);
        tableView.setItems(statusList);
        tableView.getSortOrder().add(sort);
        tableView.refresh();
        tableView.sort();
    }*/
    
    
   /*     //Returns highest Rank
    public int countStatus(){
            return statusList.size() + 1;
    }
    */
     public void addNewStatus(String status) {
        
        for(Status currentStatus : statusList)
            if(currentStatus.getStatus().equalsIgnoreCase(status)) 
                throw new IllegalArgumentException("Status already exists");
        
        
        if (status.isEmpty()){
          throw new IllegalArgumentException("No status entered");
        }
        
       statusList.add(new Status(status, title ));
             
    } 
    
    //Delete rank function
    public void delStatus(String[] status){
                                   
        if (status==null)
            throw new IllegalArgumentException("No status entered");
        
        for(int i = 0; i<status.length; i++)
            for(int j = 0; j<statusList.size(); j++)  
                if (status[i].equals(statusList.get(j)))  
                    statusList.remove(j);

    }
}
