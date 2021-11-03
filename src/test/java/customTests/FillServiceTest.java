package customTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dao.Database;

import requests.*;
import services.*;
import results.*;

public class FillServiceTest {
	private FillService fs;

	@Before
	public void setUp() {
		Database.setTesting(true);
		fs = new FillService();
	}

	@After
	public void tearDown() {
		fs = null;
		Database.setTesting(false);
	}

	@Test
	public void testDefaultFill() {
		RegisterRequest rr = new RegisterRequest("kMonster", "buenaVista", "mockingjay@myldsmail.net", "Katniss", "Everdeen", 'F');
		AuthResult ar = new RegisterService().register(rr);
		
		Result res = fs.fill(new FillRequest(ar.getAuthToken().getUserName(), 0));
		
		assertTrue(res.getMessage().contains("31"));
		assertTrue(res.getMessage().contains("123"));
	}
	
	@Test
	public void testSpecificFill() {
		RegisterRequest rr = new RegisterRequest("vMonster", "castlevania", "fangs@gmail.com", "Alucard", "Belmont", 'M');
		AuthResult ar = new RegisterService().register(rr);
		
		Result res = fs.fill(new FillRequest(ar.getAuthToken().getUserName(), 6));
		
		assertTrue(res.getMessage().contains("127"));
		assertTrue(res.getMessage().contains("507"));
	}
	
	@Test
	public void testNotRegistered() {
		Result res = fs.fill(new FillRequest("hutch", 0));
		
		assertTrue(res.getMessage().contains("User not already registered."));
	}
}
