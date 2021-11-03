package customTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data_access.Database;
import request.*;
import result.*;
import service.*;

public class PersonServiceTest {
	private PersonService ps;
	private Database db;
	
	@BeforeEach
	public void setUp() {
		db = new Database();
		ps = new PersonService();
	}

	@AfterEach
	public void tearDown() {
		ps = null;
		db.closeConnection(false);
	}

	@Test
	public void testGetAll() {
		RegisterRequest rr = new RegisterRequest(
				"yodamaster",
				"manamana",
				"huffle@hogwarts.com",
				"John",
				"Wrner",
				"M",
				"ID");
		AuthResult ar = new RegisterService().register(rr);
		
		PersonResult pr = ps.getAll(new PersonRequest(ar.getAuthToken().getAuthTokenID(), ar.getAuthToken().getPersonID()));
	
		assertEquals(pr.getData().length, 31, 0);
	}

	@Test
	public void testGetPerson() {
		RegisterRequest rr = new RegisterRequest(
				"fMonster",
				"scribbles",
				"peeves@hogwarts.com",
				"Chuck",
				"Norris",
				"M",
				"ID");
		AuthResult ar = new RegisterService().register(rr);
		PersonResult pr = ps.getPerson(new PersonRequest(ar.getAuthToken().getAuthTokenID(), ar.getAuthToken().getPersonID()));
		
		assertEquals(pr.getPerson().getFirstName(), "Chuck");
		assertEquals(pr.getPerson().getLastName(), "Norris");
	}
}
