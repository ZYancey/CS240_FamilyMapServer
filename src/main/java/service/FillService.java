package service;

import java.util.Objects;
import java.util.UUID;
import model.*;

import data_access.*;
import request.FillRequest;
import result.*;

public class FillService {
    public FillService() {}


    public Result fill(FillRequest fillRequest) {
        Database database = new Database();
        int persons = 0;
        int events = 0;
        try {
            //Validate User registration.
            if(AlreadyRegistered(fillRequest.getUsername(), database)) {
                ListResult clear = new ListResult(database.getPersonData().getAllPersons(fillRequest.getUsername()), database.getEventData().getAllEvents(fillRequest.getUsername()));
                for(int i = 0; i < clear.getPersonList().length; i++) {
                    database.getPersonData().deletePerson(clear.getPersonList()[i]);
                }
                for(int j = 0; j < clear.getEventList().length; j++) {
                    database.getEventData().deleteEvent(clear.getEventList()[j]);
                }

                User u = database.getUserData().getUser(fillRequest.getUsername());
                Person p = new Person(u.getFirstName(), u.getLastName(), u.getGender(), UUID.randomUUID().toString(),  "", "", "", u.getUsername());


                u.setPersonID(p.getPersonID());
                database.getUserData().modifyUser(u);

                GenerationService generationService = new GenerationService();
                ListResult listResult = null;

                if(fillRequest.getGenerations() == 0) {
                    listResult = generationService.GenerateDefaultAncestorData(p);
                }
                else if(fillRequest.getGenerations() > 0) {
                    listResult = generationService.GenerateAncestorData(p, fillRequest.getGenerations());
                }
                else if(fillRequest.getGenerations() < 0) {
                    database.closeConnection(false);
                    return new Result("Invalid number of generations on the fill request. It must be greater than or equal to 0.");
                }

                for(int a = 0; a < Objects.requireNonNull(listResult).getPersonList().length; a++) {
                    database.getPersonData().addPerson(listResult.getPersonList()[a]);
                }

                for(int b = 0; b < listResult.getEventList().length; b++) {
                    database.getEventData().addEvent(listResult.getEventList()[b]);
                }

                persons = listResult.getPersonList().length;
                events = listResult.getEventList().length;

                database.closeConnection(true);
            }
        } catch (DataAccessException e) {
            database.closeConnection(false);
            return new Result(String.format("Fill failed. : %s", e.getLocalizedMessage()));
        }
        return new Result(String.format("Successfully added %s persons and %s events to the database.", persons, events));
    }

    private boolean AlreadyRegistered(String userName, Database db) throws DataAccessException {
        try {
            db.getUserData().getUser(userName);
            return true;
        } catch (DataAccessException e) {
            throw new DataAccessException("User not already registered.");
        }
    }
}