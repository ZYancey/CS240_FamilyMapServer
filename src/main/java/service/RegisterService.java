package service;

import java.util.UUID;

import result.*;
import request.*;
import model.*;
import data_access.*;

public class RegisterService {
    public RegisterService(){}

    public AuthResult register(RegisterRequest registerRequest){
        System.out.println("REGISTER Service Entered");
        Database tempDB = new Database();

        Person person = new Person(
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getGender(),
                UUID.randomUUID().toString(),
                "",
                "",
                "",
                registerRequest.getUsername());

        User user = new User(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                registerRequest.getEmail(),
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getGender(),
                person.getPersonID());


        try{
            if(!verifyNewUser(tempDB, user.getUsername())){
                System.out.println("NOT UNIQUE");
                throw new DataAccessException("User not Unique");
            }

            tempDB.getUserData().addUser(user);

            GenerationService data = new GenerationService();
            ListResult listResult = data.GenerateDefaultAncestorData(person);
            for(int i = 0; i < listResult.getPersonList().length; i++) {
                tempDB.getPersonData().addPerson(listResult.getPersonList()[i]);
            }

            for(int j = 0; j < listResult.getEventList().length; j++) {
                tempDB.getEventData().addEvent(listResult.getEventList()[j]);
            }

            tempDB.closeConnection(true);
            LoginService loginService = new LoginService();
            return loginService.login(new LoginRequest(registerRequest.getUsername(), registerRequest.getPassword()));

        }catch(DataAccessException exception){
            tempDB.closeConnection(false);
            return new AuthResult(String.format("Error : %s", exception.getLocalizedMessage()));
        }
    }

    private boolean verifyNewUser(Database tempDB, String username) {
        try{
            return tempDB.getUserData().getUser(username) == null;
        }catch(DataAccessException exception){
            return true;
        }
    }
}