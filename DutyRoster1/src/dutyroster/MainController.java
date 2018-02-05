package dutyroster;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;


public class MainController implements Initializable {
    
    @FXML private TableView<ObservableList<StringProperty>> tableView = new TableView<>();
    @FXML private ComboBox comboMonth;
    @FXML private ComboBox comboYear;  
     
   
    private ObservableList<String> monthList = FXCollections.observableArrayList();
    private ObservableList<Integer> yearList = FXCollections.observableArrayList();
    private ObservableList<ObservableList<String>> rosterList = FXCollections
            .observableArrayList();
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Calendar now = Calendar.getInstance();
        int curYear = now.get(Calendar.YEAR);
        int curMonth = now.get(Calendar.MONTH);          

        loadMonths(curMonth);
        loadYears(curYear);
        setDate(curYear, curMonth);
        
    }    
        
    @FXML
    private void exitProgram(ActionEvent event) {   
    System.exit(0);
    }

    @FXML
    private void openEmployeeEditor(ActionEvent event) {
         
        try{
         FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeFXML.fxml")); 
         Parent root1 = loader.load();
         EmployeeController eController = loader.getController();
         Stage stage = new Stage();
         stage.setTitle("Employee Editor");
         stage.setResizable(false);
         stage.setScene(new Scene(root1));
         stage.setOnHidden(e -> eController.shutDown());
         stage.show(); 
          
        }
        catch(Exception e){
           System.out.println("Can't load new scene: " + e); 
        }
    }
    
    
     @FXML
    private void openRankEditor(ActionEvent event) {
         
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RankFXML.fxml")); 
            Parent root1 = loader.load();
            RankController eController = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Rank Editor");
            stage.setResizable(false);
            Scene sceneRank = new Scene(root1);
            //sceneRank.getStylesheets().add("stylesheet.css");

            stage.setScene(sceneRank);
            stage.setOnHidden(e -> eController.shutDown());
            stage.show(); 
          
        }
        catch(Exception e){
           System.out.println("Can't load new scene: " + e); 
        }
    }  

    public void loadMonths(int curMonth){
    
        String[] monthArry = new String[] {"January", "Febuary", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"}; 
 
        for(String month : monthArry){
            
            monthList.add(month);   
        }
        
        comboMonth.getItems().setAll(monthList);
        comboMonth.getSelectionModel().select(curMonth);

    }
 
    private void loadYears(int curYear) {
        
        for(int i = curYear - 3; i < curYear + 5; i++)
            yearList.add(i);
    
        comboYear.getItems().setAll(yearList);
        comboYear.getSelectionModel().select(Integer.toString(curYear));
    }
    
    
    @FXML public void newDate(ActionEvent event){
        
        int month = convertMonth(comboMonth.getValue().toString());
        int year = Integer.parseInt(comboYear.getValue().toString());
   
        setDate(year, month);

    }
    
    @FXML public void goUp(ActionEvent event){
        dateIncrement(true);
    }  
    
    @FXML public void goDown(ActionEvent event){
        dateIncrement(false);
    } 
     
    private void dateIncrement(boolean up){

       int month = convertMonth(comboMonth.getValue().toString());
       int year = Integer.parseInt(comboYear.getValue().toString());
         
       if(up && month == 11){
           year += 1;
           month = 0;
       }
       else if (!up && month == 0){
           year -= 1;
           month = 11;
       }
       else{
            int a = (up)? 1: -1; 
            month = month + a;
       }

       setDate(year,month);
       
       comboMonth.getSelectionModel().select(month);
       comboYear.getSelectionModel().select(Integer.toString(year));

    }
     
    private void setDate(int intYear, int intMonth){
 
        String[] weekDay = new String[] {null, "SU", "MO", "TU", "WE", "TH", "FR", "SA"};
        
        Calendar selCal = Calendar.getInstance();
        selCal.set(intYear, intMonth, 1);
        int lastDay = selCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        ObservableList<StringProperty> row = FXCollections.observableArrayList();
        
        tableView.getColumns().clear();
        tableView.getItems().clear();
        
        TableColumn<ObservableList<StringProperty>, String> colRank = createColumn(0, "Rank");
        colRank.setPrefWidth(150);
        colRank.setMaxWidth(300);
        tableView.getColumns().add(colRank);
     
        TableColumn<ObservableList<StringProperty>, String> colName = createColumn(1, "Name");
        colName.setPrefWidth(300);
        colName.setMaxWidth(600);
        tableView.getColumns().add(colName);
      
        TableColumn<ObservableList<StringProperty>, String> colInc = createColumn(2, "Increment");
        colInc.setPrefWidth(150);
        colInc.setMaxWidth(150);
        colInc.setResizable(false);
  
            TableColumn<ObservableList<StringProperty>, String> colN = createColumn(2, "N");
            colN.setPrefWidth(35);
            colN.setMaxWidth(35);
            colN.setResizable(false);
            colN.setStyle("-fx-alignment: CENTER;");
            colInc.getColumns().add(colN);  
           
            
            TableColumn<ObservableList<StringProperty>, String> colW = createColumn(3, "W");
            colW.setPrefWidth(35);
            colW.setMaxWidth(35);
            colW.setResizable(false);
            colW.setStyle("-fx-alignment: CENTER;");
            colInc.getColumns().add(colW);
            
            
            TableColumn<ObservableList<StringProperty>, String> colH = createColumn(4, "H");
            colH.setPrefWidth(35);
            colH.setMaxWidth(35);
            colH.setResizable(false);
            colH.setStyle("-fx-alignment: CENTER;");
            colInc.getColumns().add(colH); 
            
            
        tableView.getColumns().add(colInc); 
                    
        for (int i = 1; i <= lastDay; i++) {
            final int finalIdx = i + 4;
            
            TableColumn<ObservableList<StringProperty>, String> col = createColumn(finalIdx, Integer.toString(i));
            col.setResizable(false);

            selCal.set(Calendar.DAY_OF_MONTH, i);
            int  day = selCal.get(Calendar.DAY_OF_WEEK);
 
            TableColumn<ObservableList<StringProperty>, String> colI = createColumn(finalIdx, weekDay[day]);
            colI.setPrefWidth(35);
            colI.setMaxWidth(35);
            colI.setResizable(false);
 
            col.getColumns().add(colI);           

            if (day == 7 || day == 1){
                colI.setStyle(
                        "-fx-background-color:#eeeeee;"
                        + "-fx-border-color:#dddddd;"
                        + "-fx-alignment: CENTER;"
                );
            }
            else{
                colI.setStyle("-fx-alignment: CENTER;");    
            }
            
            tableView.getColumns().add(col);
            tableView.setEditable(true);
        } 
  
    row.add(new SimpleStringProperty("Black Belt"));
    row.add(new SimpleStringProperty("Noris, Chuck"));
    row.add(new SimpleStringProperty("1"));
    row.add(new SimpleStringProperty("2"));
    row.add(new SimpleStringProperty("3"));

    tableView.getItems().add(row);

    } 
    
    private int convertMonth(String month){
   
        String[] monthArry = new String[] {"January", "Febuary", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"}; 
        int i = 0;
        for(String m : monthArry){
            
            if (m.equals(month))
                return i;
            i++;
        }
        
        return 0;
    }
 
    private TableColumn<ObservableList<StringProperty>, String> createColumn(
        final int columnIndex, String columnTitle) {
        
        TableColumn<ObservableList<StringProperty>, String> column = new TableColumn<>();

        column.setText(columnTitle);
        column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<StringProperty>, String>, ObservableValue<String>>() {
             @Override
             public ObservableValue<String> call(
                 CellDataFeatures<ObservableList<StringProperty>, String> cellDataFeatures) {
               ObservableList<StringProperty> values = cellDataFeatures.getValue();
               // Pad to current value if necessary:
               for (int index = values.size(); index <= columnIndex; index++) {
                   values.add(index, new SimpleStringProperty(""));
               }
               return cellDataFeatures.getValue().get(columnIndex);
             }
           });
        column.setCellFactory(TextFieldTableCell.<ObservableList<StringProperty>>forTableColumn());
        return column;
     }
    
}

