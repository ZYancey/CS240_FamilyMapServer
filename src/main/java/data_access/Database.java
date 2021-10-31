package data_access;

import java.sql.*;

public class Database {
    private Connection conn;
    private final AuthTokenDAO AuthData;
    private final UserDAO UserData;
    private final EventDAO EventData;
    private final PersonDAO PersonData;

    public Database(){
        try {
            getConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        AuthData = new AuthTokenDAO(conn);
        UserData = new UserDAO(conn);
        EventData = new EventDAO(conn);
        PersonData = new PersonDAO(conn);
    }

    //Whenever we want to make a change to our database we will have to open a connection and use
    //Statements created by that connection to initiate transactions
    public Connection openConnection() throws DataAccessException {
        try {
            //The Structure for this Connection is driver:language:path
            //The path assumes you start in the root of your project unless given a non-relative path
            final String CONNECTION_URL = "jdbc:sqlite:familymap.sqlite";

            // Open a database connection to the file given in the path
            conn = DriverManager.getConnection(CONNECTION_URL);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }

        return conn;
    }
    public Connection getConnection() throws DataAccessException {
        if(conn == null) {
            return openConnection();
        } else {
            return conn;
        }
    }

    public AuthTokenDAO getAuthData(){return AuthData;}
    public UserDAO getUserData(){return UserData;}
    public EventDAO getEventData(){return EventData;}
    public PersonDAO getPersonData(){return PersonData;}


    //When we are done manipulating the database it is important to close the connection. This will
    //End the transaction and allow us to either commit our changes to the database or rollback any
    //changes that were made before we encountered a potential error.

    //IMPORTANT: IF YOU FAIL TO CLOSE A CONNECTION AND TRY TO REOPEN THE DATABASE THIS WILL CAUSE THE
    //DATABASE TO LOCK. YOUR CODE MUST ALWAYS INCLUDE A CLOSURE OF THE DATABASE NO MATTER WHAT ERRORS
    //OR PROBLEMS YOU ENCOUNTER

    public void closeConnection(boolean commit) {
        try {
            try {
                if(commit) { conn.commit(); }
                if(!commit) { conn.rollback(); }
                conn.close();
                conn = null;
            } catch (SQLException e) {
                throw new DataAccessException("closeConnection failed");
            }
        } catch (DataAccessException close) {

        }
    }
    public void clearTables() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM event";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM person";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM user";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}

