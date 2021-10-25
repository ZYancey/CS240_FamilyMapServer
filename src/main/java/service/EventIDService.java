package service;

import result.EventIDResult;
import request.EventIDRequest;
import model.*;
import data_access.*;


/**
 * Returns the single Event object with the specified ID.
 */
public class EventIDService {

    /**
     *  Outputs Response Body
     */
    private String message;


    /**
     *  Tracks if operation was successful or not
     */
    private boolean success;


    /**
     * Returns the single Event object with the specified ID.
     *
     * @param request the request in question
     * @return
     */
    EventIDResult eventID(EventIDRequest request){
        return null;
    }
}
