/**
 * This class is used like a private strut for all employee related data
 * @Author Othen W. Prock
 * @Version 1.2, 11 November, 2017
 * 
 */
package dutyroster;

import javafx.beans.property.SimpleStringProperty;


public class Employee {

   private Integer sort;
   private final SimpleStringProperty rank = new SimpleStringProperty("");
   private final SimpleStringProperty name = new SimpleStringProperty("");

    /**
     * Blank Constructor
     */
    public Employee() {
      this(0,"", "");
    }
 
    /**
     * Main Constructor. used to add new employees
     * @param sortIn
     * @param rankIn
     * @param nameIn 
     */
    public Employee(int sortIn, String rankIn, String nameIn) {
        setSort(sortIn);
        setRank(rankIn);
        setName(nameIn);
    }

    
    //Getters
    public String getRank() {
        return rank.get();
    }
    
    public String getName() {
        return name.get();
    }
    
     public int getSort() {
        return sort;
    }
    
    //Setters
    public void setRank(String rankIn) {
        rank.set(rankIn);
    }
    
    public void setName(String nameIn) {
        name.set(nameIn);
    } 
    
    public void setSort(int sortIn) {
        sort = sortIn;
    }
    
}
