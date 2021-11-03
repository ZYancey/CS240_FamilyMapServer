package service;

import result.*;
import data_access.*;

public class ClearService {
    public ClearService() {}

    public Result clear() {
        Database database = new Database();

        try {
            database.getAuthData().clearTables();
            database.getPersonData().clearTables();
            database.getEventData().clearTables();
            database.getUserData().clearTables();
            database.closeConnection(true);

        } catch (DataAccessException exception) {
            database.closeConnection(false);
            return new Result("Clear failed");
        }
        return new Result("Clear succeeded.");
    }
}
