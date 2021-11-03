package customTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data_access.Database;

import data_access.*;

import request.LoginRequest;
import result.AuthResult;
import service.*;

public class LoginServiceTest {
	private LoginService ls;
	private Database db;

	@BeforeEach
	public void setUp() {
		db = new Database();
		ls = new LoginService();
		new ClearService().clear();
	}

	@AfterEach
	public void tearDown() {
		ls = null;
		db.closeConnection(false);
	}

	@Test
	public void testLogin() {
		LoginRequest req = new LoginRequest("dMonster", "pass");
		AuthResult res = ls.login(req);
		assertEquals("Error : User not registered.", res.getMessage());
		
		Database db = new Database();
		try {
			db.getUserData().addUser(new model.User("dMonster", "pass", "clarky@apple.com", "Clark", "Green", "M", "cani"));
			db.closeConnection(true);
		} catch (DataAccessException e) {
			db.closeConnection(false);
			System.out.println("Add for Login Test failed.");
			e.printStackTrace();
		}
		LoginRequest req2 = new LoginRequest("dMonster", "pass"); 
		AuthResult check2 = ls.login(req2);
		if(req2.getUsername() == null) { System.out.println("REQ2"); }
		if(check2 == null) { System.out.println("RESULT"); }
		if(check2.getAuthToken() == null) { System.out.println("AUTH"); System.out.println(check2.getMessage()); }
		if(check2.getAuthToken().getUserName() == null) { System.out.println("USERNAME"); }
		assertEquals(req2.getUsername(), check2.getAuthToken().getUserName());
	}
}
