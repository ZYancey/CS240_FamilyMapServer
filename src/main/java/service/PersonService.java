package service;


import model.*;
import data_access.*;
import request.PersonRequest;
import result.PersonResult;


public class PersonService {
    public PersonService() {}


    public PersonResult getAll(PersonRequest pr) {
        
        Database db = new Database();
        try {
            AuthToken token;
            try {
                token = db.getAuthData().getAuthToken(pr.getAuthTokenID());
                if(token == null) {
                    db.closeConnection(false);
                    
                    return new PersonResult("Invalid AuthTokenID.");
                }
            } catch (DataAccessException e) {
                db.closeConnection(false);
                
                return new PersonResult("Invalid AuthTokenID.");
            }


            Person[] results = db.getPersonData().getAllPersons(token.getUserName());
            db.closeConnection(true);
            return new PersonResult(results);
            
        } catch (DataAccessException per) {
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            return new PersonResult(String.format("Failed to get all Persons : %s", per.getLocalizedMessage()));
        }
    }

    public PersonResult getPerson(PersonRequest pr) {
        
        Database db = new Database();
        try {
            AuthToken token;
            try {
                //Get AuthToken
                token = db.getAuthData().getAuthToken(pr.getAuthTokenID());
                if(token == null) {
                    
                    return new PersonResult("Invalid AuthTokenID.");
                }
            } catch (DataAccessException e) {
                return new PersonResult("Invalid AuthTokenID.");
            }

            Person result = db.getPersonData().getPerson(pr.getPersonID());
            if(!result.getUsername().equals(token.getUserName())) {
                throw new DataAccessException("Not authorized to access that person.");
            }
            db.closeConnection(true);
            
            
            return new PersonResult(result);
        } catch (DataAccessException per) {
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            return new PersonResult(String.format("Failed to get Person : %s", per.getLocalizedMessage()));
        }
    }
}