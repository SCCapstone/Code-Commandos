/**
 * @authors Othen Prock, Michael Harlow
 * @version 2
 * 3/22/18
 */

package dutyroster;
public class Roster {

    
    private String title;
    private int dInterval; // Duty Interval
    private int rInterval; // Rest Interval
    private int amount;
    private boolean weekends, holidays;
    
    //Constructors
    public Roster() {
      this("New Roster",24,24,1,true,true);
    }
    
    public Roster(String title) {
      this(title,24,24,1,true,true);
    } 
    
    public Roster(String title, int dInterval, int rInterval, int amount, boolean weekends, boolean holidays) {
        
        this.title = title;
        this.dInterval = dInterval;
        this.rInterval = rInterval;
        this.amount = amount;
        this.weekends = weekends;
        this.holidays = holidays;
        
    }

    public void setTitle(String val) {
        title = val;
    }
      
    public void setDInterval(int val) {
        dInterval = val;
    }
    
    
    public void setRInterval(int val) {
        rInterval = val;
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
    
    public int getDInterval(){
        return dInterval;
    }
     
    public int getRInterval(){
        return rInterval;
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
     
   
}


