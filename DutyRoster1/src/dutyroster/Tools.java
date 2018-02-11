/**
 * This class is for nested classes of reusable functions
 * @author othen
*/
package dutyroster;

import java.util.Comparator;

public class Tools {
   
    //remove last delimeter from storage array
    public static String removeLastChar(String str) {
        if(str.length() <= 1 )
            return "";
        return str.substring(0, str.length() - 1);
    }    
    
    public class EmployeeComparator implements Comparator<Employee>{

        @Override
        public int compare(Employee e1, Employee e2){
            // Assume no nulls, and simple ordinal comparisons

            // First by rank - stop if this gives a result.
            int rankResult = e1.getRank().compareTo(e2.getRank());
            if (rankResult != 0)
                return rankResult;


            // Next by name
            int nameResult = e1.getName().compareTo(e2.getName());
            return nameResult;

        }
    
    }
        
}
