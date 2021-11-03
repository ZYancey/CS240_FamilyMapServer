package data_access;

import java.sql.*;

import model.AuthToken;
public class AuthTokenDAO {
    public AuthTokenDAO(Connection c) {
        setConnection(c);
    }
    private Connection c;
    public void setConnection(Connection c) { this.c = c; }
    public Connection getConnection() { return c; }

    public void addAuthToken(AuthToken a) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "INSERT INTO authtoken (authtokenID, username, personID) VALUES (?, ?, ?);";
                stmt = c.prepareStatement(sql);

                stmt.setString(1, a.getAuthTokenID());
                stmt.setString(2, a.getUserName());
                stmt.setString(3, a.getPersonID());
                stmt.executeUpdate();
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Add AuthToken failed. : %s", e.getLocalizedMessage()));
        }
    }

    public AuthToken getAuthToken(String authID) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "SELECT * FROM authtoken WHERE authtokenID = ?;";
                stmt = c.prepareStatement(sql);
                stmt.setString(1, authID);

                ResultSet rs = stmt.executeQuery();
                return new AuthToken(rs.getString("authtokenID"),
                        rs.getString("username"),
                        rs.getString("personID"));
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Get AuthToken failed. : %s", e.getLocalizedMessage()));
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