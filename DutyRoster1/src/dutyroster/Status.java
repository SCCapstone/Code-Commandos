/**
 * @authors Austin Freed, Tanya Peyush, Harini Karnati
 * 02/12/2017
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dutyroster;

import javafx.beans.property.SimpleStringProperty;

public class Status {
    
    
    //private Integer sort = 0;
     private Integer sort;
     
     private final SimpleStringProperty title = new SimpleStringProperty("");
     private final SimpleStringProperty Code = new SimpleStringProperty("");
     private final SimpleStringProperty status = new SimpleStringProperty("");

    
    //Constructors
    public Status() {
      this("", "");
    }
    
    public Status(String status, String title) {
        
        
       // setSort(sort);
        setStatus(status);
        setCode(title);
        
    }
    //getters and setters
    public String getStatus() {
        return status.get();
    }
    
    public int getSort() {
        return sort;
    }
    
     public String getCode() {
        return Code.get();
    }
    
    public void setStatus(String statusIn) {
        title.set(statusIn);
    }
    
    public void setCode(String code) {
        title.set(code);
    }
 
    public void setSort(int sortIn) {
        sort = sortIn;
    }
        
}

