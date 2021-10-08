package service;

import result.EventResult;
import request.EventRequest;
import model.*;
import data_access.*;


/**
 * Returns ALL events for ALL family members of the current user. The current user is determined from the provided auth token.
 */
public class EventService {

    /**
     *  Outputs Response Body
     */
    private String message;


    /**
     *  Tracks if operation was successful or not
     */
    private boolean success;


    /**
     * Returns ALL events for ALL family members of the current user. The current user is determined from the provided auth token.
     *
     * @param request the request in question
     * @return
     */
    EventResult clear(EventRequest request){
        return null;
    }
}