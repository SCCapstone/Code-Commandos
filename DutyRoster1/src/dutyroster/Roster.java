/**
 * @authors Othen Prock 
 * @version 1
 * 2/10/18
 */

package dutyroster;

import java.util.ArrayList;

//This clas is used as a secure strut
public class Roster {

    
    private String title;
    private int priority, interval, amount;
    private boolean weekends, holidays;
    private ArrayList<Employee> assigned = new ArrayList<>();;
   
    //Constructors
    public Roster() {
      this("New Roster",0,24,1,true,true);
    }
    
    public Roster(String title, int priority) {
      this(title,priority,24,1,true,true);
    } 
    
    public Roster(String title, int priority,
            int interval, int amount, boolean weekends, boolean holidays) {
        
        this.title = title;
        this.priority = priority;
        this.interval = interval;
        this.amount = amount;
        this.weekends = weekends;
        this.holidays = holidays;
        
    }

    public void setTitle(String val) {
        title = val;
    }
    
    public void setPriority(int val) {
        priority = val;
    }
        
     public void setInterval(int val) {
        interval = val;
    }
     
    public void setAmount(int val) {
        amount = val;
    }

    public void setWeekends(boolean val) {
        weekends = val;
    }

    public void setHolidays(boolean val) {
        holidays = val;
    }
    
    public String getTitle(){
        return title;
    }
    
    public int getPriority(){
        return priority;
    }
 
    public int getInterval(){
        return interval;
    }
    
    public int getAmount(){
        return amount;
    }
    
    public boolean getWeekends(){
        return weekends;
    }
     
    public boolean getHolidays(){
        return holidays;
    }  
     
    public void addMember(Employee ep){
        assigned.add(ep);
    }
    
    public void deleteMember(Employee ep){
        assigned.remove(ep);
    }
    
    public void assignedSort(){
        Tools tools = new Tools();
        Tools.EmployeeComparator eC = tools.new EmployeeComparator();
        assigned.sort(eC);
    }
}


