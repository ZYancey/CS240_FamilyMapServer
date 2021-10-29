package service;

import java.util.logging.*;

import data_access.*;
import request.LoadRequest;
import result.Result;

/**The class definition for a LoadService object.*/
public class LoadService {
    /**The general constructor for a LoadService object.*/
    public LoadService() {}

    /**The Logger object to log statements on the server log.*/
    private static Logger logger;
    static { logger = Logger.getLogger("familymaptest"); }


    public Result load(LoadRequest lr) {
        logger.log(Level.INFO, "Starting load.");
        //Clear the database.
        ClearService cs = new ClearService();
        Result clear = cs.clear();
        if(!clear.getMessage().equals("Clear succeeded.")) {
            logger.log(Level.SEVERE, clear.getMessage());
            return new Result(String.format("::Load:: Failed to clear database : %s", clear.getMessage()));
        }

        Database db = new Database();

        try {
            //Load Users
            logger.log(Level.FINE, "Loading Users from list.");
            for(int i = 0; i < lr.getUserList().length; i++) {
                db.getUserData().addUser(lr.getUserList()[i]);
            }

            //Load Persons
            logger.log(Level.FINE, "Loading Persons from list.");
            for(int j = 0; j < lr.getPersonList().length; j++) {
                db.getPersonData().addPerson(lr.getPersonList()[j]);
            }

            //Load Events
            logger.log(Level.FINE, "Loading Persons from list.");
            for(int k = 0; k < lr.getEventList().length; k++) {
                db.getEventData().addEvent(lr.getEventList()[k]);
            }
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            logger.log(Level.SEVERE, e.getLocalizedMessage());
            return new Result(String.format("::Load:: Failed to load data : %s", e.getLocalizedMessage()));
        }

        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        logger.log(Level.FINE, String.format("Successfully added %s users, %s persons, and %s events to the database.", lr.getUserList().length, lr.getPersonList().length, lr.getEventList().length));
        logger.log(Level.INFO, "Exiting load.");
        return new Result(String.format("Successfully added %s users, %s persons, and %s events to the database.", lr.getUserList().length, lr.getPersonList().length, lr.getEventList().length));
    }
}