package service;

import result.RegisterResult;
import request.RegisterRequest;
import model.*;
import data_access.*;


/**
 * Creates a new user account, generates 4 generations of ancestor data for the new user, logs the user in, and returns an auth token.
 */
public class RegisterService {

    /**
     *  Outputs Response Body
     */
    private String message;


    /**
     *  Tracks if operation was successful or not
     */
    private boolean success;


    /**
     * Creates a new user account, generates 4 generations of ancestor data for the new user, logs the user in, and returns an auth token.
     *
     * @param request the request in question
     * @return
     */
    RegisterResult register(RegisterRequest request){
        return null;
    }
}