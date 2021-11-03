package customTests;

import static org.junit.Assert.*;

import model.Event;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dao.*;
import results.*;
import services.*;
import requests.*;

public class EventServiceTest {
	private EventService es;

	@Before
	public void setUp() {
		Database.setTesting(true);
		es = new EventService();
		new ClearService().clear();
	}

	@After
	public void tearDown() {
		es = null;
		Database.setTesting(false);
	}

	@Test
	public void testGetAll() {
		RegisterRequest req = new RegisterRequest("gMonster", "gremlins", "dumblydore@hogwarts.edu", "Albus", "Dumbledore", 'M');
		AuthResult ar = new RegisterService().register(req);
		
		EventRequest er = new EventRequest(ar.getAuthToken().getAuthTokenID(), "");
		assertEquals(123, es.getAll(er).getData().length);
	}

	@Test
	public void testGetEvent() {
		RegisterRequest req = new RegisterRequest("tMonster", "gaanhpianh", "dobby@hogwarts.edu", "Nick", "Dumbledore", 'M');
		AuthResult ar = new RegisterService().register(req);
		
		Event a = new Event("faltg", ar.getAuthToken().getPersonID(), "tMonster", 10.191, 77.004, "Iceland", "Yyyvsk", "birth", "1777");

		Database db = new Database();
		
		try {
			db.getED().addEvent(a);
			db.closeConnection(true);
		} catch (DatabaseException e) {
			db.closeConnection(false);
			System.out.println(e.getLocalizedMessage());
		}
		
		EventRequest er = new EventRequest(ar.getAuthToken().getAuthTokenID(), "faltg");
		assertEquals("1777", es.getEvent(er).getEvent().getYear());
	}
}
