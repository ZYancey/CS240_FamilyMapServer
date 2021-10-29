package service;

import java.util.UUID;
import java.util.logging.*;

import model.*;

import data_access.*;
import request.FillRequest;
import result.*;

/**The class definition for a FillService object.*/
public class FillService {
    /**The general constructor for a FillService object.*/
    public FillService() {}

    /**The Logger object to log statements on the server log.*/
    private static Logger logger;
    static { logger = Logger.getLogger("familymaptest"); }

    /**Fill the database with the given information for the user given. Any previous data in the database for the user is cleared out before the database is filled with the new data.
     * @param f			A FillRequest containing the user and information to fill the database with.
     * @return			A Result object with the resulting message.*/
    public Result fill(FillRequest f) {
        Database db = new Database();
        int persons = 0;
        int events = 0;
        try {
            //Validate User registration.
            if(UserAlreadyRegistered(f.getUsername(), db)) {
                //Obtain and then delete all Persons and Events tied to the User.
                ListResult clear = new ListResult(db.getPersonData().getAllPersons(f.getUsername()), db.getEventData().getAllEvents(f.getUsername()));
                for(int i = 0; i < clear.getPersons().length; i++) {
                    db.getPersonData().deletePerson(clear.getPersons()[i]);
                }
                for(int j = 0; j < clear.getEvents().length; j++) {
                    db.getEventData().deleteEvent(clear.getEvents()[j]);
                }

                //Create a new Person object for the User since the old one was deleted.
                User u = db.getUserData().getUser(f.getUsername());
                Person p = new Person(UUID.randomUUID().toString(), u.getUsername(),  u.getFirstName(), u.getLastName(), u.getGender(), "", "", "");
                //Add the new PersonID back to the User.
                u.setPersonID(p.getPersonID());
                db.getUserData().modifyUser(u);

                DataGenerator data = new DataGenerator();
                ListResult def = null;
                //Create new ancestor data.
                if(f.getGenerations() == 0) {
                    logger.log(Level.FINER, "No generations parameter.");
                    def = data.GenerateDefaultAncestorData(p);
                }
                else if(f.getGenerations() > 0) {
                    logger.log(Level.FINER, "Generations specified.");
                    def = data.GenerateAncestorData(p, f.getGenerations());
                }
                else if(f.getGenerations() < 0) {
                    db.closeConnection(false);
                    return new Result("Invalid number of generations on the fill request. It must be greater than or equal to 0.");
                }

                //Add the newly created Persons and Events to the database.
                for(int a = 0; a < def.getPersons().length; a++) {
                    db.getPersonData().addPerson(def.getPersons()[a]);
                }

                for(int b = 0; b < def.getEvents().length; b++) {
                    db.getEventData().addEvent(def.getEvents()[b]);
                }

                logger.log(Level.INFO, "Finished adding data from Fill Request.");
                db.closeConnection(true);
                persons = def.getPersons().length;
                events = def.getEvents().length;
            }
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            logger.log(Level.SEVERE, e.getLocalizedMessage());
            return new Result(String.format("Fill failed. : %s", e.getLocalizedMessage()));
        }
        //Return the successful Result.
        return new Result(String.format("Successfully added %s persons and %s events to the database.", persons, events));
    }

    private boolean UserAlreadyRegistered(String userName, Database db) throws DataAccessException {
        logger.log(Level.FINER, "Validating User registration...");
        try {
            db.getUserData().getUser(userName);
            return true;
        } catch (DataAccessException e) {
            throw new DataAccessException("User not already registered.");
        }
    }
}