package service;

import model.*;
import data_access.*;
import request.EventRequest;
import result.EventResult;

public class EventService {
    public EventService() {}

    public EventResult getAll(EventRequest eventRequest) {
        Database database = new Database();
        try {
            AuthToken token;
            try {
                token = database.getAuthData().getAuthToken(eventRequest.getAuthTokenID());
                if(token == null) {
                    database.closeConnection(false);
                    return new EventResult("Error: Invalid AuthTokenID.");
                }
            } catch (DataAccessException exception) {
                database.closeConnection(false);
                return new EventResult("Error: Invalid AuthTokenID.");
            }

            Event[] results = database.getEventData().getAllEvents(token.getUserName());
            database.closeConnection(true);
            return new EventResult(results);
        } catch (DataAccessException exception) {
            database.closeConnection(false);
            return new EventResult(String.format("Error: Failed to get all Events : %s", exception.getLocalizedMessage()));
        }
    }

    public EventResult getEvent(EventRequest eventRequest) {
        Database database = new Database();
        try {
            AuthToken token;
            try {
                //Get AuthToken
                token = database.getAuthData().getAuthToken(eventRequest.getAuthTokenID());
                if(token == null) {
                    database.closeConnection(false);
                    return new EventResult("Error: Invalid AuthTokenID.");
                }
            } catch (DataAccessException exception) {
                database.closeConnection(false);
                return new EventResult("Error: Invalid AuthTokenID.");
            }

            Event result = database.getEventData().getEvent(eventRequest.getEventID());
            if(!result.getUsername().equals(token.getUserName())) {
                throw new DataAccessException("Error: Not authorized to access that event.");
            }
            database.closeConnection(true);
            return new EventResult(result);

        } catch (DataAccessException exception) {
            database.closeConnection(false);
            return new EventResult(String.format("Error : %s", exception.getLocalizedMessage()));
        }
    }
}