package service;

import model.*;
import data_access.*;
import request.EventRequest;
import result.EventResult;

public class EventService {
    public EventService() {}

    public EventResult getAll(EventRequest er) {
        Database db = new Database();
        try {
            AuthToken token;
            try {
                token = db.getAuthData().getAuthToken(er.getAuthTokenID());
                if(token == null) {
                    db.closeConnection(false);
                    return new EventResult("Error: Invalid AuthTokenID.");
                }
            } catch (DataAccessException e) {
                db.closeConnection(false);
                return new EventResult("Error: Invalid AuthTokenID.");
            }

            Event[] results = db.getEventData().getAllEvents(token.getUserName());
            db.closeConnection(true);
            return new EventResult(results);
        } catch (DataAccessException evt) {
            db.closeConnection(false);
            return new EventResult(String.format("Error: Failed to get all Events : %s", evt.getLocalizedMessage()));
        }
    }

    public EventResult getEvent(EventRequest er) {
        Database db = new Database();
        try {
            AuthToken token;
            try {
                //Get AuthToken
                token = db.getAuthData().getAuthToken(er.getAuthTokenID());
                if(token == null) {
                    db.closeConnection(false);
                    return new EventResult("Error: Invalid AuthTokenID.");
                }
            } catch (DataAccessException e) {
                db.closeConnection(false);
                return new EventResult("Error: Invalid AuthTokenID.");
            }

            Event result = db.getEventData().getEvent(er.getEventID());
            if(!result.getUsername().equals(token.getUserName())) {
                throw new DataAccessException("Error: Not authorized to access that event.");
            }
            db.closeConnection(true);
            return new EventResult(result);
        } catch (DataAccessException evt) {
            db.closeConnection(false);
            return new EventResult(String.format("Error : %s", evt.getLocalizedMessage()));
        }
    }
}