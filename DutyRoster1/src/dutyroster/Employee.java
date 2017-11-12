/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dutyroster;

import javafx.beans.property.SimpleStringProperty;


public class Employee {

   private Integer sort;
   private final SimpleStringProperty rank = new SimpleStringProperty("");
   private final SimpleStringProperty name = new SimpleStringProperty("");

    public Employee() {
      this(0,"", "");
    }
 
    public Employee(int sortIn, String rankIn, String nameIn) {
        setSort(sortIn);
        setRank(rankIn);
        setName(nameIn);
    }

    public String getRank() {
        return rank.get();
    }
    
    public String getName() {
        return name.get();
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
    
    public void setName(String nameIn) {
        name.set(nameIn);
    }
    
        
}
