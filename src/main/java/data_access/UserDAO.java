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
        PreparedStatement preparedStatement = null;
        try {
            try {
                String sql = "INSERT INTO user (Username, Password, Email, FirstName, LastName, Gender, PersonID) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?)";
                preparedStatement = conn.prepareStatement(sql);

                //Fill the statement with the User's parameters.
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setString(4, user.getFirstName());
                preparedStatement.setString(5, user.getLastName());
                preparedStatement.setString(6, user.getGender());
                preparedStatement.setString(7, user.getPersonID());

                preparedStatement.executeUpdate();
            }
            finally {
                if(preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        } catch (SQLException exception) {
            if(exception.getLocalizedMessage().contains("not unique")) {
                throw new DataAccessException("Username already registered in the database.");
            }
            throw new DataAccessException(String.format("Add User failed. : %s", exception.getLocalizedMessage()));
        }
    }

    public User getUser(String Username) throws DataAccessException {
        PreparedStatement preparedStatement = null;
        try {
            try {
                String sql = "SELECT * FROM user WHERE Username=?;";
                preparedStatement = conn.prepareStatement(sql);

                preparedStatement.setString(1, Username);

                ResultSet resultSet = preparedStatement.executeQuery();
                if(!resultSet.next()) { throw new DataAccessException("Get User failed. : User not found."); }
                return new User(
                        resultSet.getString("Username"),
                        resultSet.getString("Password"),
                        resultSet.getString("Email"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("Gender"),
                        resultSet.getString("PersonID"));
            }
            finally {
                if(preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        } catch (SQLException exception) {
            throw new DataAccessException(String.format("Get User failed. : %s", exception.getLocalizedMessage()));
        }
    }

    public void modifyUser(User user) throws DataAccessException {
        PreparedStatement preparedStatement = null;
        try {
            try {
                String sql = "UPDATE user SET password=?, email=?, firstName=?, lastName=?, gender=?, personID=? WHERE Username=?;";
                preparedStatement = conn.prepareStatement(sql);

                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setString(4, user.getFirstName());
                preparedStatement.setString(5, user.getLastName());
                preparedStatement.setString(6, user.getGender());
                preparedStatement.setString(7, user.getPersonID());

                preparedStatement.executeUpdate();
            }
            finally {
                if(preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        } catch (SQLException exception) {
            throw new DataAccessException(String.format("Modify User failed. : %s", exception.getLocalizedMessage()));
        }
    }

    public void clearTables() throws DataAccessException {
        try (Statement statement = conn.createStatement()){
            String sql = "DELETE FROM user";
            statement.executeUpdate(sql);
        } catch (SQLException exception) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

}