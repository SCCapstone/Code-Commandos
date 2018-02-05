/**
 * This class will unit test the Rank Controller Class.
 * Author Othen W.Prock
 * @version 1.0.0 February 1, 2018
 */
package dutyroster;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RankControllerTest{
    
    private final RankController instance = new RankController();
    private int count;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        
    }
    
    @Before
    public void setUp() throws Exception {
        count = instance.countRanks();
        
    }


    /**
     * Test of addRank method, of class RankController.
     */
    @Test (expected = IllegalArgumentException.class)
    public void test1AddRankEmpty() {
        System.out.println("addNewRankEmpty");
        String rank = "";
        instance.addNewRank(rank);
    }
    
    @Test
    public void test2AddRank() {
        System.out.println("addNewRankTest1");
        String rank = "test1";
        instance.addNewRank(rank);
    }
  

    @Test (expected = IllegalArgumentException.class)
    public void test3AddRankAlreadyExists() {
        System.out.println("addNewRankAreadyExists");
        String rank = "test2";
        instance.addNewRank(rank);
        instance.addNewRank(rank);
    }

    /**
     * Test of delRanks method, of class RankController.
     */
    @Test
    public void test5DelRanks() {
        System.out.println("delRanks");
        String[] ranks = {"test1","test2"};
        instance.delRanks(ranks);
    }
 
        /**
     * Test of countRanks method, of class RankController.
     */
    @Test
    public void test6countRanks() {
        System.out.println("countRanks");
        assertTrue(count== instance.countRanks());
        
    }
    
}
