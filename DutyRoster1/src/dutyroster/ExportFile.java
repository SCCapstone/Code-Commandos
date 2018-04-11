/**
 *
 * @author Michael
 */
package dutyroster;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class ExportFile {

    private final FileChooser fileChooser = new FileChooser();
    private  final String TITLE = "Export Roster";
    private final File file;

    //constructor 
    public ExportFile(String newFileName) throws IOException {
        
        
        fileChooser.setTitle(TITLE);
        fileChooser.setInitialFileName(newFileName);
         
        
        fileChooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("PDF file (*.pdf)", "*.pdf"));
       

        Stage mainStage = new Stage();
        //Show save file dialog
        
        file = fileChooser.showSaveDialog(mainStage);
        
        boolean result = Files.deleteIfExists(file.toPath());
    
    }

    public File getFilePath(){
        return file;
    }

 
}






