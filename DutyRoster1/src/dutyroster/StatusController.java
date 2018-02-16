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


public final class StatusController implements Initializable {

    //GUI
    //used for tableview 
     @FXML private TableView<Status> tableView;
    @FXML private TableColumn<Status,String> colCode;
    @FXML private TableColumn<Status,String> colTitle;
    @FXML private TextField codeField;
    @FXML private TextField titleField;
  
    //List of rankssor
    private ObservableList<Status> statusList;
    // used to encrypt, decrypt and store the file.
    // private SecureFile scStatus;
    
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
        
        updateSort();
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
            strData +=  status.getCode() + "@" +  status.getTitle() + "|"; });
            strData = Tools.removeLastChar(strData);
        
        //Store string array into secure file
        sc.store(strData);
        
        //clear strData
        strData = "";
        
    }
    
    //retrieve data from secure file
    public void retrieveData(){
        
        String a = sc.retrieve();
      
        String aArry[] = a.split("\\|", -1);
        for (String b : aArry){
            
            if (b.length() > 2){
                
                String bArry[] = b.split("\\@", -1);
                
                if(bArry[0].length() > 0 && bArry[1].length() > 0)
                    statusList.add( new Status(bArry[0],bArry[1]) );
            }
            
        }

    }
      
    //The add rank action checks to make sure the rank doesn't already exits
    @FXML public void addStatus(ActionEvent event) {
        
        Alert alert;
       
        if (codeField.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR, " Enter a status code.");
            alert.setTitle("Missing Status");
            alert.showAndWait();
        return;
        }  
        
        if (titleField.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR, " Enter a status title.");
            alert.setTitle("Missing Status");
            alert.showAndWait();
        return;
        }
        
        
        if (titleExists(titleField.getText())){
             alert = new Alert(Alert.AlertType.ERROR, "Each title must be a unique value.");
             alert.setTitle("Status Already Exists");
             alert.showAndWait();
          return;
        }
       
        
        statusList.add( new Status(codeField.getText(),titleField.getText()) );
         
       updateSort();
        
       codeField.setText("");
       titleField.setText("");
    
    } 
 
    //Delete rank function
    public void deleteStatus(ObservableList<Status> tmpList){
                                   
        if (tmpList==null)
            return;
 
        for(int i = 0; i<tmpList.size(); i++)
            for(int j = 0; j<statusList.size(); j++)  
                if (tmpList.get(i).equals(statusList.get(j)))  
                    statusList.remove(j);
        
      updateSort();
    }
    
    private void updateSort(){
        colCode.setSortType(TableColumn.SortType.ASCENDING);
        tableView.setItems(statusList);
        tableView.getSortOrder().add(colCode);
        tableView.refresh();
        tableView.sort();
    }
    
     public boolean titleExists (String strIn) {
        
        if(!statusList.isEmpty())
            for(Status currentStatus : statusList)
                if(currentStatus.getTitle().equalsIgnoreCase(strIn)) 
                    return true;
        
        return false;      
    }
 
}
