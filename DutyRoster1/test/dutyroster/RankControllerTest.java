/**
 * This class will unit test the Rank Controller Class.
 * Author Othen W.Prock
 * @version 1.0.0 February 1, 2018
 */
package dutyroster;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import org.junit.After;
import org.junit.AfterClass;
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

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() throws Exception {
        count = instance.countRanks();
        
    }

    @After
    public void tearDown() throws Exception {
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

    /**
     * Test of startUp method, of class RankController.
     */
    @Test
    public void testStartUp() {
        System.out.println("startUp");
        RankController instance = new RankController();
        instance.startUp();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of initialize method, of class RankController.
     */
    @Test
    public void testInitialize() {
        System.out.println("initialize");
        URL url = null;
        ResourceBundle rb = null;
        RankController instance = new RankController();
        instance.initialize(url, rb);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of shutDown method, of class RankController.
     */
    @Test
    public void testShutDown() {
        System.out.println("shutDown");
        RankController instance = new RankController();
        instance.shutDown();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of storeData method, of class RankController.
     */
    @Test
    public void testStoreData() {
        System.out.println("storeData");
        RankController instance = new RankController();
        instance.storeData();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of retrieveData method, of class RankController.
     */
    @Test
    public void testRetrieveData() {
        System.out.println("retrieveData");
        RankController instance = new RankController();
        instance.retrieveData();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addRank method, of class RankController.
     */
    @Test
    public void testAddRank() {
        System.out.println("addRank");
        ActionEvent event = null;
        RankController instance = new RankController();
        instance.addRank(event);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of highestIndexRank method, of class RankController.
     */
    @Test
    public void testHighestIndexRank() {
        System.out.println("highestIndexRank");
        RankController instance = new RankController();
        int expResult = 0;
        int result = instance.highestIndexRank();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of moveSortDown method, of class RankController.
     */
    @Test
    public void testMoveSortDown() {
        System.out.println("moveSortDown");
        ActionEvent event = null;
        RankController instance = new RankController();
        instance.moveSortDown(event);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rankExists method, of class RankController.
     */
    @Test
    public void testRankExists() {
        System.out.println("rankExists");
        String strIn = "";
        RankController instance = new RankController();
        boolean expResult = false;
        boolean result = instance.rankExists(strIn);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteRank method, of class RankController.
     */
    @Test
    public void testDeleteRank() {
        System.out.println("deleteRank");
        ObservableList<Rank> tmpList = null;
        RankController instance = new RankController();
        instance.deleteRank(tmpList);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of countRanks method, of class RankController.
     */
    @Test
    public void testCountRanks() {
        System.out.println("countRanks");
        RankController instance = new RankController();
        int expResult = 0;
        int result = instance.countRanks();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addNewRank method, of class RankController.
     */
    @Test
    public void testAddNewRank() {
        System.out.println("addNewRank");
        String rank = "";
        RankController instance = new RankController();
        instance.addNewRank(rank);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delRanks method, of class RankController.
     */
    @Test
    public void testDelRanks() {
        System.out.println("delRanks");
        String[] ranks = null;
        RankController instance = new RankController();
        instance.delRanks(ranks);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
