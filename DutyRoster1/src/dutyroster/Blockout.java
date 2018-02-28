/**
 * This is an employee strut class
 * @author Austin Freed
 * @assisted by Tanya Peyush
 * @version 5 2/28/2018
 */
package dutyroster;


//import javafx.beans.property.SimpleBooleanProperty;
import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;


public class Blockout {

  
   //SimipleString is a string. It works better with tableview
   private final SimpleStringProperty name = new SimpleStringProperty("");
   private final SimpleStringProperty status = new SimpleStringProperty("");
   private LocalDate fromDate;
   private LocalDate toDate;


   
    //Constructor 
    public Blockout() {
     
         
        status.set(null);
        name.set(null); 
        fromDate = LocalDate.now();
        toDate = LocalDate.now();
    }
 
    public Blockout(String nameIn, String statusIn, LocalDate from, LocalDate to) {
       
        name.set(nameIn); 
        status.set(statusIn);
        fromDate = from;
        toDate = to;
       
    }


    //getters for rank, name, sort
    public String getName() {
        return name.get();
    }
    
    public String getStatus() {
        return status.get();
    }
    
    public LocalDate getFromDate() {
        
        return fromDate;
    }
    public LocalDate getToDate() {
        
        return toDate;
    }
  
    
  
     // setters
    public void setName(String nameIn) {
        name.set(nameIn);
    }
    
    public void setStatus(String statusIn) {
        status.set(statusIn);
    }
    
    public void setFromDate(LocalDate from) {
       fromDate = from;
              
    }
    
    public void setToDate(LocalDate to) {
       toDate = to;
              
    }
    
}
