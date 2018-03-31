/**
 * This is for the Duty Roster settings
 * @author ISHU
 */
package dutyroster;


public class Setting {
    
    private String title;
    private int value;
    private String text;
    private String desc;
    
    public Setting (String inTitle, String inDesc){
        title = inTitle;
        desc = inDesc;
        value = 0;
        text = "";
    }
    
    //Getters
    
    public String getTitle(){
        return title;
    }
    
    public String getDesc(){
        return desc;
    }
    
    public int getValue(){
        return value;
    }
    
    public String getText(){
        return text;
    }
    
    //Setters
    
    public void setTitle(String inTitle){
        title = inTitle;
    }
    
    public void setDesc(String inDesc){
        desc = inDesc;
    }
        
    public void setValue(int inValue){
        value = inValue;
    }
    
    public void setText(String inText){
        text = inText;
    }    
}
