package service;


import data_access.*;
import request.LoadRequest;
import result.Result;

public class LoadService {
    public LoadService() {}


    public Result load(LoadRequest lr) {
        ClearService cs = new ClearService();
        Result clear = cs.clear();
        if(!clear.getMessage().equals("Clear succeeded.")) {
            return new Result(String.format("::Load:: Failed to clear database : %s", clear.getMessage()));
        }

        Database db = new Database();

        try {
            //Load Users
            for(int i = 0; i < lr.getUserList().length; i++) {
                db.getUserData().addUser(lr.getUserList()[i]);
            }

            //Load Persons
            for(int j = 0; j < lr.getPersonList().length; j++) {
                db.getPersonData().addPerson(lr.getPersonList()[j]);
            }

            //Load Events
            for(int k = 0; k < lr.getEventList().length; k++) {
                db.getEventData().addEvent(lr.getEventList()[k]);
            }

            db.closeConnection(true);

        } catch (DataAccessException e) {
            db.closeConnection(false);
            return new Result(String.format("::Load:: Failed to load data : %s", e.getLocalizedMessage()));
        }
        return new Result(String.format("Successfully added %s users, %s persons, and %s events to the database.",
                lr.getUserList().length, lr.getPersonList().length, lr.getEventList().length));
    }
}