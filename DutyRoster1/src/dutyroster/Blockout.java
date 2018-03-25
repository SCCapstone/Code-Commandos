/**
 * This is an employee strut class
 * @author Austin Freed
 * @assisted by Tanya Peyush
 * @version 6 3/15/2018
 */
package dutyroster;

//import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Blockout {
  
   //SimipleString is a string. It works better with tableview
   private final SimpleStringProperty name = new SimpleStringProperty("");
   private final SimpleStringProperty status = new SimpleStringProperty("");
   private final SimpleStringProperty fromDate = new SimpleStringProperty("");
   private final SimpleStringProperty toDate = new SimpleStringProperty("");

    //Constructor 
    public Blockout() {

        status.set(null);
        name.set(null); 
        fromDate.set(null);
        toDate.set(null);
    }
 
    public Blockout(String nameIn, String statusIn, String from, String to) {
       
        name.set(nameIn); 
        status.set(statusIn);
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
        && this.fromDate.equals(that.fromDate)
        && this.toDate.equals(that.toDate);
    }
    
}
