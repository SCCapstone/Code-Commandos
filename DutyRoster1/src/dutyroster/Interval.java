/**
 * This class is a strut for the interval selector on the main page
 */
package dutyroster;

/**
 *
 * @author othen
 */
public class Interval {
    
    int hour;
    String title;
    
    public Interval(int hour, String title){
        this.hour = hour;
        this.title = title;    
    }
    
    public int getHour(){
        return hour;
    }
    
    public String getTitle(){
        return title;
    }
    
    public void setHour(int hour){
        this.hour=hour;
    }
    public void setTitle(String title){
        this.title=title;
    }
    
}
