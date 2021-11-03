package customTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data_access.Database;

import request.*;
import service.*;
import result.*;

public class FillServiceTest {
	private FillService fs;
	private Database db;

	@BeforeEach
	public void setUp() {
		db = new Database();
		fs = new FillService();
	}

	@AfterEach
	public void tearDown() {
		fs = null;
		db.closeConnection(false);
	}

	@Test
	public void testDefaultFill() {
		RegisterRequest rr = new RegisterRequest(
				"kMonster",
				"buenaVista",
				"mockingjay@myldsmail.net",
				"Katniss",
				"Everdeen",
				"F",
				"ID");

		AuthResult ar = new RegisterService().register(rr);
		
		Result res = fs.fill(new FillRequest(ar.getAuthToken().getUserName(), 0));
		
		assertTrue(res.getMessage().contains("31"));
		assertTrue(res.getMessage().contains("123"));
	}
	
	@Test
	public void testSpecificFill() {
		RegisterRequest rr = new RegisterRequest(
				"vMonster",
				"castlevania",
				"fangs@gmail.com",
				"Alucard",
				"Belmont",
				"M",
				"ID");
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
