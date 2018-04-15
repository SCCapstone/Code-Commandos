/**
 * This class is a collection of functions to assign duty to employees
 * on rosters for a specific month
 * @author Othen Prock, Michael Harlow
 * @version 4, March 22, 2018
 */
package dutyroster;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public class Assignment {
    
    private final ArrayList<Roster> rosterArray = new ArrayList();
    private final ArrayList<Blockout> blockoutArray = new ArrayList();
    private final ArrayList<Status> statusArray = new ArrayList();
    private final ArrayList<ArrayList<ArrayList<String>>> crewsArray = new ArrayList();
    private final ArrayList<Holiday> holidayArray = new ArrayList();
    
    private final static String DUTY="///", DETAIL="D", REST="A", BLANK="";
    
    private final int year;
    private final int month;
    
    public Assignment(ArrayList<Roster> rArray, int inYear, int inMonth){
        year = inYear;
        month = inMonth;//Month - 1 to start at 0.
        rosterArray.addAll(rArray);
        loadCrewsArray();
        loadHolidays();
        loadStatusData();
        loadBlockouts();
    }
    
    private void loadCrewsArray(){
        
        for(Roster r: rosterArray){
            ArrayList<ArrayList<String>> inner = getCrewData(r.getTitle());
            crewsArray.add(inner);
        }
    }
    
    public void setAssingned(){
        
        ArrayList<ArrayList<ArrayList<Assignee>>> rosterLevel = getUpdate();
        Calendar selCal = Calendar.getInstance();
        selCal.set(year, (month-1), 1);
        
        for(int i = 0 ; i < crewsArray.size(); i++){
            
            ArrayList<ArrayList<String>> crewlist = crewsArray.get(i);
            ArrayList<ArrayList<Assignee>> roster = rosterLevel.get(i);
            ObservableList<ObservableList<StringProperty>> rowData = FXCollections.observableArrayList(); 
            
            boolean rWeekends = rosterArray.get(i).getWeekends();
            boolean rHolidays = rosterArray.get(i).getHolidays();
            
            String title =  rosterArray.get(i).getTitle();
            String pathName = "Crew_"+ title+"_"+year+"_"+month;
            RosterData dr = new RosterData();

            for (ArrayList<String> row : crewlist) {
                ObservableList<StringProperty> member = FXCollections.observableArrayList();
                member.add(new SimpleStringProperty(row.get(0))); //Rank
                member.add(new SimpleStringProperty(row.get(1))); //Name
                member.add(new SimpleStringProperty(row.get(2))); //NM n
                member.add(new SimpleStringProperty(row.get(3))); //NM w
                member.add(new SimpleStringProperty(row.get(4))); //NM h
                member.add(new SimpleStringProperty(row.get(5))); //n
                member.add(new SimpleStringProperty(row.get(6))); //w
                member.add(new SimpleStringProperty(row.get(7))); //h
              
                //Day of the month
                for(int j = 0; j < roster.size(); j++){
                    
                    //These will be needed to handle the weekend incrementers
                    selCal.set(Calendar.DAY_OF_MONTH, j+1);
                    boolean isHol = isHoliday(selCal);
                    int thisDay = selCal.get(Calendar.DAY_OF_WEEK);
                    int daily=0;
                    
                    for(Assignee a:roster.get(j)){
                        
                        if ( isHol && rHolidays ){
                             daily = a.getLastH(); 
                             member.set(4, new SimpleStringProperty(Integer.toString(a.getLastH())) ); 
                          
                        }
                        else if(rWeekends && (thisDay==1 || thisDay==7) ){
                             daily = a.getLastW(); 
                             member.set(3, new SimpleStringProperty(Integer.toString(a.getLastW())) ); 
                        }  
                        else{
                            daily = a.getLastN();
                            member.set(2, new SimpleStringProperty(Integer.toString(a.getLastN())) );  
                        }
                        
                        if (j==roster.size()-1){
                            member.set(4, new SimpleStringProperty(Integer.toString(a.getLastH()+1)) ); 
                            member.set(3, new SimpleStringProperty(Integer.toString(a.getLastW()+1)) );
                            member.set(2, new SimpleStringProperty(Integer.toString(a.getLastN()+1)) ); 
                        }

                        if(a.getName().equals(row.get(1))){
                           String newS;
                           
                            if (!a.getStatusCode().isEmpty() )
                               newS = a.getStatusCode();
                            else    
                               newS = Integer.toString(daily);
                          
                           member.add( (8+j), new SimpleStringProperty(newS) ); 
                           break;
                        }
                    
                    }
                    
                }
                rowData.add(member);
            }
            dr.storeData(pathName, rowData); 
        }
    
    }
    
    public ArrayList<ArrayList<ArrayList<Assignee>>> getUpdate(){
        
        //Used to contain new daily info for each assignee
        ArrayList<ArrayList<ArrayList<Assignee>>> rosterLevel = new ArrayList();            
        
        Calendar selCal = Calendar.getInstance();
        selCal.set(year, (month-1), 1);
        int lastDay = selCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        Calendar thisDate = Calendar.getInstance();       
        
        ArrayList<String> errorList = new ArrayList<>();

        //Loop through rosters==============
        for(int i = 0; i < crewsArray.size(); i++){ 
            
            ArrayList<ArrayList<Assignee>> dayLevel = new ArrayList();
            boolean rWeekends = rosterArray.get(i).getWeekends();
            boolean rHolidays = rosterArray.get(i).getHolidays();
            int rDInterval =  rosterArray.get(i).getDInterval();
            int rRInterval =  rosterArray.get(i).getRInterval();
            int rAmount =  rosterArray.get(i).getAmount();
            int n,w,h,d;
            boolean errorFlag = false;

            //====Day Columns Loop
            for(int j=1; j <= lastDay; j++){ 
                
                ArrayList<Assignee> assignees = new ArrayList();
                selCal.set(Calendar.DAY_OF_MONTH, j);
                boolean isHol = isHoliday(selCal);
                int thisDay = selCal.get(Calendar.DAY_OF_WEEK);

                thisDate.set(year, month-1, j);
                
                //===Crew Rows Loop
                for(ArrayList<String> row : crewsArray.get(i)){
                    
                    d = (j>1) ? lookupDuty( dayLevel,j-1,row.get(1)) : rRInterval + 1;
                    
                    if(isHol && rHolidays){
                        n = lookupInc( dayLevel,row,'n',j-1,row.get(1));
                        w = lookupInc( dayLevel,row,'w',j-1,row.get(1));
                        h = lookupInc( dayLevel,row,'h',j-1,row.get(1))+1;     
                    }
                    else if(rWeekends && (thisDay==1 || thisDay==7) ){
                        n = lookupInc( dayLevel,row,'n',j-1,row.get(1));
                        w = lookupInc( dayLevel,row,'w',j-1,row.get(1))+1;
                        h = lookupInc( dayLevel,row,'h',j-1,row.get(1));
                    }
                    else{
                        n = lookupInc( dayLevel,row,'n',j-1,row.get(1))+1;
                        w = lookupInc( dayLevel,row,'w',j-1,row.get(1));
                        h = lookupInc( dayLevel,row,'h',j-1,row.get(1));
                    }                        

                    assignees.add(new Assignee(row.get(1),n,w,h,d));
                }
                
                //Sort list decsending so first index is the highest increment
                if(isHol && rHolidays){
                    Collections.sort(assignees, (Assignee o1, Assignee o2) ->
                    o2.getLastH()-o1.getLastH());
                }
                else if(rWeekends && (thisDay==1 || thisDay==7) ) {
                    Collections.sort(assignees, (Assignee o1, Assignee o2) ->
                    o2.getLastW()-o1.getLastW());
                }
                else{
                    Collections.sort(assignees, (Assignee o1, Assignee o2) ->
                    o2.getLastN()-o1.getLastN());
                }
        
                //Duty Assignment here==============================
                int countAssigned = 0;
                
                int dailyRequirement = (24/rDInterval) * rAmount;
                int needed = dailyRequirement;
                for(int c = 0; c < assignees.size(); c++){
                    
                    String onBlock = blockStatus(assignees.get(c).getName(), thisDate);
                    String tName = assignees.get(c).getName();
                    int tResting = assignees.get(c).getOnDuty();
                    
                    boolean onDuty=false;
                    boolean onRest=false;
                    boolean upcomingDuty=false;
                    boolean upcomingRest=false;
                    
                    //Check duty conflicts on higher priority rosters that are happening today
                    //returns 1 for duty, -1 for resting
                    int chkToday = (i > 0) ? checkDutyConflict(rosterLevel,rRInterval,tName,i,j-1) : 0;//today                  
                    
                    if (chkToday==1)
                        onDuty = true;
                    else if (chkToday==-1)
                        onRest = true;

                    //Check duty conflicts on higher priority rosters that will happen tomorrow
                    //returns 1 for duty, -1 for resting
                    int chkTomorrow = (i > 0 && j < lastDay) ? checkDutyConflict(rosterLevel,rRInterval,tName,i,j) : 0; //tomorrow 
                    
                    if (chkTomorrow==1)
                        upcomingDuty = true;
                    else if (chkTomorrow==-1)
                        upcomingRest = true;                    
                    
                    //Check for current roster weekend, holiday, normal conflicts
                    if(tResting < rRInterval) 
                        onRest = true; 


                    if( c < needed && onBlock.isEmpty() && !onDuty && !onRest && !upcomingRest && !upcomingDuty){
                        
                        //Place on duty
                        assignees.get(c).setStatusCode(DUTY);
                        assignees.get(c).setOnDuty(0);
                        countAssigned++;
                    
                        //Reset current increment
                        if(isHol && rHolidays){
                           assignees.get(c).setLastH(0); 
                        }
                        else if(rWeekends && (thisDay==1 || thisDay==7) ){
                            assignees.get(c).setLastW(0); 
                        }
                        else{
                            assignees.get(c).setLastN(0); 
                        }
                    
                    }
                    else{
                        
                        //Adding rested time should always happen unless the person is pulling duty
                        int lastDuty = assignees.get(c).getOnDuty(); //Used for rest accumalation
                        assignees.get(c).setOnDuty(lastDuty+rDInterval); //add more rested time
                        
                        if(onDuty){
                            needed++;
                            assignees.get(c).setStatusCode(DETAIL); 
                            
                        }
                        else if(!onBlock.isEmpty() || onRest || upcomingDuty || upcomingRest){
                            
                            needed++;
                            if(c < needed || !onBlock.isEmpty()){
                                assignees.get(c).setStatusCode(REST);
                                //Since the increment already occured, the only way to
                                //make it seem as if it stopped is to just take one back here
                                if(isHol && rHolidays)
                                    assignees.get(c).setLastH(assignees.get(c).getLastH()-1);    
                                else if(rWeekends && (thisDay==1 || thisDay==7) )
                                    assignees.get(c).setLastW(assignees.get(c).getLastW()-1);
                                else
                                    assignees.get(c).setLastN(assignees.get(c).getLastN()-1);     

                            }     
                        }
                        else{
                            assignees.get(c).setStatusCode(BLANK);   
                        }
                            
                    }

                }
                
                if(dailyRequirement > countAssigned )
                    errorFlag = true;
                dayLevel.add(assignees);    
            }
            
            if(errorFlag)
                errorList.add(rosterArray.get(i).getTitle());
            
            rosterLevel.add(dayLevel);
        }
        
        if (!errorList.isEmpty() ) {
            
            String roster = "";
            for (String s : errorList)
                roster += (roster.isEmpty())? s : ", " + s;
            
            roster = (errorList.size()>1) ? " the followin rosters; " + roster : " roster " + roster;

            Alert alert;
             alert = new Alert(Alert.AlertType.ERROR, "There may not be enough employees on" + roster);
             alert.setTitle("Possible assignment errors");
             alert.showAndWait();
        }
        
        
        return rosterLevel;       
    }
    
     private int checkDutyConflict(ArrayList<ArrayList<ArrayList<Assignee>>> rosters,
            int cRInterval,String name,int rIndex, int thisDay){ 
         
        for(int i = rIndex - 1; i >= 0; i--){
        
            int rRInterval =  rosterArray.get(i).getRInterval();    
            rRInterval = (rRInterval > cRInterval)? rRInterval : cRInterval; 
            
            ArrayList<Assignee> assignees = rosters.get(i).get(thisDay);
           
            for(Assignee a : assignees){       
                
                if(a.getName().equals(name)){

                    if(a.getStatusCode().equals(DUTY))
                        return 1;
                    else if(a.getOnDuty() <= rRInterval) 
                        return -1;
                }

            }
            
        } 
        return 0;
    }
    
    private int lookupInc(ArrayList<ArrayList<Assignee>> dayLevel, ArrayList<String> uData, char type, int day, String eName){
      
        if(dayLevel.isEmpty()){
       
            switch(type){
                case 'n' : return Integer.parseInt(uData.get(5)) - 1;
                case 'w' : return Integer.parseInt(uData.get(6)) - 1;
                case 'h' : return Integer.parseInt(uData.get(7)) - 1;
            }             
        }
 
        for(Assignee row : dayLevel.get(day-1)){
            
            if(row.getName().equals(eName)){
                switch(type){
                    case 'n' : return row.getLastN();
                    case 'w' : return row.getLastW();
                    case 'h' : return row.getLastH();
                } 
            }   
        }
        return 0;
    }
    
    private int lookupDuty(ArrayList<ArrayList<Assignee>> dayLevel, int day, String eName){
      
        for(Assignee row : dayLevel.get(day-1))
            if(row.getName().equals(eName))
                return row.getOnDuty();

        return 0;
    }
    
    private ArrayList<ArrayList<String>> getCrewData(String rosterName){
        
        ArrayList<ArrayList<String>> returnArray = new ArrayList();
        
        Calendar selCal = Calendar.getInstance();
        selCal.set(year, (month-1), 1);

        CrewController cController = new CrewController();
        SecureFile scCrews = new SecureFile("Crew_" + rosterName);      
        ArrayList<Employee> aCrews = cController.getCrewData(scCrews);
        ArrayList<String> rdArray = new ArrayList();
        String pathName = "Crew_"+ rosterName+"_"+year+"_"+month;
        //This will pull daily data for each roster member        
        RosterData rd = new RosterData();
 
        for(Employee crew: aCrews){
            
            String crewName = crew.getName();
            
            ArrayList<String> row = new ArrayList();
            row.add(crew.getRank()); //Index 0
            row.add(crewName); //Index 1
         
            if (!crewName.isEmpty())
            rdArray = rd.getRow(pathName,crewName);
           
            if(rdArray==null || rdArray.isEmpty()){
                for (int i = 0; i < 6; i++)
                        row.add("1");
           }
           else{
               for (int i = 2; i < 8; i++)
                        row.add(rdArray.get(i));
           }
           returnArray.add(row);
        }

    return returnArray;    
    }
    
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
                    
                     String holName = Tools.replaceSpecialChars(bArry[0]);   
                    holidayArray.add(  new Holiday( holName, bArry[1], bArry[2])  );
                }
            
            }    
        
        }       
        
    }
    
    public Boolean isHoliday (Calendar curDate){
        
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();   
      
        for(Holiday h : holidayArray){
            
            try {
                startDate.setTime( sdf.parse( h.getFromDate() ) );
                endDate.setTime( sdf.parse( h.getToDate() ) );
                endDate.add(Calendar.DATE, 1);
            } 
            catch (ParseException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if ( curDate.after(startDate) && curDate.before(endDate) )
                return true;
            
        }    
        return false;
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
                    String sCode = Tools.replaceSpecialChars(bArry[0]);
                    String sTitle = Tools.replaceSpecialChars(bArry[1]);
                    
                    statusArray.add(  new Status(sCode, sTitle, incs)  );  
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
                     String sTitle = Tools.replaceSpecialChars(bArry[0]);
                     
                    blockoutArray.add( new Blockout(
                            sTitle,
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
 
    private boolean doesStatusInc(String code){
        
        for (Status s : statusArray)
            if (s.getCode().equals(code))
                return s.getIncrements();
        
        return false;
    }

}