package customTests;

import data_access.DataAccessException;
import data_access.Database;
import data_access.PersonDAO;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class P_DAOTest {
    private Database db;
    private Person bestPerson;
    private PersonDAO pDao;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        bestPerson = new Person(
                "Biking_123A",
                "Gale",
                "Gale123A",
                "35.9f",
                "F",
                "Japan",
                "Ushiku",
                "Biking_Around");
        //Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the PersonDAO so it can access the database
        pDao = new PersonDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
        pDao.addPerson(bestPerson);
        //So lets use a find method to get the event that we just put in back out
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        Person compareTest = pDao.getPerson(bestPerson.getPersonID());
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(bestPerson, compareTest);
    }
    @Test
    public void retrievalPass() throws DataAccessException {
        pDao.addPerson(bestPerson);
        Person compareTest = pDao.getPerson(bestPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        //lets do this test again but this time lets try to make it fail
        //if we call the method the first time it will insert it successfully
        pDao.addPerson(bestPerson);
        //but our sql table is set up so that "eventID" must be unique. So trying to insert it
        //again will cause the method to throw an exception
        //Note: This call uses a lambda function. What a lambda function is is beyond the scope
        //of this class. All you need to know is that this line of code runs the code that
        //comes after the "()->" and expects it to throw an instance of the class in the first parameter.
        assertThrows(DataAccessException.class, ()-> pDao.addPerson(bestPerson));
    }

    @Test
    public void deletePass() throws DataAccessException {

        pDao.addPerson(bestPerson);
        pDao.clearTables();
        pDao.addPerson(bestPerson);
        Person compareTest = pDao.getPerson(bestPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);
    }
}
