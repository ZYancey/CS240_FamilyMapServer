package data_access;

import java.sql.*;

import model.User;
public class UserDAO {
    public UserDAO(Connection c) {
        setConnection(c);
    }
    private Connection c;
    public void setConnection(Connection c) { this.c = c; }
    public Connection getConnection() { return c; }

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

    public void modifyUser(User user) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "UPDATE user SET password=?, email=?, firstName=?, lastName=?, gender=?, personID=? WHERE Username=?;";
                stmt = c.prepareStatement(sql);

                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getFirstName());
                stmt.setString(5, user.getLastName());
                stmt.setString(6, user.getGender());
                stmt.setString(7, user.getPersonID());

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


    public User getUser(String Username) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "SELECT * FROM user WHERE Username=?;";
                stmt = c.prepareStatement(sql);

                stmt.setString(1, Username);

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

    public void clearTables() throws DataAccessException {
        try (Statement stmt = c.createStatement()){
            String sql = "DELETE FROM user";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

}