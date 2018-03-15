/**
 * @authors Austin Freed, Tanya Peyush, Harini Karnati
 * @version 2, 3/15/2018
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.KeyCode;

public final class StatusController implements Initializable {

    //GUI
    //used for tableview 
    @FXML private TableView<Status> tableView;
    @FXML private TableColumn<Status,String> colCode;
    @FXML private TableColumn<Status,String> colTitle;
    @FXML private TableColumn<Status,Boolean> colIncrements;
    @FXML private TextField codeField;
    @FXML private TextField titleField;
    @FXML private CheckBox chkIncrements;
  
    //List of rankssor
    private ObservableList<Status> statusList;
    
    private String strData;
    
    public StatusController(){
    startUp();
    }
    
    public void startUp(){
        statusList = FXCollections.observableArrayList();
        //pull encrypted info and load into ranked list
        retrieveData();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        startUp(); 
        
        //Add multi select to table
        tableView.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        
        titleField.setOnKeyPressed((event) -> { 
            if(event.getCode() == KeyCode.ENTER)         
                if ( !( codeField.getText().isEmpty() || titleField.getText().isEmpty()) ) 
                    statusAdd();
        }
        );
        
        colIncrements.setCellValueFactory(cellData -> cellData.getValue().iProperty());
        colIncrements.setCellFactory(param -> new CheckBoxTableCell<Status, Boolean>());
        
        //Create the Delete menu item
        MenuItem mi1 = new MenuItem("Delete");
            mi1.setOnAction((ActionEvent event) -> { 
                ObservableList<Status> items = tableView.getSelectionModel().getSelectedItems();
                deleteStatus(items);
            });    
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(mi1);
        tableView.setContextMenu(menu);
        
        //used to edit the tables.
        tableView.setEditable(true); 
       
        updateSort();   
    }    

    public void shutDown() {
        storeData();
    }
    
    //Converting store data into an array string
    public void storeData(){
        SecureFile sc = new SecureFile("Status");
        strData = "";
        
        if (statusList == null)
            return;
        statusList.forEach((status) -> { 
            String incs = (status.getIncrements()==true)? "1" : "0";
                 
            strData +=  status.getCode() + "@" +  status.getTitle() + "@" + incs + "|"; });
            strData = Tools.removeLastChar(strData);
        
        //Store string array into secure file
        sc.store(strData);
        
        //clear strData
        strData = ""; 
    }
    
    //retrieve data from secure file
    public void retrieveData(){
        SecureFile sc = new SecureFile("Status");
        String a = sc.retrieve();
      
        String aArry[] = a.split("\\|", -1);
        for (String b : aArry){
            
            if (b.length() > 2){
                
                String bArry[] = b.split("\\@", -1);
                
                if(bArry[0].length() > 0 && bArry[1].length() > 0){
                    boolean incs = ( bArry[2].equals("1") );
                    statusList.add(  new Status(bArry[0], bArry[1], incs)  );  
                }
            }
        }
    }
      
    //The add rank action checks to make sure the rank doesn't already exits
    @FXML 
    protected void addStatus(ActionEvent event) {
        statusAdd();
    }
    
    @FXML
    private void comboAdd(ActionEvent event) {
        if ( !( codeField.getText().isEmpty() || titleField.getText().isEmpty()) ) 
                    statusAdd();
    }
    
    public void statusAdd(){
        
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
        
        if (codeField.getText().length() > 1){
             alert = new Alert(Alert.AlertType.ERROR, "Code Field can only contain one value");
             alert.setTitle("Code Length Too Long");
             alert.showAndWait();
          return;
        }
        
        boolean incs = chkIncrements.isSelected();
        statusList.add(  new Status(codeField.getText(),titleField.getText(), incs)  );
        
        updateSort();
    }
    
    /**
     * This is workaround for a known table update bug
     */
    private void updateSort(){
         //Sets the code column as the primary sort
        colCode.setSortType(TableColumn.SortType.ASCENDING);
        //Sets the code title as the secondary sort
        colTitle.setSortType(TableColumn.SortType.ASCENDING);
        tableView.setItems(statusList);
        tableView.getSortOrder().add(colTitle);
        tableView.getSortOrder().add(colCode);
        tableView.refresh();
        tableView.sort();
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
    
     public boolean titleExists (String strIn) {
        
        if(!statusList.isEmpty())
            for(Status currentStatus : statusList)
                if(currentStatus.getTitle().equalsIgnoreCase(strIn)) 
                    return true;
        
        return false;      
    }
 
}