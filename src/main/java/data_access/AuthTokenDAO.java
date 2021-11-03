package data_access;

import java.sql.*;

import model.AuthToken;
public class AuthTokenDAO {
    public AuthTokenDAO(Connection conn) {
        setConnection(conn);
    }
    private Connection conn;
    public void setConnection(Connection conn) { this.conn = conn; }
    public Connection getConnection() { return conn; }

    public void addAuthToken(AuthToken authToken) throws DataAccessException {
        PreparedStatement preparedStatement = null;
        try {
            try {
                String sql = "INSERT INTO authtoken (authtokenID, username, personID) VALUES (?, ?, ?);";
                preparedStatement = conn.prepareStatement(sql);

                preparedStatement.setString(1, authToken.getAuthTokenID());
                preparedStatement.setString(2, authToken.getUserName());
                preparedStatement.setString(3, authToken.getPersonID());
                preparedStatement.executeUpdate();
            }
            finally {
                if(preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        } catch (SQLException exception) {
            throw new DataAccessException(String.format("Add AuthToken failed. : %s", exception.getLocalizedMessage()));
        }
    }

    public AuthToken getAuthToken(String authID) throws DataAccessException {
        PreparedStatement preparedStatement = null;
        try {
            try {
                String sql = "SELECT * FROM authtoken WHERE authtokenID = ?;";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, authID);

                ResultSet resultSet = preparedStatement.executeQuery();
                return new AuthToken(resultSet.getString("authtokenID"),
                        resultSet.getString("username"),
                        resultSet.getString("personID"));
            }
            finally {
                if(preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        } catch (SQLException exception) {
            throw new DataAccessException(String.format("Get AuthToken failed. : %s", exception.getLocalizedMessage()));
        }
    }

    public void clearTables() throws DataAccessException {
        try (Statement statement = conn.createStatement()){
            String sql = "DELETE FROM authtoken";
            statement.executeUpdate(sql);
        } catch (SQLException exception) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}