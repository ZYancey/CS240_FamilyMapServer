package service;

import java.util.UUID;
import model.*;

import data_access.*;
import request.FillRequest;
import result.*;

public class FillService {
    public FillService() {}


    public Result fill(FillRequest f) {
        Database db = new Database();
        int persons = 0;
        int events = 0;
        try {
            //Validate User registration.
            if(UserAlreadyRegistered(f.getUsername(), db)) {
                ListResult clear = new ListResult(db.getPersonData().getAllPersons(f.getUsername()), db.getEventData().getAllEvents(f.getUsername()));
                for(int i = 0; i < clear.getPersonList().length; i++) {
                    db.getPersonData().deletePerson(clear.getPersonList()[i]);
                }
                for(int j = 0; j < clear.getEventList().length; j++) {
                    db.getEventData().deleteEvent(clear.getEventList()[j]);
                }

                User u = db.getUserData().getUser(f.getUsername());
                Person p = new Person(u.getFirstName(), u.getLastName(), u.getGender(), UUID.randomUUID().toString(),  "", "", "", u.getUsername());
                //Add the new PersonID back to the User.
                u.setPersonID(p.getPersonID());
                db.getUserData().modifyUser(u);

                DataGenerator data = new DataGenerator();
                ListResult def = null;
                //Create new ancestor data.
                if(f.getGenerations() == 0) {
                    def = data.GenerateDefaultAncestorData(p);
                }
                else if(f.getGenerations() > 0) {
                    def = data.GenerateAncestorData(p, f.getGenerations());
                }
                else if(f.getGenerations() < 0) {
                    db.closeConnection(false);
                    return new Result("Invalid number of generations on the fill request. It must be greater than or equal to 0.");
                }

                //Add the newly created Persons and Events to the database.
                for(int a = 0; a < def.getPersonList().length; a++) {
                    db.getPersonData().addPerson(def.getPersonList()[a]);
                }

                for(int b = 0; b < def.getEventList().length; b++) {
                    db.getEventData().addEvent(def.getEventList()[b]);
                }

                persons = def.getPersonList().length;
                events = def.getEventList().length;

                db.closeConnection(true);
            }
        } catch (DataAccessException e) {
            db.closeConnection(false);
            return new Result(String.format("Fill failed. : %s", e.getLocalizedMessage()));
        }
        //Return the successful Result.
        return new Result(String.format("Successfully added %s persons and %s events to the database.", persons, events));
    }

    private boolean UserAlreadyRegistered(String userName, Database db) throws DataAccessException {
        try {
            db.getUserData().getUser(userName);
            return true;
        } catch (DataAccessException e) {
            throw new DataAccessException("User not already registered.");
        }
    }
}