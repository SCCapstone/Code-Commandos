
package dutyroster;

public class Assignee{
    
    private String name;
    private boolean onDuty;
    private int lastN, lastW, lastH;
    
    
    public Assignee(String inName,
                    int inLastN,
                    int inLastW,
                    int inLastH,
                    boolean onD){
        name = inName;
        lastN = inLastN;
        lastW = inLastW;
        lastH = inLastH;
        onDuty = onD;
    }
    
    public String getName(){
        return name;
    }
    
    public int getLastN(){
        return lastN;
    }
    

    public int getLastW(){
        return lastW;
    }  
        
    public int getLastH(){
        return lastH;
    }  
    public boolean getOnDuty(){
        return onDuty;
    }   
    public void setName(String inName){
        name = inName; 
    }
    

    public void setLastN(int inLast){
        lastN = inLast;
    }
    
    public void setLastW(int inLast){
        lastW = inLast;
    }
    
    public void setLastH(int inLast){
        lastH = inLast;
    }
    public void setOnDuty(boolean onD){
            onDuty = onD;
    }
}
