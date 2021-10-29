package service;

import java.util.logging.*;

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
                //Get AuthToken
                token = db.getAuthData().getAuthToken(er.getAuthTokenID());
                if(token == null) {
                    db.closeConnection(false);
                    return new EventResult("Invalid AuthTokenID.");
                }
            } catch (DataAccessException e) {
                db.closeConnection(false);
                return new EventResult("Invalid AuthTokenID.");
            }

            //Get Persons based on userName from the token.
            Event[] results = db.getEventData().getAllEvents(token.getUserName());
            db.closeConnection(true);
            return new EventResult(results);
        } catch (DataAccessException evt) {
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            return new EventResult(String.format("Failed to get all Events : %s", evt.getLocalizedMessage()));
        }
    }

    /**Finds a specific Event based on the eventID.
     * @param er			the EventRequest with the eventID to be found.
     * @return				an EventResult object with the response data, or <code>null</code> if not found.*/
    public EventResult getEvent(EventRequest er) {
        Database db = new Database();
        try {
            AuthToken token;
            try {
                //Get AuthToken
                token = db.getAuthData().getAuthToken(er.getAuthTokenID());
                if(token == null) {
                    return new EventResult("Invalid AuthTokenID.");
                }
            } catch (DataAccessException e) {
                return new EventResult("Invalid AuthTokenID.");
            }

            Event result = db.getEventData().getEvent(er.getEventID());
            if(!result.getUsername().equals(token.getUserName())) {
                throw new DataAccessException("Not authorized to access that event.");
            }
            db.closeConnection(true);
            return new EventResult(result);
        } catch (DataAccessException evt) {
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            return new EventResult(String.format("Failed to get Event : %s", evt.getLocalizedMessage()));
        }
    }
}