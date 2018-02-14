/**
 * This is an employee strut class
 * @author Harini
 * @assisted by Austin Freed
 * @version 3 11/21/2017
 */
package dutyroster;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;


public class Employee {

   private Integer sort;
   //SimipleString is a string. It works better with tableview
   private final SimpleStringProperty rank = new SimpleStringProperty("");
   private final SimpleStringProperty name = new SimpleStringProperty("");
   private SimpleBooleanProperty crew = new SimpleBooleanProperty(false);
   //Constructor 
    public Employee() {
      this(0,"", "");
    }
 
    public Employee(int sortIn, String rankIn, String nameIn) {
        setSort(sortIn);
        setRank(rankIn);
        setName(nameIn);       
    }

     public Employee(int sortIn, String rankIn, String nameIn, Boolean crewIn) {
        setSort(sortIn);
        setRank(rankIn);
        setName(nameIn);
        setCrew(crewIn);
    }
     
    //getters for rank, name, sort
    public String getRank() {
        return rank.get();
    }
    
    public String getName() {
        return name.get();
    }
    
     public int getSort() {
        return sort;
    }
  
     // setters
    public void setRank(String rankIn) {
        rank.set(rankIn);
    }
    
    public void setName(String nameIn) {
        name.set(nameIn);
    } 
    
    public void setSort(int sortIn) {
        sort = sortIn;
    }
     
  

    public SimpleBooleanProperty crewProperty() {
        return this.crew;
    }

    public java.lang.Boolean getCrew() {
        return this.crewProperty().get();
    }

    public void setCrew(final java.lang.Boolean crew) {
        this.crewProperty().set(crew);
    }
    
    
}
