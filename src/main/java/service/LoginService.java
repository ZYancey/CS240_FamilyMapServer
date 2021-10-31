package service;

import data_access.*;
import request.LoginRequest;
import result.AuthResult;
import java.util.UUID;
import model.*;


public class LoginService {
    public LoginService() {}

    public AuthResult login(LoginRequest lr) {
        Database db = new Database();
        User entry;
        try {
            try {
                entry = db.getUserData().getUser(lr.getUsername());
            } catch (DataAccessException e) {
                db.closeConnection(false);
                return new AuthResult("Error : User not registered.");
            }

            if(!entry.getPassword().equals(lr.getPassword())) {
                db.closeConnection(false);
                return new AuthResult("Error : The password entered did not match the password on file.");
            }

            String authID = UUID.randomUUID().toString();
            AuthToken a = new AuthToken(authID, lr.getUsername(), entry.getPersonID());

            db.getAuthData().addAuthToken(a);
            db.closeConnection(true);
            return new AuthResult(a);

        } catch (DataAccessException notFound) {
            db.closeConnection(false);
            return new AuthResult(String.format("Error : %s", notFound.getLocalizedMessage()));
        }
    }
}