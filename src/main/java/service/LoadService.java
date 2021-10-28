package service;

import result.Result;
import request.LoadRequest;
import data_access.*;


/**
 * Clears all data from the database (just like the /clear API), and then loads the posted user, person, and event data into the database.
 */
public class LoadService {

    /**
     *  Outputs Response Body
     */
    private String message;


    /**
     *  Tracks if operation was successful or not
     */
    private boolean success;


    /**
     * Clears all data from the database (just like the /clear API), and then loads the posted user, person,
     * and event data into the database.
     *
     * @param request the request in question
     * @return
     */
    Result load(LoadRequest request){
        return null;
    }
}