/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dutyroster;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tanya
 */
public class RankTest {
    
    public RankTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getRank method, of class Rank.
     */
    @Test
    public void testGetRank() {
        System.out.println("getRank");
        Rank instance = new Rank();
        String expResult = "";
        String result = instance.getRank();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSort method, of class Rank.
     */
    @Test
    public void testGetSort() {
        System.out.println("getSort");
        Rank instance = new Rank();
        int expResult = 0;
        int result = instance.getSort();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setRank method, of class Rank.
     */
    @Test
    public void testSetRank() {
        System.out.println("setRank");
        String rankIn = "";
        Rank instance = new Rank();
        instance.setRank(rankIn);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSort method, of class Rank.
     */
    @Test
    public void testSetSort() {
        System.out.println("setSort");
        int sortIn = 0;
        Rank instance = new Rank();
        instance.setSort(sortIn);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
