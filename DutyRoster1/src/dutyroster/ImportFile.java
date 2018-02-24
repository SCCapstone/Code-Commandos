/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dutyroster;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private final ObservableList<Rank> rankList;
    private final ObservableList<Employee> importErrors;

    //constructor 
    public ImportFile(ObservableList<Rank> rankArray) {
        
        rankList = FXCollections.observableArrayList();
        importErrors = FXCollections.observableArrayList();
        
        rankList.addAll(rankArray);
        
        fileChooser.setTitle(TITLE);
        fileChooser.getExtensionFilters().add( new ExtensionFilter("Comma Separated Values File", "*.csv"));

        Stage mainStage = new Stage();
        File selectedFile = fileChooser.showOpenDialog(mainStage);

        if (selectedFile != null) {
            pathFile = selectedFile.getPath();
            }
    }  

    /**
    * returns file path
    * 
     * @return 
    */
    public String getFilePath(){
        return pathFile;
    }

    public ObservableList<Employee> getData(){

        ObservableList<Employee>  returnArray = FXCollections.observableArrayList();

        Scanner sc = null;
        try {
            sc = new Scanner(new File(pathFile));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImportFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (sc != null) {

            while(sc.hasNextLine()) {

                String row = sc.nextLine();
                String[] val = row.split(",");
                int priority = Tools.getSortIndex(rankList, val[0]);
             
                if (priority >= 0){
                    Employee tempE = new Employee(priority, val[0], val[1]);
                    returnArray.add(tempE);
                }
                else{
                    importErrors.add(new Employee(priority, val[0], val[1]));
                }
                
            }
            sc.close();

        }
        return returnArray;
    }

    public ObservableList<Employee> getErrors() {
        return importErrors;
    }
    
}






