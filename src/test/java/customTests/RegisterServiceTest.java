package customTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data_access.Database;

import request.RegisterRequest;
import result.AuthResult;
import service.*;

public class RegisterServiceTest {
	private RegisterService rs;
	private Database db;

	@BeforeEach
	public void setUp() {
		db = new Database();
		rs = new RegisterService();
	}

	@AfterEach
	public void tearDown() {
		rs = null;
		db.closeConnection(false);
	}

	@Test
	public void testRegister() {
		RegisterRequest rr = new RegisterRequest("cMonster",
				"apple",
				"seeker@hogwarts.com",
				"Harry",
				"Potter",
				"M",
				"personID");
		AuthResult ar = rs.register(rr);
		assertEquals(rr.getUsername(), ar.getAuthToken().getUserName());
	}
}
