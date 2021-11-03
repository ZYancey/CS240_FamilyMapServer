package service;

import model.*;
import data_access.*;
import request.PersonRequest;
import result.PersonResult;

public class PersonService {
    public PersonService() {}

    public PersonResult getAll(PersonRequest personRequest) {
        Database database = new Database();
        try {
            AuthToken token;
            try {
                token = database.getAuthData().getAuthToken(personRequest.getAuthTokenID());
                if(token == null) {
                    database.closeConnection(false);
                    return new PersonResult("Error : Invalid AuthTokenID.");
                }
            } catch (DataAccessException exception) {
                database.closeConnection(false);
                return new PersonResult("Error : Invalid AuthTokenID.");
            }

            Person[] allPersons = database.getPersonData().getAllPersons(token.getUserName());
            database.closeConnection(true);
            return new PersonResult(allPersons);
        } catch (DataAccessException exception) {
            database.closeConnection(false);
            return new PersonResult(String.format("Error : %s", exception.getLocalizedMessage()));
        }
    }

    public PersonResult getPerson(PersonRequest personRequest) {
        Database database = new Database();
        try {
            AuthToken token;
            try {
                //Get AuthToken
                token = database.getAuthData().getAuthToken(personRequest.getAuthTokenID());
                if(token == null) {
                    database.closeConnection(false);
                    return new PersonResult("Error : Invalid AuthTokenID.");
                }
            } catch (DataAccessException exception) {
                database.closeConnection(false);
                return new PersonResult("Error : Invalid AuthTokenID.");
            }

            Person person = database.getPersonData().getPerson(personRequest.getPersonID());
            if(!person.getUsername().equals(token.getUserName())) {
                throw new DataAccessException("Error : Not authorized to access that person.");
            }
            database.closeConnection(true);
            return new PersonResult(person);
        } catch (DataAccessException exception) {
            database.closeConnection(false);
            return new PersonResult(String.format("Error : Failed to get Person : %s", exception.getLocalizedMessage()));
        }
    }
}