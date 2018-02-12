package dutyroster;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;


public class MainController implements Initializable {
    
    @FXML private TableView<ObservableList<StringProperty>> tableView = new TableView<>();
    @FXML private ComboBox comboMonth;
    @FXML private ComboBox comboYear;
    @FXML private TextField fTitle;
    @FXML private TextField fInterval;
    @FXML private TextField fAmount;
    @FXML private CheckBox cWeekends;
    @FXML private CheckBox cHolidays;
    
    @FXML private TabPane rosterTabs;
    @FXML private HBox rosterControls;
    @FXML private Button bAddRoster;
    
    private ObservableList<String> monthList = FXCollections.observableArrayList();
    private ObservableList<Integer> yearList = FXCollections.observableArrayList();
    private ObservableList<ObservableList<String>> rosterList = FXCollections
            .observableArrayList();
   
    private ArrayList<Roster> rosterArray = new ArrayList<>(); 
    private Roster currentRoster = new Roster();
    //Extracting Data from encrypted file
    private SecureFile sc;
    private String strData;
    
    public MainController(){
        startUp();   
    }
    
    public void startUp(){
        //instantiates new file, use file name Ranks as storage
        sc = new SecureFile("Rosters");
        retrieveData();
    }  
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Calendar now = Calendar.getInstance();
        int curYear = now.get(Calendar.YEAR);
        int curMonth = now.get(Calendar.MONTH);          

        loadMonths(curMonth);
        loadYears(curYear);
        setDate(curYear, curMonth);
        
        bAddRoster.setTooltip(new Tooltip("Click here to add a new roster"));

        rosterControls.setVisible(false);
        if (rosterArray.size() > 0){
            rosterArray.forEach((roster) -> {
                    createTab(roster.getTitle(), roster.getPriority());
            });
            rosterControls.setVisible(true);
            SingleSelectionModel<Tab> selectionModel = rosterTabs.getSelectionModel();
            selectionModel.select(0);
            selectedRoster(0);
        }
        
         rosterTabs.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Tab>() {
                @Override
                public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {

                    if (t1!=null){
       
                        int id = rosterTabs.getSelectionModel().getSelectedIndex();
                        selectedRoster(id);

                    }
                    
                }
            
            }
        
        );
         
        //Form Controls
        
        fTitle.textProperty().addListener((observable, oldValue, newValue) -> {
             currentRoster.setTitle(newValue);
             rosterTabs.getSelectionModel().getSelectedItem().setText(newValue);
            }
        );
        
        fInterval.setTooltip(new Tooltip("The interval (in hours) for each shift"));
        fInterval.textProperty().addListener((observable, oldValue, newValue) -> {
             currentRoster.setInterval(Integer.parseInt(newValue));
            }
        );  
        
        fAmount.setTooltip(new Tooltip("The total number of employees assigned to each shift"));
        fAmount.textProperty().addListener((observable, oldValue, newValue) -> {
             currentRoster.setAmount(Integer.parseInt(newValue));
            }
        );  
        
        cWeekends.setTooltip(new Tooltip("Check this to keep a separate rototion for weekends"));
        cWeekends.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
                Boolean oldValue, Boolean newValue) {
                currentRoster.setWeekends(newValue);
            }
        });
        
        cWeekends.setTooltip(new Tooltip("Check this to keep a separate rototion for holidays"));
        cHolidays.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
                Boolean oldValue, Boolean newValue) {
                currentRoster.setHolidays(newValue);
            }
        });

    }    
 
    public void shutDown() {  
        storeData();
    }  
    
    @FXML private void exitProgram(ActionEvent event) {   
    System.exit(0);
    }

    @FXML private void openEmployeeEditor(ActionEvent event) {
         
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
    
    @FXML private void openRankEditor(ActionEvent event) {
         
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
  
    /*
    row.add(new SimpleStringProperty("Black Belt"));
    row.add(new SimpleStringProperty("Noris, Chuck"));
    row.add(new SimpleStringProperty("1"));
    row.add(new SimpleStringProperty("2"));
    row.add(new SimpleStringProperty("3"));

    tableView.getItems().add(row);
    */
    
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
    
    //Added for Roster operations
    @FXML public void addRoster(ActionEvent event) {
        newRoster();
    }
    
    public void newRoster() {
        int nextPriority = rosterArray.size();
        rosterArray.add(new Roster("Roster " + ( nextPriority + 1), nextPriority));
        createTab("Roster " + (nextPriority + 1), nextPriority);
    }

    public void  createTab(String title, int priority){
        Tab tab = new Tab();
        tab.setText(title);
        tab.setOnCloseRequest(new EventHandler<Event>(){
            @Override
            public void handle(Event ev) 
            {
                Alert alert;
                alert = new Alert(AlertType.WARNING, 
                        "This will permanently remove " + currentRoster.getTitle() + "."
                                + " Do you wish to continue?",
                        ButtonType.YES,
                        ButtonType.NO);
              alert.setTitle("Remove " + currentRoster.getTitle());
              Optional<ButtonType> result = alert.showAndWait();
              if (result.get() == ButtonType.YES) {
                  deleteRoster(currentRoster.getPriority());
                  if(rosterArray.isEmpty())
                        rosterControls.setVisible(false);
              }
              else{
                  ev.consume();
              }  
           }
        });
        
        rosterTabs.getTabs().add(tab);
        rosterTabs.getSelectionModel().select(tab);
        selectedRoster(priority); 
    }
    
    public void deleteRoster(int index){
       int count = rosterArray.size();
       rosterArray.remove(index);
       updatePriority();
       selectedRoster(index - 1);

    }
 
    public void updatePriority(){
        
        for(int i = 0; i < rosterArray.size(); i++) 
                rosterArray.get(i).setPriority(i);
              
        /*int i = 0;
        for (Tab tab : rosterTabs.getTabs()) 
	tab.setId(Integer.toString(i++));
        */
    }
    
    public void setCurrentRoster(){
        if (currentRoster==null)
            return;
        
        rosterControls.setVisible(true);
        
        fTitle.setText(currentRoster.getTitle());
        fInterval.setText(Integer.toString(currentRoster.getInterval()));
        fAmount.setText(Integer.toString(currentRoster.getAmount()));
        cWeekends.setSelected(currentRoster.getWeekends());
        cHolidays.setSelected(currentRoster.getHolidays());
    
    }
    
    public void selectedRoster(int xVal) {
       
        for(int i = 0; i < rosterArray.size(); i++) 
            if(rosterArray.get(i).getPriority() == xVal){
                currentRoster = rosterArray.get(i);
                setCurrentRoster();
                return;
            }
    }
    
    //retrieve data from secure file
    public void retrieveData(){
        
        String a = sc.retrieve();
      
        String aArry[] = a.split("\\|", -1);
        for (String b : aArry){
            
            if (b.length() > 2){
                
                String bArry[] = b.split("\\@", -1);
                
                if(bArry[0].length() > 0 && bArry[1].length() > 0) {
                    rosterArray.add( 
                        new Roster(
                        bArry[0],
                        Integer.parseInt(bArry[1]),
                        Integer.parseInt(bArry[2]),
                        Integer.parseInt(bArry[3]),
                        Boolean.parseBoolean(bArry[4]),
                        Boolean.parseBoolean(bArry[5])    
                        ) 
                    
                    );
                    
                }
            
            }
            
        }

    }
  
     //Converting store data into an array string
    public void storeData(){
        
        Tools tools = new Tools();
        strData = "";
        rosterArray.forEach((roster) -> {  
            strData +=  roster.getTitle() 
                    + "@" 
                    +  roster.getPriority() 
                    + "@" 
                    +  roster.getInterval() 
                    + "@"  
                    +  roster.getAmount() 
                    + "@" 
                    +  roster.getWeekends()
                    + "@" 
                    +  roster.getHolidays() 
                    + "|";    
        });
            strData = tools.removeLastChar(strData);
        
        //Store string array into secure file
        sc.store(strData);
        
        //clear strData
        strData = "";
        
    }   

}

