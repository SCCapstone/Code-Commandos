package dutyroster;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicLong;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;


public class MainController implements Initializable {
    
    
    public static String rosterName;
    
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
    @FXML private Button bAddRoster, bSave;
    
    private Tab currentDragTab;
    private static final AtomicLong idGenerator = new AtomicLong();
    private final String draggingID = "DraggingTab-"+idGenerator.incrementAndGet() ;
    private int dragIndex,dropIndex;
    
    private ObservableList<String> monthList = FXCollections.observableArrayList();
    private ObservableList<Integer> yearList = FXCollections.observableArrayList();
    private ObservableList<ObservableList<String>> rosterList = FXCollections
            .observableArrayList();
    private Scene sceneStatus;
   
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
       
        addSupport(rosterTabs);
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
                    createTab(roster.getTitle());
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
        fTitle.setOnKeyTyped(e -> {bSave.setDisable(false);});
        fInterval.setOnKeyTyped(e -> {bSave.setDisable(false);});
        fAmount.setOnKeyTyped(e -> {bSave.setDisable(false);});
        cWeekends.setOnMouseClicked(e -> {bSave.setDisable(false);});
        cHolidays.setOnMouseClicked(e -> {bSave.setDisable(false);});
     
        fInterval.setTooltip(new Tooltip("The interval (in hours) for each shift"));             
        fAmount.setTooltip(new Tooltip("The total number of employees assigned to each shift"));
        cWeekends.setTooltip(new Tooltip("Check this to keep a separate rototion for weekends"));
        cHolidays.setTooltip(new Tooltip("Check this to keep a separate rototion for holidays"));
 
    }    
 
    @FXML public void saveFields(){
        
        
        currentRoster.setInterval(Integer.parseInt(fInterval.getText()));
        currentRoster.setAmount(Integer.parseInt(fAmount.getText()));
        currentRoster.setWeekends(cWeekends.isSelected());
        currentRoster.setHolidays(cHolidays.isSelected());
        
        
        if (!fTitle.getText().equals(currentRoster.getTitle())){
            currentRoster.setTitle(fTitle.getText());
        
            Tab newTab = new Tab(fTitle.getText());
            Tab currentTab = rosterTabs.getSelectionModel().getSelectedItem();
            int newIndex = rosterTabs.getSelectionModel().getSelectedIndex();
            rosterTabs.getTabs().remove(currentTab);
            rosterTabs.getTabs().add(newIndex, newTab);
            rosterTabs.getSelectionModel().select(newTab);
        }
        
       
        bSave.setDisable(true);
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
         stage.initModality(Modality.APPLICATION_MODAL);
         stage.setScene(new Scene(root1));
         stage.setOnHidden(e -> eController.shutDown());
         stage.show(); 
          
        }
        catch(IOException e){
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
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setScene(sceneRank);
            stage.setOnHidden(e -> eController.shutDown());
            stage.show(); 
          
        }
        catch(IOException e){
           System.out.println("Can't load new scene: " + e); 
        }
    }  
    
    @FXML private void openStatusEditor(ActionEvent event) {
         
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StatusFXML.fxml")); 
            Parent root1 = loader.load();
            StatusController eController = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Status Editor");
            stage.setResizable(false);
            Scene sceneStatus2 = new Scene(root1);
            //sceneRank.getStylesheets().add("stylesheet.css");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneStatus2);
            stage.setOnHidden(e -> eController.shutDown());
            stage.show(); 
          
        }
        catch(IOException e){
           System.out.println("Can't load new scene: " + e); 
        }
    }  

    @FXML private void openCrewEditor(ActionEvent event) {
         
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CrewFXML.fxml")); 
            Parent root1 = loader.load();
            CrewController controller = new CrewController();
            loader.setController(controller);
            Stage stage = new Stage();
            stage.setTitle("Assign members to " + currentRoster.getTitle());
            stage.setResizable(false);
           
            Scene sceneCrew = new Scene(root1);
            //sceneRank.getStylesheets().add("stylesheet.css");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneCrew);
            stage.setOnHidden(e -> controller.shutDown());
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
        column.setCellValueFactory((CellDataFeatures<ObservableList<StringProperty>, String> cellDataFeatures) -> {
            ObservableList<StringProperty> values = cellDataFeatures.getValue();
            // Pad to current value if necessary:
            for (int index = values.size(); index <= columnIndex; index++) {
                values.add(index, new SimpleStringProperty(""));
            }
            return cellDataFeatures.getValue().get(columnIndex);
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
        String title = "Roster " + ( nextPriority + 1);
        
        int i = 1;
        while (tabTitleExists(title)) {
          title =  "Roster " + ( nextPriority + 1) + "_" + (i++);  
        }
        
        rosterArray.add(new Roster(title));
        createTab(title);
    }

    public void  createTab(String title){  

        Tab tab = new Tab(title);
        tab.setOnCloseRequest(new EventHandler<Event>(){
            @Override
            public void handle(Event ev) {
                Alert alert;
                alert = new Alert(AlertType.WARNING, 
                        "This will permanently remove " + currentRoster.getTitle() + "."
                                + " Do you wish to continue?",
                        ButtonType.YES,
                        ButtonType.NO);
                alert.setTitle("Remove " + currentRoster.getTitle());
              
                //Deactivate Defaultbehavior for yes-Button:
                Button yesButton = (Button) alert.getDialogPane().lookupButton( ButtonType.YES );
                yesButton.setDefaultButton( false );

                //Activate Defaultbehavior for no-Button:
                Button noButton = (Button) alert.getDialogPane().lookupButton( ButtonType.NO );
                noButton.setDefaultButton( true );
              
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.YES) {
                    int index = rosterTabs.getSelectionModel().getSelectedIndex();
                  deleteRoster(index);
                  if(rosterArray.isEmpty())
                        rosterControls.setVisible(false);
                }
                else{
                    ev.consume();
                }  
            }
        });
        Tooltip tip = new Tooltip("Set roster priority by dragging tabs. The farest left tab has the highest priority");
        tab.setTooltip(tip);
        rosterTabs.getTabs().add(tab);
        rosterTabs.getSelectionModel().select(tab);
        int index = rosterTabs.getSelectionModel().getSelectedIndex();
        selectedRoster(index); 
    }
   
    public boolean tabTitleExists(String chkTitle){
        
        for(int i = 0; i < rosterArray.size(); i++) 
            if(chkTitle.equals(rosterArray.get(i).getTitle()))
                return true;
        return false;

    }
    
    public void deleteRoster(int index){
      
       rosterArray.remove(index);
       selectedRoster(index - 1);

    }
 
    public void selectedRoster(int xVal) {
        
        currentRoster = rosterArray.get(xVal);
        rosterName = currentRoster.getTitle();
        rosterControls.setVisible(true);
        fTitle.setText(rosterName);
        fInterval.setText(Integer.toString(currentRoster.getInterval()));
        fAmount.setText(Integer.toString(currentRoster.getAmount()));
        cWeekends.setSelected(currentRoster.getWeekends());
        cHolidays.setSelected(currentRoster.getHolidays());
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
                        Boolean.parseBoolean(bArry[3]),
                        Boolean.parseBoolean(bArry[4])    
                        ) 
                    
                    );
                    
                }
            
            }
            
        }

    }
  
     //Converting store data into an array string
    public void storeData(){

        strData = "";
        rosterArray.forEach((roster) -> {  
            strData +=  roster.getTitle() 
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
            strData = Tools.removeLastChar(strData);
        
        //Store string array into secure file
        sc.store(strData);
        
        //clear strData
        strData = "";
        
    }   

     public void addSupport(TabPane tabPane) {
        
        tabPane.getTabs().forEach(this::addDragHandlers);
        tabPane.getTabs().addListener((ListChangeListener.Change<? extends Tab> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach(this::addDragHandlers);
                }
                if (c.wasRemoved()) {
                    c.getRemoved().forEach(this::removeDragHandlers);
                }
            }
        });

         tabPane.setOnDragOver(e -> {
            if (draggingID.equals(e.getDragboard().getString()) && 
                    currentDragTab != null &&
                    currentDragTab.getTabPane() != tabPane) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
        });
        
        tabPane.setOnDragDropped(e -> {
            if (draggingID.equals(e.getDragboard().getString()) && 
                    currentDragTab != null &&
                    currentDragTab.getTabPane() != tabPane) {

                currentDragTab.getTabPane().getTabs().remove(currentDragTab);
                tabPane.getTabs().add(currentDragTab);
                currentDragTab.getTabPane().getSelectionModel().select(currentDragTab);
            }
        });
    }   
    
    private void addDragHandlers(Tab tab) {

        // move text to label graphic:
        if (tab.getText() != null && ! tab.getText().isEmpty()) {
            Label label = new Label(tab.getText(), tab.getGraphic());
            tab.setText(null);
            tab.setGraphic(label);
        }

        Node graphic = tab.getGraphic();
        graphic.setOnDragDetected(e -> {
            Dragboard dragboard = graphic.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(draggingID);
            dragboard.setContent(content);
            dragboard.setDragView(graphic.snapshot(null, null));
            currentDragTab = tab ;
           
        });
        graphic.setOnDragOver(e -> {
            if (draggingID.equals(e.getDragboard().getString()) && 
                    currentDragTab != null &&
                    currentDragTab.getGraphic() != graphic) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
        });
        graphic.setOnDragDropped(e -> {
            if (draggingID.equals(e.getDragboard().getString()) && 
                    currentDragTab != null &&
                    currentDragTab.getGraphic() != graphic) {
                
                //Need both of these to do the swap;
                dragIndex = tab.getTabPane().getTabs().indexOf(currentDragTab);
                dropIndex = tab.getTabPane().getTabs().indexOf(tab);
                Roster tmpRoster = rosterArray.get(dragIndex);
                
                currentDragTab.getTabPane().getTabs().remove(currentDragTab);
                tab.getTabPane().getTabs().add(dropIndex, currentDragTab);

                if (Math.abs(dragIndex - dropIndex)==1)
                    Collections.swap(rosterArray, dragIndex, dropIndex);
                else 
                    if (dragIndex < dropIndex)
                        for (int i = dragIndex; i < dropIndex; i++)
                            Collections.swap(rosterArray, i, (i+1) );         
                    else
                        for (int i = dragIndex; i > dropIndex; i--)
                            Collections.swap(rosterArray, i, (i-1) );    
                
                currentDragTab.getTabPane().getSelectionModel().select(currentDragTab);
                selectedRoster(dropIndex);
            }
        });
        graphic.setOnDragDone(e -> {   
            dragIndex=0;
            dropIndex=0;
            currentDragTab = null;
        });
    }

    private void removeDragHandlers(Tab tab) {
        tab.getGraphic().setOnDragDetected(null);
        tab.getGraphic().setOnDragOver(null);
        tab.getGraphic().setOnDragDropped(null);
        tab.getGraphic().setOnDragDone(null);
    }
}
   


