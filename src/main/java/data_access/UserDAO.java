package data_access;

import java.sql.*;

import model.User;
public class UserDAO {
    public UserDAO(Connection conn) {
        setConnection(conn);
    }
    private Connection conn;
    public void setConnection(Connection conn) { this.conn = conn; }
    public Connection getConnection() { return conn; }

    public void addUser(User user) throws DataAccessException {
        PreparedStatement statement = null;
        try {
            try {
                String sql = "INSERT INTO user (Username, Password, Email, FirstName, LastName, Gender, PersonID) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?)";
                statement = conn.prepareStatement(sql);

                //Fill the statement with the User's parameters.
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getEmail());
                statement.setString(4, user.getFirstName());
                statement.setString(5, user.getLastName());
                statement.setString(6, user.getGender());
                statement.setString(7, user.getPersonID());

                statement.executeUpdate();
            }
            finally {
                if(statement != null) {
                    statement.close();
                }
            }
        } catch (SQLException e) {
            if(e.getLocalizedMessage().contains("not unique")) {
                throw new DataAccessException("Username already registered in the database.");
            }
            throw new DataAccessException(String.format("Add User failed. : %s", e.getLocalizedMessage()));
        }
    }

    public User getUser(String Username) throws DataAccessException {
        PreparedStatement statement = null;
        try {
            try {
                String sql = "SELECT * FROM user WHERE Username=?;";
                statement = conn.prepareStatement(sql);

                statement.setString(1, Username);

                ResultSet rs = statement.executeQuery();
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
                if(statement != null) {
                    statement.close();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Get User failed. : %s", e.getLocalizedMessage()));
        }
    }

    public void modifyUser(User user) throws DataAccessException {
        PreparedStatement statement = null;
        try {
            try {
                String sql = "UPDATE user SET password=?, email=?, firstName=?, lastName=?, gender=?, personID=? WHERE Username=?;";
                statement = conn.prepareStatement(sql);

                statement.setString(1, user.getUsername());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getEmail());
                statement.setString(4, user.getFirstName());
                statement.setString(5, user.getLastName());
                statement.setString(6, user.getGender());
                statement.setString(7, user.getPersonID());

                statement.executeUpdate();
            }
            finally {
                if(statement != null) {
                    statement.close();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Modify User failed. : %s", e.getLocalizedMessage()));
        }
    }

    public void clearTables() throws DataAccessException {
        try (Statement statement = conn.createStatement()){
            String sql = "DELETE FROM user";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

}