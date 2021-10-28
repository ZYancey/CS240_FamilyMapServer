package data_access;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;

import model.User;
public class UserDAO {
    /**The general constructor for a UserDAO object.
     * @param c			the database Connection object*/
    public UserDAO(Connection c) {
        setConnection(c);
    }

    /**The Logger object to log statements on the server log.*/
    private static Logger logger;
    static { logger = Logger.getLogger("familymaptest"); }

    /**A reference to the database Connection object*/
    private Connection c;



    /**Sets the connection for the UserDAO object.
     * @param c			the database Connection object*/
    public void setConnection(Connection c) { this.c = c; }

    /**@return			the database Connection object*/
    public Connection getConnection() { return c; }

    /**Adds a User's information to the database.
     * @throws 			DataAccessException */
    public void addUser(User user) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "INSERT INTO user (Username, Password, Email, FirstName, LastName, Gender, PersonID) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?)";
                stmt = c.prepareStatement(sql);

                //Fill the statement with the User's parameters.
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getFirstName());
                stmt.setString(5, user.getLastName());
                stmt.setString(6, user.getGender());
                stmt.setString(7, user.getPersonID());

                //Execute the finalized statement.
                logger.log(Level.FINEST, "Adding User");
                stmt.executeUpdate();
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            if(e.getLocalizedMessage().contains("not unique")) {
                throw new DataAccessException("Username already registered in the database.");
            }
            throw new DataAccessException(String.format("Add User failed. : %s", e.getLocalizedMessage()));
        }
    }

    /**Modifies an existing database entry for a User.*/
    public void modifyUser(User user) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "UPDATE user SET password=?, email=?, firstName=?, lastName=?, gender=?, personID=? WHERE Username=?;";
                stmt = c.prepareStatement(sql);

                //Fill statement with the User's parameters.
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getFirstName());
                stmt.setString(5, user.getLastName());
                stmt.setString(6, user.getGender());
                stmt.setString(7, user.getPersonID());

                //Execute the finalized statement.
                logger.log(Level.FINEST, "Modifying User");
                stmt.executeUpdate();
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Modify User failed. : %s", e.getLocalizedMessage()));
        }
    }

    /**Deletes an existing database entry for a User.
     * @throws 			DataAccessException */
    public void deleteUser(User u) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "DELETE FROM user WHERE Username=?;";
                stmt = c.prepareStatement(sql);

                //Fill statement with the User's Username.
                stmt.setString(1, u.getUsername());

                //Execute the finalized statement.
                logger.log(Level.FINEST, "Deleting User");
                stmt.executeUpdate();
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Delete User failed. : %s", e.getLocalizedMessage()));
        }

    }

    /**Deletes all User information from the database.
     * @throws 			DataAccessException */
    public void deleteAllUsers() throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "DELETE FROM user;";
                stmt = c.prepareStatement(sql);

                //No extra parameters to add to the statement, so proceed to execution.
                logger.log(Level.FINEST, "Deleting all Users");
                stmt.executeUpdate();
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Delete all Users failed. : %s", e.getLocalizedMessage()));
        }
    }

    /**Retrieves the information for a User in the database.
     * @return			a User object representing the information in the database.
     * @throws 			DataAccessException */
    public User getUser(String Username) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "SELECT * FROM user WHERE Username=?;";
                stmt = c.prepareStatement(sql);

                //Fill statement with the given Username.
                stmt.setString(1, Username);

                //Execute the finalized query, and construct a User object using the data from the ResultSet.
                logger.log(Level.FINEST, "Getting User");
                ResultSet rs = stmt.executeQuery();
                if(!rs.next()) { throw new DataAccessException("Get User failed. : User not found."); }
                return new User(
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("Email"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Gender"),
                        rs.getString("PersonID"));
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Get User failed. : %s", e.getLocalizedMessage()));
        }
    }

    /**Retrieves all information for all Persons in the database.
     * @return 			an array of User objects representing all the information in the User table of the database.
     * @throws 			DataAccessException */
    public User[] getAllUsers() throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "SELECT * FROM user;";
                stmt = c.prepareStatement(sql);

                //No extra parameters to add to the statement, so proceed to execution.
                logger.log(Level.FINEST, "Getting all Users");
                ResultSet rs = stmt.executeQuery();

                //Iterate through the ResultSet and use the data to build User objects to add to the Set.
                ArrayList<User> res = new ArrayList<User>();
                while(rs.next()) {
                    String u = rs.getString("Username");
                    String p = rs.getString("Password");
                    String e = rs.getString("Email");
                    String f = rs.getString("FirstName");
                    String l = rs.getString("LastName");
                    String g = rs.getString("Gender");
                    String id = rs.getString("PersonID");
                    res.add(new User(u, p, e, f, l, g, id));
                }
                User[] all = new User[res.size()];
                res.toArray(all);
                return all;
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch(SQLException e) {
            throw new DataAccessException(String.format("Get All Users failed. : %s", e.getLocalizedMessage()));
        }
    }

    public void clearTables() throws DataAccessException {
        try (Statement stmt = c.createStatement()){
            String sql = "DELETE FROM user";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

}