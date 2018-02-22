/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dutyroster;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 *
 * @author Harini
 */
public class ImportFile {

    private final FileChooser fileChooser = new FileChooser();
    private  final String TITLE = "Open Employee Data .csv";
    private String pathFile;


    //constructor 
    public ImportFile() {
        fileChooser.setTitle(TITLE);
        fileChooser.getExtensionFilters().add( new ExtensionFilter("Comma Separated Values File", "*.csv"));

        Stage mainStage = new Stage();
        File selectedFile = fileChooser.showOpenDialog(mainStage);

        if (selectedFile != null) {
            pathFile = selectedFile.getPath();
            }
    }  

    /*
    * returns file path
    * 
    */
    public String getFilePath(){
        return pathFile;
    }


    public ArrayList<Employee> getData(){

        ArrayList <Employee>  returnArray = new ArrayList();

        Scanner sc = null;
        try {
            sc = new Scanner(new File(pathFile));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImportFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (sc != null) {

            String tempRank = "", tempName = "";
            int priority = 0;
            // sc.useDelimiter(",");
            
            while(sc.hasNextLine()) {
                 
                String row = sc.nextLine();
               
                    String[] val = row.split(",");
                    
                    if (!(tempRank.isEmpty() || tempRank.equals(val[0]) )  )  
                        priority++;
                    
                    tempRank = val[0];
                    tempName = val[1];
                    

                    Employee tempE = new Employee(priority, tempRank, tempName);

                    returnArray.add(tempE);
                }
               sc.close();
  
               
            }


            return returnArray;
        }

       
    }






