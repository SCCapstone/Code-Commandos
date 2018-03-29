
package dutyroster;


public class Assignee{
    
    private String name, statusCode;
    private int onDuty;
    private int lastN, lastW, lastH;
    
    public Assignee(String inName,
                    int inLastN,
                    int inLastW,
                    int inLastH){
        name = inName;
        lastN = inLastN;
        lastW = inLastW;
        lastH = inLastH;
        statusCode="";
        onDuty = 100;//This enures any employee can be assigned duty
                     //at the beginning, unless that employee pulled duty on
                     //the last shift of the previous month. Then that person
                     //will carry over a zero using setOnDuty function.
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
    
    public int getOnDuty(){
        return onDuty;
    } 
 
    public String getStatusCode(){
        return statusCode;
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
    
    public void setOnDuty(int onD){
            onDuty = onD;
    }
    
    public void setStatusCode(String status){
            statusCode = status;
    }
    
}
