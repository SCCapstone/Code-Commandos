/**
 * This is an employee strut class
 * @author Harini, Othen
 * @assisted by Austin Freed
 * @version 4 2/17/2018
 */
package dutyroster;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;


public class Employee {

   private Integer sort;
   //SimipleString is a string. It works better with tableview
   private final SimpleStringProperty rank = new SimpleStringProperty("");
   private final SimpleStringProperty name = new SimpleStringProperty("");
   private final SimpleBooleanProperty crew = new SimpleBooleanProperty(false);
   
    //Constructor 
    public Employee() {
      this(0,"", "");
    }
 
    public Employee(int sortIn, String rankIn, String nameIn) {
        setSort(sortIn);
        setRank(rankIn);
        setName(nameIn);       
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
     
    //Added for assigning employees to individual rosters
    public SimpleBooleanProperty crewProperty() {
        return this.crew;
    }

    public boolean getCrew() {
        return this.crewProperty().get();
    }

    public void setCrew(boolean crew) {
        this.crewProperty().set(crew);
    }
    

}
