package customTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data_access.Database;

import model.Event;

import data_access.*;
import result.*;
import service.*;
import request.*;

public class EventServiceTest {
	private EventService es;
	private Database db;

	@BeforeEach
	public void setUp() {
		db = new Database();
		es = new EventService();
		new ClearService().clear();
	}

	@AfterEach
	public void tearDown() {
		es = null;
		db.closeConnection(false);
	}

	@Test
	public void testGetAll() {
		RegisterRequest req = new RegisterRequest(
				"gMonster",
				"gremlins",
				"dumblydore@hogwarts.edu",
				"Albus",
				"Dumbledore",
				"M",
				"ID");
		AuthResult ar = new RegisterService().register(req);
		
		EventRequest er = new EventRequest(ar.getAuthToken().getAuthTokenID(), "");
		assertEquals(123, es.getAll(er).getData().length);
	}

}
