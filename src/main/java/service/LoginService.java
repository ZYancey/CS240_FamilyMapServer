package service;

import data_access.*;
import request.LoginRequest;
import result.AuthResult;
import java.util.UUID;
import model.*;


public class LoginService {
    public LoginService() {}

    public AuthResult login(LoginRequest loginRequest) {
        Database database = new Database();
        User attemptedLogin;
        try {
            try {
                attemptedLogin = database.getUserData().getUser(loginRequest.getUsername());
            } catch (DataAccessException exception) {
                database.closeConnection(false);
                return new AuthResult("Error : User not registered.");
            }

            if(!attemptedLogin.getPassword().equals(loginRequest.getPassword())) {
                database.closeConnection(false);
                return new AuthResult("Error : The password entered did not match the password on file.");
            }

            String authID = UUID.randomUUID().toString();
            AuthToken authToken = new AuthToken(authID, loginRequest.getUsername(), attemptedLogin.getPersonID());

            database.getAuthData().addAuthToken(authToken);
            database.closeConnection(true);
            return new AuthResult(authToken);

        } catch (DataAccessException exception) {
            database.closeConnection(false);
            return new AuthResult(String.format("Error : %s", exception.getLocalizedMessage()));
        }
    }
}