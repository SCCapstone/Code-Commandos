/**
 * This is an employee strut class
 * @author Austin Freed, Tanya Peyush
 * @version 7 4/21/2018
 */
package dutyroster;

//import javafx.beans.property.SimpleBooleanProperty;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;

public class Blockout implements Comparable<Blockout>{
  
   //SimipleString is a string. It works better with tableview
   private final SimpleStringProperty name = new SimpleStringProperty("");
   private final SimpleStringProperty status = new SimpleStringProperty("");
   private final SimpleStringProperty reason = new SimpleStringProperty("");
   private final SimpleStringProperty fromDate = new SimpleStringProperty("");
   private final SimpleStringProperty toDate = new SimpleStringProperty("");

   private SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
   
   
    //Constructor 
    public Blockout() {
        name.set(null); 
        status.set(null);
        reason.set(null);
        fromDate.set(null);
        toDate.set(null);
    }
 
    public Blockout(String nameIn, String statusIn, String reasonIn, String from, String to) {
       
        name.set(nameIn); 
        status.set(statusIn);
        reason.set(reasonIn);
        fromDate.set(from);
        toDate.set(to);
       
    }

    //getters for rank, name, sort
    public String getName() {
        return name.get();
    }
    
    public String getStatus() {
        return status.get();
    }
    
    public String getReason() {
        return reason.get();
    }
    
    public String getFromDate() {
        
        return fromDate.get();
    }
   
    public String getToDate() {
        
        return toDate.get();
    }
  
     // setters
    public void setName(String nameIn) {
        name.set(nameIn);
    
    }
    
    public void setStatus(String statusIn) {
        status.set(statusIn);
    
    }
    
    public void setReason(String reasonIn) {
        reason.set(reasonIn);
    
    } 
    
    public void setFromDate(String from) {
       fromDate.set(from);
              
    }
    
    public void setToDate(String to) {
       toDate.set(to);
              
    }
    
    public boolean equals(Blockout that) {
        
        if (!(that instanceof Blockout)) {
        return false;
        }

        // Custom equality check here.
        return this.name.equals(that.name)
        && this.status.equals(that.status)
        && this.reason.equals(that.reason)
        && this.fromDate.equals(that.fromDate)
        && this.toDate.equals(that.toDate);
    }

    @Override
    public int compareTo(Blockout that) {
  
       
       
        Calendar thisDate = Calendar.getInstance();
        Calendar thatDate = Calendar.getInstance();    

       try {
           thisDate.setTime( sdf.parse( this.getFromDate() ) );
           thatDate.setTime( sdf.parse( that.getFromDate() ) );
       } catch (ParseException ex) {
           Logger.getLogger(Blockout.class.getName()).log(Level.SEVERE, null, ex);
       }
        
        
        return ( thisDate.before(thatDate) ? -1 : thisDate.equals(thatDate) ? 0 : 1);     
        
    }
    
}
