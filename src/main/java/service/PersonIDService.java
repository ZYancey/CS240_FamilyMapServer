package service;

import result.PersonIDResult;
import request.PersonIDRequest;
import model.*;
import data_access.*;


/**
 * Returns the single Person object with the specified ID.
 */
public class PersonIDService {

    /**
     *  Outputs Response Body
     */
    private String message;


    /**
     *  Tracks if operation was successful or not
     */
    private boolean success;


    /**
     * Returns the single Person object with the specified ID.
     *
     * @param request the request in question
     * @return
     */
    PersonIDResult personID(PersonIDRequest request){
        return null;
    }
}