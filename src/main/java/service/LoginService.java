package service;

import result.LoginResult;
import request.LoginRequest;
import model.*;
import data_access.*;


/**
 * Logs in the user and returns an auth token.
 */
public class LoginService {

    /**
     *  Outputs Response Body
     */
    private String message;


    /**
     *  Tracks if operation was successful or not
     */
    private boolean success;


    /**
     * Logs in the user and returns an auth token.
     *
     * @param request the request in question
     * @return
     */
    LoginResult login(LoginRequest request){
        return null;
    }
}