/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dutyroster;

import javafx.beans.property.SimpleStringProperty;


public class Rank {

   private Integer sort = 0;
    private final SimpleStringProperty rank = new SimpleStringProperty("");
   

    public Rank() {
      this(0, "");
    }
 
    public Rank(int sort, String rank) {
        
        setSort(sort);
        setRank(rank);
        
    }

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
