/**
 * @authors Austin Freed, Tanya 
 * @version 1
 * 11/20/17
 */
package dutyroster;

import javafx.beans.property.SimpleStringProperty;

//This clas is used as a secure strut
public class Rank {

   private Integer sort = 0;
    private final SimpleStringProperty rank = new SimpleStringProperty("");
   
    //Constructors
    public Rank() {
      this(0, "");
    }
    
    public Rank(int sort, String rank) {
        
        setSort(sort);
        setRank(rank);
        
    }
    //getters and setters
    public String getRank() {
        return rank.get();
    }
    
    public int getSort() {
        return sort;
    }
    
    public void setRank(String rankIn) {
        rank.set(rankIn);
    }
    
 
 
    public void setSort(int sortIn) {
        sort = sortIn;
    }
        
}
