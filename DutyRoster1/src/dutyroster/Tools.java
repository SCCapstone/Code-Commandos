/**
 * This class is for nested classes of reusable functions
 * @author othen prock, Harini
 * @version 2, 3/23/18
*/
package dutyroster;

import java.util.Calendar;
import java.util.Comparator;
import javafx.collections.ObservableList;

public class Tools {
   
    /**
     * remove last delimeter from storage array
     * @param str
     * @return 
     */
    public static String removeLastChar(String str) {
        if(str.length() <= 1 )
            return "";
        return str.substring(0, str.length() - 1);
    }  
    
    /**
     * getSortIndex pulls the index number for the rank.
     * @param rankOptions
     * @param strRank
     * @return 
     */
    public static int getSortIndex(ObservableList<Rank> rankOptions, String strRank) {
          
        //pulling from the rank, in rankOption pull the current rank to get the index number.
        for(Rank c : rankOptions)
            if (c.getRank().equalsIgnoreCase(strRank) )
                    return c.getSort();
        return 1000;
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
    
    public static int getFirstSaturday(int year, int month) {
    Calendar cal = Calendar.getInstance();
    cal.set(year, month, 1);        
    cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
    cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
    cal.set(Calendar.MONTH, month);
    cal.set(Calendar.YEAR, year);
    return cal.get(Calendar.DATE);
}

        
}
