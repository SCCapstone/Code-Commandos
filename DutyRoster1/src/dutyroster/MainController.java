/**
 * This is the main screen of the program. Plus, it handles roster data
 * @author Othen Prock, Michael Harlow
 * @version 10, 3/27/2018
 */
package dutyroster;

import com.itextpdf.text.DocumentException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.util.StringConverter;

public class MainController implements Initializable {
      
    public static String rosterName = "";
    
    @FXML private TableView<ObservableList<StringProperty>>tableView;
    @FXML private ComboBox comboMonth;
    @FXML private ComboBox comboYear;
    @FXML private ComboBox<Interval> cDInterval;
    @FXML private ComboBox<Interval> cRInterval;
    @FXML private Label lowerOutput;
    @FXML private TextField fTitle;
    @FXML private TextField fDInterval;
    @FXML private TextField fRInterval;
    @FXML private TextField fAmount;
    @FXML private CheckBox cWeekends;
    @FXML private CheckBox cHolidays;
    @FXML private TabPane rosterTabs;
    @FXML private HBox rosterControls;
    @FXML private Button bAddRoster, bSave;
    
    private Tab currentDragTab;
    private static final AtomicLong ID = new AtomicLong();
    private final String draggingID = "DraggingTab-"+ID.incrementAndGet();
    private int dragIndex,dropIndex;
    private int lastDayOfMonth,curMonth,curYear;
    private static final String DATE_FORMAT = "d MMM uuuu";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
           
    private final ObservableList<String> monthList = FXCollections.observableArrayList();
    private final ObservableList<Integer> yearList = FXCollections.observableArrayList();
    private final ObservableList<Interval> intervalList = FXCollections.observableArrayList();
    private final ObservableList<ObservableList<StringProperty>> rowData = FXCollections.observableArrayList(); 
   
    private final ArrayList<Roster> rosterArray = new ArrayList(); 
    private final ArrayList<Holiday> holidayArray = new ArrayList(); 
    private final ArrayList<Status> statusArray = new ArrayList();
    private final ArrayList<Blockout> blockoutArray = new ArrayList();
    private Roster currentRoster = new Roster();
    //Extracting Data from encrypted file

    private String strData;
    private boolean updateLock;
    
    public MainController(){
        startUp();   
    }
    
    public void startUp(){
        tableView = new TableView();
        //instantiates new file, use file name Ranks as storage
        
        retrieveData();
        
        if(!rosterArray.isEmpty())
        currentRoster = rosterArray.get(0);
    }  
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        tableView.setItems(rowData);
        addSupport(rosterTabs);
        Calendar now = Calendar.getInstance();
        int tmpMonth = now.get(Calendar.MONTH);          
        int tmpYear = now.get(Calendar.YEAR);
        
        loadMonths(tmpMonth);
        loadYears(tmpYear);
        loadHolidays();
        loadBlockouts();
        loadStatusData();
        updateCrew();
        
        setDate(tmpYear, tmpMonth);
        
        tableView.setStyle("-fx-selection-bar:#cccccc;");
        
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
        
        rosterTabs.getSelectionModel().selectedItemProperty().addListener((
        ObservableValue<? extends Tab> ov, Tab t, Tab t1) -> {
                if (t1!=null){
                 int id = rosterTabs.getSelectionModel().getSelectedIndex();
                 selectedRoster(id); 
                }
        });
         
        intervalList.addAll(
                new Interval(1, "1 hour"), 
                new Interval(2, "2 hours"),
                new Interval(3, "3 hours"),
                new Interval(4, "4 hours"),
                new Interval(6, "6 hours"),
                new Interval(8, "8 hours"),
                new Interval(12, "12 hours"),
                new Interval(24, "1 day")
        );
       
        cDInterval.setItems(intervalList); 
        cDInterval.setConverter(new StringConverter<Interval>() {

            @Override
            public String toString(Interval object) {
                return object.getTitle();
            }

            @Override
            public Interval fromString(String s) {
                return (Interval) cDInterval.getItems().stream().filter(ap -> 
                    ap.getTitle().equals(s)).findFirst().orElse(null);
            }
        });      
        cDInterval.valueProperty().addListener((ObservableValue<? extends Interval> obs, Interval oldval, Interval newval) -> {
            if(newval != null)
                fDInterval.setText(Integer.toString(newval.getHour()));
                currentRoster.setDInterval(newval.getHour());
                bSave.setDisable(false);
        });
        
        setDInterval();
        
        cRInterval.setItems(intervalList); 
        cRInterval.setConverter(new StringConverter<Interval>() {

            @Override
            public String toString(Interval object) {
                return object.getTitle();
            }

            @Override
            public Interval fromString(String s) {
                return (Interval) cRInterval.getItems().stream().filter(ap -> 
                    ap.getTitle().equals(s)).findFirst().orElse(null);
            }
        });      
        cRInterval.valueProperty().addListener((ObservableValue<? extends Interval> obs, Interval oldval, Interval newval) -> {
            if(newval != null)
                fRInterval.setText(Integer.toString(newval.getHour()));
                currentRoster.setRInterval(newval.getHour());
                bSave.setDisable(false);
        });

       setRInterval();
        
        //Form Controls
        fTitle.setOnKeyTyped(e -> {bSave.setDisable(false);});
        
        fAmount.setOnKeyTyped(e -> {bSave.setDisable(false);});
        cWeekends.setOnMouseClicked(e -> {bSave.setDisable(false);});
        cHolidays.setOnMouseClicked(e -> {bSave.setDisable(false);});
     
        fDInterval.setTooltip(new Tooltip("The interval (in hours) for each shift"));             
        fAmount.setTooltip(new Tooltip("The total number of employees assigned to each shift"));
        cWeekends.setTooltip(new Tooltip("Check this to keep a separate rototion for weekends"));
        cHolidays.setTooltip(new Tooltip("Check this to keep a separate rototion for holidays"));
 
         //Set save to false at startup
        bSave.setDisable(true);
    }    
 
    public void setDInterval(){
        
        int selected = -1;

        for(int i = 0; i < intervalList.size(); i++)
            if (!fDInterval.getText().isEmpty() && intervalList.get(i).getHour() == Integer.parseInt(fDInterval.getText()) )
                selected = i;

        if(selected >= 0)
            cDInterval.getSelectionModel().select(selected);
    }
    
    public void setRInterval(){
        
        int selected = -1;

        for(int i = 0; i < intervalList.size(); i++)
            if (!fRInterval.getText().isEmpty() && intervalList.get(i).getHour() == Integer.parseInt(fRInterval.getText()) )
                selected = i;

        if(selected >= 0)
            cRInterval.getSelectionModel().select(selected);
    }
    
    @FXML public void saveFields(){
        
        currentRoster.setDInterval(Integer.parseInt(fDInterval.getText()));
        currentRoster.setAmount(Integer.parseInt(fAmount.getText()));
        
        boolean updateCal = false;
        if (currentRoster.getWeekends()!= cWeekends.isSelected()
            || currentRoster.getHolidays()!= cHolidays.isSelected()){
                updateCal = true;
        }
        
        currentRoster.setWeekends(cWeekends.isSelected());
        currentRoster.setHolidays(cHolidays.isSelected());    
        
        String newTitle = fTitle.getText();
        String oldTitle = currentRoster.getTitle();
        
        if (!newTitle.equals(oldTitle)){

            currentRoster.setTitle(newTitle);           
            Tab newTab = new Tab(newTitle);
            Tab currentTab = rosterTabs.getSelectionModel().getSelectedItem();
            int newIndex = rosterTabs.getSelectionModel().getSelectedIndex();
            rosterTabs.getTabs().remove(currentTab);
            rosterTabs.getTabs().add(newIndex, newTab);
            renameData(oldTitle, newTitle);
            rosterTabs.getSelectionModel().select(newTab);

        }
        
        if (updateCal){
            setDate(curYear, curMonth-1);
        }
        
              
        bSave.setDisable(true);
    }
    
    public void shutDown() {  
        storeRosterData();
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
            stage.setTitle("Grade Editor");
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
           
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneStatus2);
            stage.setOnHidden(e -> eController.shutDown());
            stage.show(); 
          
        }
        catch(IOException e){
           System.out.println("Can't load new scene: " + e); 
        }
    } 
      
    @FXML private void openBlockout(ActionEvent event) {
         
        try{
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BlockoutFXML.fxml")); 
            Parent root1 = loader.load();
            BlockoutController BOController = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Blockout Editor");
            stage.setResizable(false);
            Scene sceneStatus2 = new Scene(root1);
            
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneStatus2);
            stage.setOnHidden(e -> {
                BOController.shutDown();
                loadBlockouts();
                updateCrew();
                    });
            stage.show(); 
        }
        catch(IOException e){
           System.out.println("Can't load new scene: " + e); 
        }
    }  
    
    @FXML private void openHolidays(ActionEvent event) {
         
        try{
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HolidayFXML.fxml")); 
            Parent root1 = loader.load();
            HolidayController HController = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Holidays Editor");
            stage.setResizable(false);
            Scene sceneStatus2 = new Scene(root1);
           
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneStatus2);
            stage.setOnHidden(e -> {
                HController.shutDown();
                loadHolidays();
                setDate(curYear, curMonth-1);
                }
            );
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
            CrewController eController = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Assign members to " + currentRoster.getTitle());
            stage.setResizable(false);
            Scene sceneCrew = new Scene(root1);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(sceneCrew);
            stage.setOnHidden(e -> {
                eController.shutDown();
                updateCrew();
                    });
            stage.show(); 
        }
        catch(IOException e){
           System.out.println("Can't load new scene: " + e); 
        }
    }    
    
    @FXML private void openSettings(ActionEvent event) {
         
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingsFXML.fxml")); 
            Parent root1 = loader.load();
           // CrewController eController = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Settings Editor");
            stage.setResizable(false);
            Scene scene = new Scene(root1);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.show(); 
        }
        catch(IOException e){
           System.out.println("Can't load new scene: " + e); 
        }
    }  
    
    @FXML private void assignDuty(){
        
        storeData();
        storeRosterData();
        
        Assignment assign = new Assignment(rosterArray,curYear,curMonth);
        assign.setAssingned();
        
        updateCrew();
    }
    
    public void loadMonths(int curMonth){
    
        String[] monthArry = new String[] {"January", "Febuary", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"}; 
 
        monthList.addAll(Arrays.asList(monthArry));        
        comboMonth.getItems().setAll(monthList);
        comboMonth.getSelectionModel().select(curMonth);
    }
 
    private void loadYears(int curYear) {
        
        for(int i = curYear - 1; i < curYear + 4; i++)
            yearList.add(i);
    
        comboYear.getItems().setAll(yearList);
        comboYear.getSelectionModel().select(Integer.toString(curYear));
    }
    
    @FXML public void printDA6(ActionEvent event){
        
        PDF da6 = new PDF(rowData,currentRoster.getTitle(),curYear,curMonth);
        
        try {
            da6.makePDF();
        } catch (DocumentException | IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
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

       comboMonth.getSelectionModel().select(month);
       comboYear.getSelectionModel().select(Integer.toString(year));

    }
     
    private void setDate(int intYear, int intMonth){
 
        String[] weekDay = new String[] {null, "SU", "MO", "TU", "WE", "TH", "FR", "SA"};
        
        Calendar selCal = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        
        //!!!Before switching to another month, store the current data!
        storeRosterData();
        //========================================
        
        selCal.set(intYear, intMonth, 1);
        lastDayOfMonth = selCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        curMonth = intMonth+1;//actual months starts at 0
        curYear = intYear;
        
        tableView.getColumns().clear();
 
        TableColumn colRank = createColumn(0, "Grade");
        colRank.setPrefWidth(150);
        colRank.setMaxWidth(300);
        colRank.setEditable(false);
        colRank.setSortable(false);
        tableView.getColumns().add(colRank);
        
        TableColumn colName = createColumn(1, "Name");
        colName.setPrefWidth(300);
        colName.setMaxWidth(600);
        colName.setEditable(false);
        colName.setSortable(false);
        tableView.getColumns().add(colName);
      
        TableColumn colN1 = createColumn(2, "");
        colN1.setMaxWidth(0.01);
        colN1.setEditable(false);
        colN1.setSortable(false);
        tableView.getColumns().add(colN1);  

        TableColumn colW1 = createColumn(3, "");
        colW1.setMaxWidth(0.01);
        colW1.setEditable(false);
        colW1.setSortable(false);
        tableView.getColumns().add(colW1); 

        TableColumn colH1 = createColumn(4, "");
        colH1.setMaxWidth(0.01);
        colH1.setEditable(false);
        colH1.setSortable(false);
        tableView.getColumns().add(colH1); 
        
        TableColumn colInc = createColumn(5, "Increments");
        colInc.setPrefWidth(150);
        colInc.setMaxWidth(150);
        colInc.setResizable(false);
        colInc.setSortable(false);
        colInc.setStyle("-fx-background-color:#ffffff;");
  
            TableColumn colN = createColumn(5, "N");
            colN.setPrefWidth(35);
            colN.setMaxWidth(35);
            colN.setResizable(false);
            colN.setSortable(false);
            colN.setStyle("-fx-alignment: CENTER;");
            colInc.getColumns().add(colN);  
                      
            TableColumn colW = createColumn(6, "W");
            colW.setPrefWidth(35);
            colW.setMaxWidth(35);
            colW.setResizable(false);
            colW.setSortable(false);
            colW.setStyle("-fx-alignment: CENTER;-fx-background-color:#dddddd;"
                        + "-fx-border-color:#ffffff;");
            colInc.getColumns().add(colW);
           
            TableColumn colH = createColumn(7, "H");
            colH.setPrefWidth(35);
            colH.setMaxWidth(35);
            colH.setResizable(false);
            colH.setSortable(false);
            colH.setStyle("-fx-alignment: CENTER;-fx-background-color:#ddeedd;"
                        + "-fx-border-color:#ffffff;");
            colInc.getColumns().add(colH); 

        tableView.getColumns().add(colInc); 
                    
        for (int i = 1; i <= lastDayOfMonth; i++) {
            final int finalIdx = i + 7;
            
            TableColumn col = createColumn(finalIdx, Integer.toString(i));
            col.setResizable(false);

            //Get current day of the month
            selCal.set(Calendar.DAY_OF_MONTH, i);
            //Get current day of the week
            int  day = selCal.get(Calendar.DAY_OF_WEEK);
            
            if (selCal.equals(now)){
            col.setStyle("-fx-alignment: CENTER;-fx-background-color:#ffffff;");
            }
            
            TableColumn colI = createColumn(finalIdx, weekDay[day]);
            colI.setPrefWidth(35);
            colI.setMaxWidth(35);
            colI.setEditable(false);
            colI.setResizable(false);
            colI.setSortable(false);
 
            col.getColumns().add(colI);           

            String isHol = isHoliday(selCal);
            
            //Check for Satudays and Sundays
            

            if ( !isHol.isEmpty() && currentRoster.getHolidays()){
                
                Tooltip tip = new Tooltip(isHol);
                
                //Clear normal date colum headers to show tool tip label with
                //Holiday title
                Label dayM = new Label(Integer.toString(i));
                dayM.setTooltip(tip);
                col.setText("");
                col.setGraphic(dayM);
                Label dayW = new Label(weekDay[day]);
                dayW.setTooltip(tip);
                colI.setText("");
                colI.setGraphic(dayW);
                
                colI.setStyle(
                        "-fx-background-color:#ddeedd;"     
                        + "-fx-alignment: CENTER;"
                         + "-fx-border-color:#ffffff;"
                );
            }
            else if ( currentRoster.getWeekends() && (day == 7 || day == 1 ) ){
                
                colI.setStyle(
                        "-fx-background-color:#dddddd;"       
                        + "-fx-alignment: CENTER;"
                         + "-fx-border-color:#ffffff;"
                );
            }
            else{
                colI.setStyle("-fx-alignment: CENTER;"); 
            }
            
            tableView.getColumns().add(col);
            tableView.setEditable(true);
               
        }
        
    updateCrew();    

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

    public void updateCrew(){
        
        if(rosterName.isEmpty() || rosterArray.isEmpty() || updateLock)
            return;

        CrewController cController = new CrewController();

        SecureFile scCrews = new SecureFile("Crew_" + rosterName);      
        ArrayList<Employee> aCrews = cController.getCrewData(scCrews);
        ArrayList<String> rdArray = new ArrayList();
        String crewName;
        String pathName = "Crew_"+ rosterName+"_"+curYear+"_"+curMonth;
        //This will pull daily data for each roster member        
        RosterData rd = new RosterData();
        Calendar thisMonth = Calendar.getInstance();
       
        rowData.clear();
        for(Employee crew: aCrews){
            
            crewName = crew.getName();
            ObservableList<StringProperty> row = FXCollections.observableArrayList();
            row.add(new SimpleStringProperty(crew.getRank())); //Index 0
            row.add(new SimpleStringProperty(crewName)); //Index 1

            if (!crewName.isEmpty()){
                rdArray = rd.getRow(pathName,crewName);
            }
               
            if(rdArray==null || rdArray.isEmpty() ){
                
                for (int i = 2; i < lastDayOfMonth + 8; i++){
                    
                    if(i<8){
                        row.add(new SimpleStringProperty("1"));
                    }
                    else{
                        
                        thisMonth.set(curYear, curMonth-1, i-7);
                        //Load blockouts for this employee
                        String block = blockStatus(crewName, thisMonth); 
                        SimpleStringProperty fill;
                       
                        if (!block.isEmpty())
                            fill = new SimpleStringProperty(block);
                        else
                            fill = new SimpleStringProperty("_");
                        
                        row.add(fill); 
                    } 
                       
                }
                
            }
            else{
                for (int i = 2; i < lastDayOfMonth + 8; i++){
                    
                    if(i<8){
                        row.add(new SimpleStringProperty(rdArray.get(i)));
                    }
                    else{
                        
                        thisMonth.set(curYear, curMonth-1, i-7);
                        //Load blockouts for this employee
                        String block = blockStatus(crewName, thisMonth); 
                        SimpleStringProperty fill;
                       
                        if (!block.isEmpty())
                            fill = new SimpleStringProperty(block);
                        else
                            fill = new SimpleStringProperty(rdArray.get(i));
                        
                        row.add(fill); 
                    } 
                
                }
            
            }
            
            rowData.add(row);
        }
        
        tableView.setItems(null);
        tableView.setItems(rowData);
        tableView.refresh();

    }
   
    @FXML public void resetIncrements(){

        Calendar lastMonth = Calendar.getInstance();
        lastMonth.set(curYear, curMonth-1,1);
        RosterData rd = new RosterData();
        ArrayList<String> lastRdArray;
        int lastY = lastMonth.get(Calendar.YEAR);
        int lastM = lastMonth.get(Calendar.MONTH);
        String pathLastMonth = "Crew_"+ rosterName +"_"
                +Integer.toString(lastY)+"_"+Integer.toString(lastM);
  
        for (ObservableList<StringProperty> row : rowData) {
            lastRdArray = rd.getRow(pathLastMonth,row.get(1).get());
                       
            if( lastRdArray != null){
                row.set(5, new SimpleStringProperty( lastRdArray.get(2) )); //Index 2
                row.set(6, new SimpleStringProperty( lastRdArray.get(3) )); //Index 3
                row.set(7, new SimpleStringProperty( lastRdArray.get(4) )); //Index 4
                
            }
        
        }
        tableView.refresh();
    
    }   
    
    public void  createTab(String title){  

        Tab tab = new Tab(title);
        tab.setOnCloseRequest((Event ev) -> {
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
        });
        Tooltip tip = new Tooltip("Set roster priority by dragging tabs. The farest left tab has the highest priority");
        tab.setTooltip(tip);
        rosterTabs.getTabs().add(tab);
 
    }
   
    public boolean tabTitleExists(String chkTitle){
        
        for(int i = 0; i < rosterArray.size(); i++) 
            if(chkTitle.equals(rosterArray.get(i).getTitle()))
                return true;
        return false;

    }
    
    public void deleteRoster(int index){
    
        String removeName = currentRoster.getTitle();
        
       rosterArray.remove(index);
       int nextIndex = (index==0)? 0: index - 1;
     
        if(rosterArray.isEmpty()){
           rosterControls.setVisible(false);
           rowData.clear();
        }
        else{
            selectedRoster(nextIndex);
        }
 
        removeData(removeName);    
    }
 
    public void removeData(String fName){
        updateLock = true;
        File dir = new File(".");   
        File dir2 = new File(dir,SecureFile.DIR);
        File[] files = dir2.listFiles();
        for (File f : files) 
            if (f.getName().startsWith("Crew_"+fName)) 
                f.delete();
        
        updateLock = false;
    }
   
    public void renameData(String oldName, String newName){
       
        updateLock = true;
        String[][] tempArry = new String[5][2];
        String newFile;
        int a=0;
        
        File dir = new File(".");   
        File dir2 = new File(dir,SecureFile.DIR);
        File[] files = dir2.listFiles();
        for (File f : files){ 
            if (f.getName().startsWith("Crew_"+oldName)){
                String oldFile = f.getName();
               tempArry[a][0] = oldFile;
               String fArry[] = oldFile.split("\\_", -1);
               fArry[1] = newName;
                    newFile = "";
                    for(int i = 0; i < fArry.length; i++)
                        newFile += (i==0)? fArry[i] : "_" + fArry[i] ;
               tempArry[a++][1] = newFile;
            }
        }
        for (String[] address : tempArry){
            if(address[0]!=null){
                File source = new File(dir2,address[0]);
                File dest = new File(dir2,address[1]);
                
                try {
                    Files.copy(
                            source.toPath(),
                            dest.toPath(),
                            REPLACE_EXISTING);
                } catch (IOException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE,
                            null, ex + " copyig roster data.");
                }

            }
        }    
            
        updateLock = false;
        
        updateCrew();
        
        removeData(oldName);
  
    }
     
    public void selectedRoster(int xVal) {
        
        if(rosterArray.isEmpty())
            return;
 
        
         //Store roster data before it changes
        storeRosterData();       
        
        currentRoster = rosterArray.get(xVal);
        cWeekends.setSelected(currentRoster.getWeekends());
        cHolidays.setSelected(currentRoster.getHolidays());       
        setDate(curYear, curMonth-1);

        rosterName = currentRoster.getTitle();
        lowerOutput.setText(rosterName + " is set to priority " + (xVal+1) + ". Drag and drop tabs to change.");
        rosterControls.setVisible(true);
        fTitle.setText(rosterName);
        fDInterval.setText(Integer.toString(currentRoster.getDInterval()));
        setDInterval();
        fRInterval.setText(Integer.toString(currentRoster.getRInterval()));
        setRInterval();
        fAmount.setText(Integer.toString(currentRoster.getAmount()));

        updateCrew();
        

    }
    
    public void storeRosterData(){
        
        if(rosterName.isEmpty()) 
            return;
        if (rowData.isEmpty())
            return;
        if(updateLock)
            return;
        
        String pathName = "Crew_"+ rosterName+"_"+curYear+"_"+curMonth;
        //This will pull daily data for each roster member        
        RosterData dr = new RosterData();
        dr.storeData(pathName, rowData);
    }
   
    //retrieve data from secure file
    public void retrieveData(){
        SecureFile sc = new SecureFile("Rosters");
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
        SecureFile sc = new SecureFile("Rosters");
        strData = "";
        rosterArray.forEach((roster) -> {  
            strData +=  roster.getTitle() 
                    + "@" 
                    +  roster.getDInterval() 
                    + "@"  
                    +  roster.getRInterval() 
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

    public void loadStatusData(){
        
          if(!statusArray.isEmpty()) 
        statusArray.clear();
        
        SecureFile sc = new SecureFile("Status");
        String a = sc.retrieve();
      
        String aArry[] = a.split("\\|", -1);
        for (String b : aArry){
            
            if (b.length() > 2){
                
                String bArry[] = b.split("\\@", -1);
                
                if(bArry[0].length() > 0 && bArry[1].length() > 0){
                    boolean incs = ( bArry[2].equals("1") );
                    statusArray.add(  new Status(bArry[0], bArry[1], incs)  );  
                }
            }
        }
    }
    
    public void loadBlockouts(){
        
        if(!blockoutArray.isEmpty()) 
        blockoutArray.clear();
        
        SecureFile scBO = new SecureFile("Blockouts");
        String a = scBO.retrieve();
      
        String aArry[] = a.split("\\|", -1);
        for (String b : aArry){
            
            if (b.length() > 2){
                
                String bArry[] = b.split("\\@", -1);
                
                // getSortIndex pulls updated rank order index. 
                if(bArry[0].length() > 0 && bArry[1].length() > 0){
                    
                    blockoutArray.add( new Blockout(
                            bArry[0],
                            bArry[1],
                            bArry[2],
                            bArry[3]
                    ));
                }
            }    
        }           
    }  
    
    private String blockStatus(String employee, Calendar curDate){
           
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();    
        
        for (Blockout b : blockoutArray){  
            
            try {
                startDate.setTime( sdf.parse( b.getFromDate() ) );
                endDate.setTime( sdf.parse( b.getToDate() ) );
                endDate.add(Calendar.DATE, 1); 
            } catch (ParseException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            if (b.getName().equals(employee) && curDate.after(startDate) && curDate.before(endDate)){
                return lookupStatusCode(b.getStatus());
            }
                
        }    
        return "";
        
    }
    
    private String lookupStatusCode(String status){
        
        for (Status s : statusArray)
            if (s.getTitle().equals(status))
                return s.getCode();
        
        return "";
    }
    
    /**
     * This is used to load holidays from secure files into the link listing array.
     */
    public void loadHolidays(){
        SecureFile scEmployees = new SecureFile("Holidays");
        String a = scEmployees.retrieve();
      
        if(!holidayArray.isEmpty())
            holidayArray.clear();
        
        String aArry[] = a.split("\\|", -1);
        for (String b : aArry){
            
            if (b.length() > 2){
                
                String bArry[] = b.split("\\@", -1);
                
                if(bArry[0].length() > 0 && bArry[1].length() > 0){
                    holidayArray.add(  new Holiday(bArry[0],bArry[1],bArry[2])  );
                }
            
            }    
        
        }       
        
    }
    
    public String isHoliday (Calendar curDate){
        
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();   
      
        for(Holiday h : holidayArray){
            
            try {
                
                startDate.setTime( sdf.parse( h.getFromDate() ) );
                endDate.setTime( sdf.parse( h.getToDate() ) );
                endDate.add(Calendar.DATE, 1);
            } catch (ParseException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if ( curDate.after(startDate) && curDate.before(endDate) )
                return h.getName();
            
        }    

        return "";
    }
}
   