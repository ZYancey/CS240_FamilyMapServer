package customTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dao.Database;

import results.Result;
import services.ClearService;

public class ClearServiceTest {
	private ClearService cs;

	@Before
	public void setUp() {
		Database.setTesting(true);
		cs = new ClearService();
	}

	@After
	public void tearDown() {
		cs = null;
		Database.setTesting(false);
	}

	@Test
	public void testClear() {
		Result message = cs.clear();
		assertEquals("Clear succeeded.", message.getMessage());
	}
}
