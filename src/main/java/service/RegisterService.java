package service;

import java.util.UUID;

import result.*;
import request.*;
import model.*;
import data_access.*;


/**
 * Creates a new user account, generates 4 generations of ancestor data for the new user, logs the user in, and returns an auth token.
 */
public class RegisterService {
    public RegisterService(){}
    //private String message;
    //private boolean success;

    public AuthResult register(RegisterRequest request){
        System.out.println("REGISTER Service Entered");
        Database tempDB = new Database();
        Person person = new Person(UUID.randomUUID().toString(),
                request.getUsername(),
                request.getFirstName(),
                request.getLastName(),
                request.getGender(),
                "",
                "",
                "");
        User user = new User(request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                request.getFirstName(),
                request.getLastName(),
                request.getGender(),
                person.getPersonID());

        try{
            if(!verifyNewUser(tempDB, user.getUsername())){
                System.out.println("NOT UNIQUE");
                throw new DataAccessException("User not Unique");
            }

            tempDB.getUserData().addUser(user);

            System.out.println("ATTEMPTING TO CREATE TREE DATA");
            //TODO GENERATE FAKE FAMILY TREE DATA HERE

            tempDB.closeConnection(true);

            LoginService loginService = new LoginService();
            return loginService.login(new LoginRequest(request.getUsername(), request.getPassword()));

        }catch(DataAccessException e){

            try {
                tempDB.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }

            return new AuthResult(String.format("Failed to register : %s", e.getLocalizedMessage()));
        }

    }

    private boolean verifyNewUser(Database tempDB, String username) {
        try{
            if(tempDB.getUserData().getUser(username) != null){
                return false;
            }
            else{
                return true;
            }
        }catch(DataAccessException e){
            return true;
        }
    }
}