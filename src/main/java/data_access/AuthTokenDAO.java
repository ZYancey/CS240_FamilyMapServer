package data_access;

import model.AuthToken;

import java.sql.*;
import java.util.ArrayList;

public class AuthTokenDAO {

    /**The general constructor for an AuthDAO object.
     * @param c					the database Connection object*/
    public AuthTokenDAO(Connection c) {
        setConnection(c);
    }

    /**A reference to the database Connection object*/
    private Connection c;



    /**Sets the connection for the AuthDAO object.
     * @param c					the database Connection object*/
    public void setConnection(Connection c) { this.c = c; }

    /**@return					the database Connection object*/
    public Connection getConnection() { return c; }

    /**Adds an AuthToken's information to the database.
     * @param a					the AuthToken object
     * @throws 					DataAccessException */
    public void addAuthToken(AuthToken a) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "INSERT INTO authtoken (authtoken, username, personID) VALUES (?, ?, ?);";
                stmt = c.prepareStatement(sql);

                //Fill statement with the AuthToken parameters.
                stmt.setString(1, a.getAuthToken());
                stmt.setString(2, a.getUserName());
                stmt.setString(3, a.getPersonID());

                //Execute the finalized statement.
                stmt.executeUpdate();
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Add AuthToken failed. : %s", e.getLocalizedMessage()));
        }
    }

    /**Deletes an existing database entry for an AuthToken.
     * @param a					the AuthToken object to be removed
     * @throws 					DataAccessException */
    public void deleteAuthToken(AuthToken a) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "DELETE FROM authtoken WHERE authtoken = ?;";
                stmt = c.prepareStatement(sql);

                //Fill statement with the AuthTokenID.
                stmt.setString(1, a.getAuthToken());

                //Execute the finalized statement.
                stmt.executeUpdate();
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Delete AuthToken failed. : %s", e.getLocalizedMessage()));
        }
    }

    /**Deletes all AuthToken information from the database.
     * @throws 						DataAccessException */
    public void deleteAllauthtoken() throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "DELETE FROM authtoken;";
                stmt = c.prepareStatement(sql);

                //No parameters to add to the statement, so proceed to execution.
                stmt.executeUpdate();
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Delete All authtoken failed. : %s", e.getLocalizedMessage()));
        }
    }

    /**Retrieves the information for an AuthToken in the database.
     * @param authID				the identifier for the AuthToken to be returned
     * @return						an AuthToken object representing the information in the database.
     * @throws DataAccessException 	if it is unable to get an AuthToken*/
    public AuthToken getAuthToken(String authID) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "SELECT * FROM authtoken WHERE authtoken = ?;";
                stmt = c.prepareStatement(sql);

                //Fill statement with the given authID.
                stmt.setString(1, authID);

                //Execute the query and add the values to a new object to be returned.
                ResultSet rs = stmt.executeQuery();
                return new AuthToken(rs.getString("authtoken"), rs.getString("username"), rs.getString("personID"));
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Get AuthToken failed. : %s", e.getLocalizedMessage()));
        }
    }

    /**Retrieves all information for all authtoken in the database.
     * @return 						an array of AuthToken objects representing all the information in the AuthToken table of the database.
     * @throws DataAccessException	if unable to get any authtoken */
    public AuthToken[] getAllauthtoken() throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "SELECT * FROM authtoken;";
                stmt = c.prepareStatement(sql);

                //No additional parameters to add, so we execute the query.
                ResultSet rs = stmt.executeQuery();

                //Iterate through the ResultSet and create new authtoken to be returned in the final Set.
                ArrayList<AuthToken> res = new ArrayList<AuthToken>();
                while(rs.next()) {
                    String id = rs.getString("authtoken");
                    String user = rs.getString("username");
                    String personID = rs.getString("personID");
                    res.add(new AuthToken(id, user, personID));
                }
                AuthToken[] all = new AuthToken[res.size()];
                res.toArray(all);
                return all;
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Get All authtoken failed. : %s", e.getLocalizedMessage()));
        }
    }

    public void clearTables() throws DataAccessException {

        try (Statement stmt = c.createStatement()){
            String sql = "DELETE FROM authtoken";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

}

