/**
 * This class is used like a private strut for all Rank related data
 * @Author Othen W. Prock
 * @Version 1.2, 11 November, 2017
 * 
 */
package dutyroster;

import javafx.beans.property.SimpleStringProperty;


public class Rank {

    private Integer sort = 0;
    private final SimpleStringProperty rank = new SimpleStringProperty("");
   
    /**
     * Blank constructor. 
     */
    public Rank() {
      this(0, "");
    }
    
    /**
     * Main Constructor. Commonly used to add new ranks.
     * @param sort
     * @param rank 
     */
    public Rank(int sort, String rank) {
        
        setSort(sort);
        setRank(rank);    
    }

    //Getters
    public String getRank() {
        return rank.get();
    }
    
    public int getSort() {
        return sort;
    }
    
    //Setters
    public void setRank(String rankIn) {
        rank.set(rankIn);
    }
    
    public void setSort(int sortIn) {
        sort = sortIn;
    }
        
}
