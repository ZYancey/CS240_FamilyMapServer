package service;

import result.LoadResult;
import request.LoadRequest;
import model.*;
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
    LoadResult load(LoadRequest request){
        return null;
    }
}