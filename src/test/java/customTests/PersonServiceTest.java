package customTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dao.Database;

import requests.*;
import results.*;
import services.*;

public class PersonServiceTest {
	private PersonService ps;
	
	@Before
	public void setUp() {
		Database.setTesting(true);
		ps = new PersonService();
	}

	@After
	public void tearDown() {
		ps = null;
		Database.setTesting(false);
	}

	@Test
	public void testGetAll() {
		RegisterRequest rr = new RegisterRequest("yodamaster", "manamana", "huffle@hogwarts.com", "John", "Werner", 'M');
		AuthResult ar = new RegisterService().register(rr);
		
		PersonResult pr = ps.getAll(new PersonRequest(ar.getAuthToken().getAuthTokenID(), ar.getAuthToken().getPersonID()));
	
		assertEquals(pr.getData().length, 31, 0);
	}

	@Test
	public void testGetPerson() {
		RegisterRequest rr = new RegisterRequest("fMonster", "scribbles", "peeves@hogwarts.com", "Chuck", "Norris", 'M');
		AuthResult ar = new RegisterService().register(rr);
		PersonResult pr = ps.getPerson(new PersonRequest(ar.getAuthToken().getAuthTokenID(), ar.getAuthToken().getPersonID()));
		
		assertEquals(pr.getPerson().getFirstName(), "Chuck");
		assertEquals(pr.getPerson().getLastName(), "Norris");
	}
}
