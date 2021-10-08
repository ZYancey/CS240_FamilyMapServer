package service;

import result.ClearResult;
import request.ClearRequest;
import model.*;
import data_access.*;


/**
 * Deletes ALL data from the database, including user accounts, auth tokens, and generated person and event data.
 */
public class ClearService {

    /**
     *  Outputs Response Body
     */
    private String message;


    /**
     *  Tracks if operation was successful or not
     */
    private boolean success;


    /**
     * Deletes all data and returns if the function was success
     *
     * @param request the clear request in question
     * @return
     */
    ClearResult clear(ClearRequest request){
        return null;
    }
}
