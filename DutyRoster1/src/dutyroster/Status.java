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
    
     
     private final SimpleStringProperty code = new SimpleStringProperty("");
     private final SimpleStringProperty title = new SimpleStringProperty("");
    
    //Constructors
    public Status() {
      this("", "");
    }
    
    public Status(String code, String title) {
        
        setCode(code);
        setTitle(title);
        
    }
    //getters and setters
    public String getCode() {
        return code.get();
    }
    
    public String getTitle() {
        return title.get();
    }
    
    public void setCode(String codeIn) {
        code.set(codeIn);
    }
    
    public void setTitle(String titleIn) {
        title.set(titleIn);
    }
      
}
