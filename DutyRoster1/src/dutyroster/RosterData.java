/**
 * This class is used to manage Roster data for each roster and for
 * each month.
 * @author Othen
 * @version 1, 2/18/2018
 */
package dutyroster;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;


public class RosterData {

    private final SecureFile scf; 

    public RosterData(String filePath){
        scf = new SecureFile(filePath);
    }
    
    /**
     * This function will retrieve all stored data for each roster based
     * on a specific month and year.
     * @return 
     */
    public ArrayList<ArrayList<String>> retrieveData(){
        
        String a = scf.retrieve();
        ArrayList<ArrayList<String>> returnArray = new ArrayList();
        String[] aArry = a.split("\\|", -1);
        int i;
        
        for (String b : aArry){
            
            ArrayList<String> innerArray = new ArrayList();
            String[] bArry = b.split("\\@", -1);

            innerArray.addAll(Arrays.asList(bArry));
            returnArray.add(innerArray);
        }
        return returnArray;
    }
  
    /**
     * This function will store all stored data for each roster based
     * on a specific month and year.
     * @param rosterArray
     */
    public void storeData(ObservableList<ObservableList<StringProperty>> rosterArray){

        String strData = "";
        for(ObservableList<StringProperty> innerList: rosterArray){
            for(StringProperty sp: innerList){
              strData +=  sp.get() + "@";
            }
            strData = Tools.removeLastChar(strData);
            strData += "|";   
        }

        strData = Tools.removeLastChar(strData);
        //Store string array into secure file
        scf.store(strData);

    }
    
    public ArrayList<String> getRow(String s){
        
        ArrayList<ArrayList<String>> sArray = retrieveData();
        ArrayList<String> returnRow = new ArrayList();
       /*
        for(ArrayList<String> row : sArray){
            if(row!=null && row.get(1).equals(s))
                returnRow = row;
        }
        */
        return returnRow;
    }
}
