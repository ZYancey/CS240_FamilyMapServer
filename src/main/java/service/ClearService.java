package service;

import result.*;
import data_access.*;

public class ClearService {
    public ClearService() {}

    public Result clear() {
        Database db = new Database();

        try {
            db.getAuthData().clearTables();
            db.getPersonData().clearTables();
            db.getEventData().clearTables();
            db.getUserData().clearTables();
            db.closeConnection(true);

        } catch (DataAccessException e) {
            db.closeConnection(false);
            return new Result("Clear failed");
        }
        return new Result("Clear succeeded.");
    }
}
