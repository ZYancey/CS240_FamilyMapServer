package customTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data_access.Database;

import result.Result;
import service.ClearService;

public class ClearServiceTest {
	private ClearService cs;
	private Database db;

	@BeforeEach
	public void setUp() {
		db = new Database();
		cs = new ClearService();
	}

	@AfterEach
	public void tearDown() {
		cs = null;
		db.closeConnection(false);
	}

	@Test
	public void testClear() {
		Result message = cs.clear();
		assertEquals("Clear succeeded.", message.getMessage());
	}
}
