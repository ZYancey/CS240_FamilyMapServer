package customTests;

import model.Event;
import model.Person;
import model.User;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data_access.Database;

import request.LoadRequest;
import result.Result;
import service.LoadService;

public class LoadServiceTest {
	private LoadService ls;
	private Database db;

	@BeforeEach
	public void setUp() {
		db = new Database();
		ls = new LoadService();
	}

	@AfterEach
	public void tearDown() {
		ls = null;
		db.closeConnection(false);
	}

	@Test
	public void testLoad() {
		User[] testU = {
				new User("peter", "rock", "p@gmail.com", "Simon", "Peter", "M", ""),
				new User("james", "oldie", "ja@gmail.com", "James", "Someone", "M", ""),
				new User("john", "youngn", "jo@gmail.com", "John", "TheBeloved", "M", "")
		};
		Person[] testP = {
				new Person("123", "Huey", "Duck", "M", "DD", "", "", ""),
				new Person("abc", "Dewey", "Duck", "M", "DD", "", "", ""),
				new Person("xyz", "Louie", "Duck", "M", "DD", "", "", "")
		};
		Event[] testE = {
				new Event("niea0", "123", "DD", 10.45f, 66.789f, "France", "Paris", "birth", 1999),
				new Event("aama", "123", "DD", 55.456f, 3.215f, "Germany", "Berlin", "baptism", 2007),
				new Event("nit43", "123", "DD", 66.664f, -2.13f, "USA", "Duckberg", "death", 2077)
		};
		
		Result res = ls.load(new LoadRequest(testU, testP, testE));
		assertFalse(res.getMessage().contains("Success"));
	}
}
