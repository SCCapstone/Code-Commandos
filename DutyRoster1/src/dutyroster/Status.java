/**
 * @authors Austin Freed, Tanya Peyush, Harini Karnati
 * @version 2, 3/15/2018
 */

package dutyroster;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Status {
    
     private final SimpleStringProperty code = new SimpleStringProperty("");
     private final SimpleStringProperty title = new SimpleStringProperty("");
     private final SimpleBooleanProperty increments = new SimpleBooleanProperty(false);
     
    //Constructors
    public Status() {
      this("", "", false);
    }
    
    public Status(String code, String title) {
        
        setCode(code);
        setTitle(title);

    }
    
    public Status(String code, String title, boolean incs) {
        
        setCode(code);
        setTitle(title);
        setIncrements(incs);
    }
    
     public SimpleBooleanProperty iProperty() {
        return this.increments;
    }
     
    //getters and setters
    public String getCode() {
        return code.get();
    }
    
    public String getTitle() {
        return title.get();
    }
    
    public boolean getIncrements() {
        return  this.iProperty().get();
    }
    
    public void setCode(String codeIn) {
        code.set(codeIn);
    }
    
    public void setTitle(String titleIn) {
        title.set(titleIn);
    }
 
    public void setIncrements(boolean incs) {
        this.iProperty().set(incs);
    }
    
    

}
