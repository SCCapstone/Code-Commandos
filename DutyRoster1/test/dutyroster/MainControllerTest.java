/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dutyroster;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
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
public class MainControllerTest {
    
    public MainControllerTest() {
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
     * Test of initialize method, of class MainController.
     */
    @Test
    public void testInitialize() {
        System.out.println("initialize");
        URL url = null;
        ResourceBundle rb = null;
        MainController instance = new MainController();
        instance.initialize(url, rb);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadMonths method, of class MainController.
     */
    @Test
    public void testLoadMonths() {
        System.out.println("loadMonths");
        int curMonth = 0;
        MainController instance = new MainController();
        instance.loadMonths(curMonth);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of newDate method, of class MainController.
     */
    @Test
    public void testNewDate() {
        System.out.println("newDate");
        ActionEvent event = null;
        MainController instance = new MainController();
        instance.newDate(event);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of goUp method, of class MainController.
     */
    @Test
    public void testGoUp() {
        System.out.println("goUp");
        ActionEvent event = null;
        MainController instance = new MainController();
        instance.goUp(event);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of goDown method, of class MainController.
     */
    @Test
    public void testGoDown() {
        System.out.println("goDown");
        ActionEvent event = null;
        MainController instance = new MainController();
        instance.goDown(event);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
