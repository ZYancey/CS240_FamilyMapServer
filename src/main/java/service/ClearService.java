package service;

import result.*;
import data_access.*;


/**
 * Deletes ALL data from the database, including user accounts, auth tokens, and generated person and event data.
 */
public class ClearService {

    public ClearService() {}

    public Result clear() {
        //Create Database Access Objects.
        Database db = new Database();

        try {
            //AuthToken deletion.
            db.getAuthData().clearTables();
            //Person deletion.
            db.getPersonData().clearTables();
            //Event deletion.
            db.getEventData().clearTables();
            //User deletion.
            db.getUserData().clearTables();

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            return new Result("Clear failed");
        }
        //All deletions passed.
        return new Result("Clear succeeded.");
    }
}
