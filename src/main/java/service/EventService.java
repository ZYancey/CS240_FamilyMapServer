package service;

import result.EventResult;
import request.EventRequest;
import model.*;
import data_access.*;


/**
 * Returns ALL events for ALL family members of the current user. The current user is determined from the provided auth token.
 */
public class EventService {

    private String message;
    private boolean success;


    public EventResult getAllEvents(EventRequest request){
        return null;
    }
    public EventResult getEvent(EventRequest request){
        return null;
    }
}