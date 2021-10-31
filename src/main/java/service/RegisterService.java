package service;

import java.util.UUID;

import result.*;
import request.*;
import model.*;
import data_access.*;

public class RegisterService {
    public RegisterService(){}

    public AuthResult register(RegisterRequest request){
        System.out.println("REGISTER Service Entered");
        Database tempDB = new Database();
        Person person = new Person(request.getFirstName(),
                request.getLastName(),
                request.getGender(),
                UUID.randomUUID().toString(),
                "",
                "",
                "",
                request.getUsername());
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

            DataGenerator data = new DataGenerator();
            //Make a fake LoadRequest object just to get the data back from the DataGenerator.
            ListResult lr = data.GenerateDefaultAncestorData(person);
            for(int i = 0; i < lr.getPersonList().length; i++) {
                tempDB.getPersonData().addPerson(lr.getPersonList()[i]);
            }

            for(int j = 0; j < lr.getEventList().length; j++) {
                tempDB.getEventData().addEvent(lr.getEventList()[j]);
            }

            tempDB.closeConnection(true);
            LoginService loginService = new LoginService();
            return loginService.login(new LoginRequest(request.getUsername(), request.getPassword()));

        }catch(DataAccessException e){
            tempDB.closeConnection(false);
            return new AuthResult(String.format("Error : %s", e.getLocalizedMessage()));
        }
    }

    private boolean verifyNewUser(Database tempDB, String username) {
        try{
            return tempDB.getUserData().getUser(username) == null;
        }catch(DataAccessException e){
            return true;
        }
    }
}