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
public class EmployeeControllerTest {
    
    public EmployeeControllerTest() {
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
     * Test of initialize method, of class EmployeeController.
     */
    @Test
    public void testInitialize() {
        System.out.println("initialize");
        URL url = null;
        ResourceBundle rb = null;
        EmployeeController instance = new EmployeeController();
        instance.initialize(url, rb);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of shutDown method, of class EmployeeController.
     */
    @Test
    public void testShutDown() {
        System.out.println("shutDown");
        EmployeeController instance = new EmployeeController();
        instance.shutDown();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of storeData method, of class EmployeeController.
     */
    @Test
    public void testStoreData() {
        System.out.println("storeData");
        EmployeeController instance = new EmployeeController();
        instance.storeData();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadEmployees method, of class EmployeeController.
     */
    @Test
    public void testLoadEmployees() {
        System.out.println("loadEmployees");
        EmployeeController instance = new EmployeeController();
        instance.loadEmployees();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadRanks method, of class EmployeeController.
     */
    @Test
    public void testLoadRanks() {
        System.out.println("loadRanks");
        EmployeeController instance = new EmployeeController();
        instance.loadRanks();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addEmployee method, of class EmployeeController.
     */
    @Test
    public void testAddEmployee() {
        System.out.println("addEmployee");
        ActionEvent event = null;
        EmployeeController instance = new EmployeeController();
        instance.addEmployee(event);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
