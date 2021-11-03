package service;


import data_access.*;
import request.LoadRequest;
import result.Result;

public class LoadService {
    public LoadService() {}


    public Result load(LoadRequest loadRequest) {
        ClearService clearService = new ClearService();
        Result clear = clearService.clear();
        if(!clear.getMessage().equals("Clear succeeded.")) {
            return new Result(String.format("::Load:: Failed to clear database : %s", clear.getMessage()));
        }

        Database database = new Database();

        try {
            for(int i = 0; i < loadRequest.getUserList().length; i++) {
                database.getUserData().addUser(loadRequest.getUserList()[i]);
            }

            for(int j = 0; j < loadRequest.getPersonList().length; j++) {
                database.getPersonData().addPerson(loadRequest.getPersonList()[j]);
            }

            for(int k = 0; k < loadRequest.getEventList().length; k++) {
                database.getEventData().addEvent(loadRequest.getEventList()[k]);
            }

            database.closeConnection(true);

        } catch (DataAccessException exception) {
            database.closeConnection(false);
            return new Result(String.format("::Load:: Failed to load data : %s", exception.getLocalizedMessage()));
        }
        return new Result(String.format("Successfully added %s users, %s persons, and %s events to the database.",
                loadRequest.getUserList().length, loadRequest.getPersonList().length, loadRequest.getEventList().length));
    }
}