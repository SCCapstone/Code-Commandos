/**
 * This is for the Duty Roster settings
 * @author ISHU
 */
package dutyroster;


public class Setting {
    
    private int cat;
    private String id;
    private String text;
    private boolean isTextArea;

    public Setting (int inCat, String inId, String inText, boolean inTA){
        cat = inCat;
        id = inId;
        text = inText;
        isTextArea = inTA;
    }
        
    //Getters
    public int getCat(){
        return cat;
    }
    
    public String getId(){
        return id;
    }
    
    public String getText(){
        return text;
    }
    
    public boolean isTextArea(){
        return isTextArea;
    }   
    //Setters   
    public void setCat(int inCat){
        cat = inCat;
    }
    
    public void setId(String inId){
        id = inId;
    }
    
    public void setText(String inText){
        text = inText;
    }

    public void setTextArea(boolean inTA){
        isTextArea = inTA;
    }
}
