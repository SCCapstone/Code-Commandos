/**
 * This is the main class for unit testing Duty Roster
 * @author othen
 * @version 1.0.0, February 1, 2018
 */
package dutyroster;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * Use suite classes annotation to attach additional test classes.
 * 
 */
@RunWith(Suite.class)
<<<<<<< Updated upstream
@Suite.SuiteClasses({dutyroster.RankControllerTest.class, dutyroster.RankGuiTest.class})
=======
@Suite.SuiteClasses({RankControllerTest.class, MainControllerTest.class, EmployeeTest.class, EmployeeControllerTest.class, RankTest.class, DriverTest.class, SecureFileTest.class, LoginControllerTest.class})
>>>>>>> Stashed changes
public class DutyrosterSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}

