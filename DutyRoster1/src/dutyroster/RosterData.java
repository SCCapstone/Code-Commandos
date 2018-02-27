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

    /**
     * This function will retrieve all stored data for each roster based
     * on a specific month and year.
     * @return 
     */
    public ArrayList<ArrayList<String>> retrieveData(String filePath){
        
        SecureFile scf = new SecureFile(filePath);
        String a = scf.retrieve();
        ArrayList<ArrayList<String>> returnArray = new ArrayList();
        String[] aArry = a.split("\\|", -1);
        
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
     * @param filePath
     * @param rosterArray
     */
    public void storeData(String filePath,
            ObservableList<ObservableList<StringProperty>> rosterArray){
        
        SecureFile scf = new SecureFile(filePath);
        
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
    /**
     * This function will get a specific row from the crew table. 
     * @param filePath
     * @param s
     * @return 
     */
    public ArrayList<String> getRow(String filePath, String s){
        
        ArrayList<ArrayList<String>> sArray = retrieveData(filePath);
       
        try{
            for(ArrayList<String> row : sArray)
                if(row!=null && row.get(1).equals(s))
                    return row;
        }
        catch (Exception ex){/*Just let it die. Its better this way...*/}

    return null;
        
    }


}
