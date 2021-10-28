package data_access;

import model.User;

import java.sql.*;

@SuppressWarnings("SqlResolve")

public class UserDAOEX {
    /*
    private final Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }*/
    public UserDAOEX(Connection conn) {
        setConnection(conn);
    }
    /**A reference to the database Connection object*/
    private Connection conn;



    public void setConnection(Connection conn) { this.conn = conn; }

    /**@return			the database Connection object*/
    public Connection getConnection() { return conn; }

    public void insert(User user) throws DataAccessException {
        System.out.println("ATTEMPTING TO INSERT NEW USER");
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement

        String sql = "INSERT INTO user (Username, Password, Email, FirstName, LastName, Gender, PersonID) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    public User findUser(String Username) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "SELECT * FROM user WHERE Username = ?;";
                stmt = conn.prepareStatement(sql);

                stmt.setString(1, Username);

                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    return null;
                    //throw new DataAccessException("Get User Failed");
                }
                User user = new User(
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("Email"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Gender"),
                        rs.getString("PersonID"));
                return user;
            } finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
        }
    }


    public User find(String Username) throws DataAccessException {
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM user WHERE Username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, Username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("Email"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Gender"),
                        rs.getString("PersonID"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    public void clearTables() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM user";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}