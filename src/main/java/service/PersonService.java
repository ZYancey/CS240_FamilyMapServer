package service;

import result.PersonResult;
import request.PersonRequest;
import model.*;
import data_access.*;


/**
 * Returns ALL family members of the current user. The current user is determined from the provided auth token.
 */
public class PersonService {

    private String message;
    private boolean success;


    /**
     * Returns ALL family members of the current user. The current user is determined from the provided auth token.
     *
     * @param request the request in question
     * @return
     */
    public PersonResult getAllPersons(PersonRequest request){
        return null;
    }

    /**
     * Returns a single family member of the current user. The current user is determined from the provided auth token.
     *
     * @param request the request in question
     * @return
     */
    public PersonResult getPerson(PersonRequest request){
        return null;
    }

}