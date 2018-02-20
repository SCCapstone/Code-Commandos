/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dutyroster;

import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 *
 * @author ISHU
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
    
    
    
}
