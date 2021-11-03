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
				new User("pesdfarg",
						"rowqet4ck",
						"p@gmail.com",
						"Pedro",
						"belmont",
						"M",
						""),
				new User("jasdfasg",
						"olwqet4die",
						"ja@gmail.com",
						"John",
						"franco",
						"M",
						""),
				new User("josdfag",
						"youwqet4ngn",
						"jo@gmail.com",
						"James",
						"Thebes",
						"M",
						"")
		};
		Person[] testP = {
				new Person("sdfa3g",
						"uwqet4",
						"usdfgweck",
						"M",
						"DD",
						"",
						"",
						""),
				new Person("sdfacg",
						"ewqet4y",
						"cdfgsk",
						"M",
						"DD",
						"",
						"",
						""),
				new Person("sdfazg",
						"owqet4e",
						"ucretera",
						"M",
						"DD",
						"",
						"",
						"")
		};
		Event[] testE = {
				new Event("nsdfaag0",
						"1wqet423",
						"DD",
						10.45f,
						66.789f,
						"France",
						"Paris",
						"birth",
						1999),
				new Event("asdfaag",
						"12wqet43",
						"DD",
						55.456f,
						3.215f,
						"Germany",
						"Berlin",
						"baptism",
						2007),
				new Event("nsdfa4g3",
						"1wqet423",
						"DD",
						66.664f,
						-2.13f,
						"USA",
						"Duckberg",
						"death",
						2077)
		};
		
		Result res = ls.load(new LoadRequest(testU, testP, testE));
		assertFalse(res.getMessage().contains("Success"));
	}
}
