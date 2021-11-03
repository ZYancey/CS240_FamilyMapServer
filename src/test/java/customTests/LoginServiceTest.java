package customTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dao.*;

import requests.LoginRequest;
import results.AuthResult;
import services.*;

public class LoginServiceTest {
	private LoginService ls;

	@Before
	public void setUp() {
		Database.setTesting(true);
		ls = new LoginService();
		new ClearService().clear();
	}

	@After
	public void tearDown() {
		ls = null;
		Database.setTesting(false);
	}

	@Test
	public void testLogin() {
		LoginRequest req = new LoginRequest("dMonster", "pass");
		AuthResult res = ls.login(req);
		assertEquals("Login failed : User not registered.", res.getMessage());
		
		Database db = new Database();
		try {
			db.getUD().addUser(new model.User("dMonster", "pass", "clarky@apple.com", "Clark", "Green", 'M', "cani"));
			db.closeConnection(true);
		} catch (DatabaseException e) {
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
