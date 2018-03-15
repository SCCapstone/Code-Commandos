/**
 * This class is a collection of functions to assign duty to employees
 * on rosters for a specific month
 * @author Othen Prock
 * @coauthor Michael Harlow
 * @version 3, March 15, 2018
 */
package dutyroster;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Assignment {
    
    private final ArrayList<Roster> rosterArray = new ArrayList();
    private final ArrayList<Blockout> blockoutArray = new ArrayList();
    private final ArrayList<ArrayList<ArrayList<String>>> crewsArray = new ArrayList();

    private final int year;
    private final int month;
    
    public Assignment(ArrayList<Roster> rArray, int inYear, int inMonth){
        year = inYear;
        month = inMonth;//Month - 1 to start at 0.
        rosterArray.addAll(rArray);
        loadCrewsArray();
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
            
            String title =  rosterArray.get(i).getTitle();
            String pathName = "Crew_"+ title+"_"+year+"_"+month;
            RosterData dr = new RosterData();

            for (ArrayList<String> row : crewlist) {
                ObservableList<StringProperty> member = FXCollections.observableArrayList();
                member.add(new SimpleStringProperty(row.get(0)));
                member.add(new SimpleStringProperty(row.get(1)));
                member.add(new SimpleStringProperty(row.get(2)));
                member.add(new SimpleStringProperty(row.get(3)));
                member.add(new SimpleStringProperty(row.get(4)));
 
                for(int j = 0; j < roster.size(); j++){
                    
                    //These will be needed to handle the weekend incrementers
                    selCal.set(Calendar.DAY_OF_MONTH, j+1);
                    int thisDay = selCal.get(Calendar.DAY_OF_WEEK);
                    int daily=0;
                    
                    for(Assignee a:roster.get(j)){
                        
                        if(rWeekends){
                            
                            if(thisDay==1 || thisDay==7)
                             daily = a.getLastW(); 
                            else
                            daily = a.getLastN();
 
                        }
                        else{
                            daily = a.getLastN();
                        }
                        
                        if(a.getName().equals(row.get(1))){
                           String newS = (a.getOnDuty()==0)? "X" : Integer.toString(daily); 
                           member.add( (5+j),new SimpleStringProperty(newS) ); 
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
        int firstSat = Tools.getFirstSaturday(year, (month-1));
       
        // Loop through rosters==============
        for(int i = 0; i < crewsArray.size(); i++){ 
            
            ArrayList<ArrayList<Assignee>> dayLevel = new ArrayList();
            boolean rWeekends = rosterArray.get(i).getWeekends();
            //boolean rHolidays = rosterArray.get(i).getHolidays();
            int rInterval =  rosterArray.get(i).getInterval();
            int rAmount =  rosterArray.get(i).getAmount();
            int n=1,w=1,h=1;
            
            //====Day Columns Loop
            for(int j=1; j <= lastDay; j++){ 
                
                ArrayList<Assignee> assignees = new ArrayList();
                selCal.set(Calendar.DAY_OF_MONTH, j);
                int thisDay = selCal.get(Calendar.DAY_OF_WEEK);
                
                //===Crew Rows Loop
                for(ArrayList<String> row : crewsArray.get(i)){
                      
                    if(j==1){
                        n = Integer.parseInt(row.get(2));
                    }
                    else{
                        
                        if(rWeekends){
                            if(thisDay==1 || thisDay==7){
                                n = lookupInc( dayLevel,"n",j-1,row.get(1));
                                    
                                    if(j==firstSat)
                                    w = Integer.parseInt(row.get(3));
                                    else
                                    w = lookupInc( dayLevel,"w",j-1,row.get(1))+1;
                            }
                            else{
                                n = lookupInc( dayLevel,"n",j-1,row.get(1))+1;
                                w = lookupInc( dayLevel,"w",j-1,row.get(1));
                            }
                        }
                        else{
                            n = lookupInc( dayLevel,"n",j-1,row.get(1))+1;
                            w = lookupInc( dayLevel,"w",j-1,row.get(1));
                        }
                       
                    }
                    
                    assignees.add(new Assignee(row.get(1),n,w,h));
                }
                
                //Sort list decsending so first index is the highest increment
                if(rWeekends){
                     if(thisDay==1 || thisDay==7){
                         Collections.sort(assignees, (Assignee o1, Assignee o2) ->
                         o2.getLastW()-o1.getLastW());
                     }
                     else{
                         Collections.sort(assignees, (Assignee o1, Assignee o2) ->
                         o2.getLastN()-o1.getLastN());
                     }
                 }
                 else{
                     Collections.sort(assignees, (Assignee o1, Assignee o2) ->
                      o2.getLastN()-o1.getLastN());
                 }          
               
                
                //Duty Assignment here==============================
                int needed = (24/rInterval) * rAmount;
                for(int c = 0; c < assignees.size(); c++){
                    
                    if(c < needed){
                        
                        if(rWeekends){
                            
                            if(thisDay==1 || thisDay==7)
                                assignees.get(c).setLastW(0);   
                            else
                                assignees.get(c).setLastN(0);
                        
                        }
                        else{
                            assignees.get(c).setLastN(0);
                        }
                       
                        assignees.get(c).setOnDuty(0);
                    
                    }
                    else{
                        int lastDuty = assignees.get(c).getOnDuty();
                        assignees.get(c).setOnDuty(lastDuty+1);    
                    }
                }
                dayLevel.add(assignees);    
            }
            rosterLevel.add(dayLevel);
        } 
        return rosterLevel;       
    }
    
    private int lookupInc(ArrayList<ArrayList<Assignee>> dayLevel, String type, int day, String eName){
       int returnI=0;
      
        for(Assignee row : dayLevel.get(day-1)){
            
            if(row.getName().equals(eName)){
                switch(type){
                    case "n" : return row.getLastN();
                    case "w" : return row.getLastW();
                    case "h" : return row.getLastH();
                } 
            }   
        }
        return returnI;
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
            row.add(crew.getRank());
            row.add(crewName);
         
            if (!crewName.isEmpty())
            rdArray = rd.getRow(pathName,crewName);
           
            if(rdArray==null || rdArray.isEmpty()){
                for (int i = 0; i < 3; i++)
                        row.add("1");
           }
           else{
                for (int i = 2; i <= 4;i++)
                    row.add(rdArray.get(i));
           }
           returnArray.add(row);
        }

    return returnArray;    
    }
    
}