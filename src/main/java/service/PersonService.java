package service;

import result.PersonResult;
import request.PersonRequest;
import model.*;
import data_access.*;


/**
 * Returns ALL family members of the current user. The current user is determined from the provided auth token.
 */
public class PersonService {

    /**
     *  Outputs Response Body
     */
    private String message;


    /**
     *  Tracks if operation was successful or not
     */
    private boolean success;


    /**
     * Returns ALL family members of the current user. The current user is determined from the provided auth token.
     *
     * @param request the request in question
     * @return
     */
    PersonResult person(PersonRequest request){
        return null;
    }
}