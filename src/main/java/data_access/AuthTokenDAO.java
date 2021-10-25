package data_access;

import model.AuthToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthTokenDAO {
    private final Connection conn;

    public AuthTokenDAO(Connection conn)
    {
        this.conn = conn;
    }


    /**
     * Inserts Token in DB
     * @param authToken
     * @throws DataAccessException
     */
    public void insert(AuthToken authToken) throws DataAccessException {

    }

    /**
     * Finds Token in DB
     * @param authToken
     * @return token
     * @throws DataAccessException
     */
    public AuthToken find(String authToken) throws DataAccessException {
        AuthToken auth = null;

        return auth;
    }}