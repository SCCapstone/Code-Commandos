 /**
 * This is an holiday inserter class
 * @author Tanya Peyush
 * @assisted by Austin Freed
 * @version 1 3/4/2018
 */
package dutyroster;

//import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Holiday {

   //SimipleString is a string. It works better with tableview
   private final SimpleStringProperty name = new SimpleStringProperty("");
   private final SimpleStringProperty fromDate = new SimpleStringProperty("");
   private final SimpleStringProperty toDate = new SimpleStringProperty("");
   
    //Constructor 
    public Holiday() {
    
        name.set(null); 
        fromDate.set(null);
        toDate.set(null);
    }
 
    public Holiday(String nameIn, String from, String to) {
       
        name.set(nameIn); 
        fromDate.set(from);
        toDate.set(to);
       
    }

    //getters for rank, name, sort
    public String getName() {
        return name.get();
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
    
    public void setFromDate(String from) {
       fromDate.set(from);
    }
    
    public void setToDate(String to) {
       toDate.set(to);
    }
    
}
