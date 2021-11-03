package data_access;

import java.util.ArrayList;
import java.sql.*;

import model.Person;
public class PersonDAO {
    public PersonDAO(Connection conn) {
        setConnection(conn);
    }
    private Connection conn;
    public void setConnection(Connection conn) { this.conn = conn; }
    public Connection getConnection() { return conn; }

    public void addPerson(Person person) throws DataAccessException {
        PreparedStatement statement = null;
        try {
            try {
                String sql = "INSERT INTO person (PersonID, Username, FirstName, LastName, Gender, FatherID, MotherID, SpouseID) " +
                        "VALUES(?,?,?,?,?,?,?,?)";
                statement = conn.prepareStatement(sql);

                //Fill the statement with the Person parameters.
                statement.setString(1, person.getPersonID());
                statement.setString(2, person.getUsername());
                statement.setString(3, person.getFirstName());
                statement.setString(4, person.getLastName());
                statement.setString(5, person.getGender());
                statement.setString(6, person.getFatherID());
                statement.setString(7, person.getMotherID());
                statement.setString(8, person.getSpouseID());

                statement.executeUpdate();
            }
            finally {
                if(statement != null) {
                    statement.close();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Add Person failed. : %s", e.getLocalizedMessage()));
        }
    }

    public void deletePerson(Person p) throws DataAccessException {
        PreparedStatement statement = null;
        try {
            try {
                String sql = "DELETE FROM person WHERE PersonID = ?;";
                statement = conn.prepareStatement(sql);

                //Fill the statement with the Person's personID.
                statement.setString(1, p.getPersonID());

                statement.executeUpdate();
            }
            finally {
                if(statement != null) {
                    statement.close();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Delete Person failed. : %s", e.getLocalizedMessage()));
        }
    }

    public Person getPerson(String personID) throws DataAccessException {
        PreparedStatement statement = null;
        try {
            try {
                String sql = "SELECT * FROM person WHERE PersonID = ?;";
                statement = conn.prepareStatement(sql);
                statement.setString(1, personID);

                ResultSet rs = statement.executeQuery();
                return new Person(
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Gender"),
                        rs.getString("PersonID"),
                        rs.getString("SpouseID"),
                        rs.getString("FatherID"),
                        rs.getString("MotherID"),
                        rs.getString("Username"));
            }
            finally {
                if(statement != null) {
                    statement.close();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Get Person failed. : %s", e.getLocalizedMessage()));
        }
    }

    public Person[] getAllPersons(String Username) throws DataAccessException {
        PreparedStatement statement = null;
        try {
            try {
                String sql = "SELECT * FROM person WHERE Username=?;";
                statement = conn.prepareStatement(sql);

                //Fill the statement with the descendant's userName.
                statement.setString(1, Username);

                //Execute the finalized query.
                ResultSet rs = statement.executeQuery();

                //Iterate over the ResultSet and use the data to construct Person objects and add them to the Set.
                ArrayList<Person> res = new ArrayList<>();
                while(rs.next()) {
                    String person = rs.getString("PersonID");
                    String username = rs.getString("Username");
                    String firstName = rs.getString("FirstName");
                    String lastName = rs.getString("LastName");
                    String gender = rs.getString("Gender");
                    String FatherID = rs.getString("FatherID");
                    String MotherID = rs.getString("MotherID");
                    String SpouseID = rs.getString("SpouseID");
                    res.add(new Person(firstName, lastName, gender, person, SpouseID, FatherID, MotherID, username));
                }
                Person[] all = new Person[res.size()];
                res.toArray(all);
                return all;
            }
            finally {
                if(statement != null) {
                    statement.close();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Get All Persons failed. : %s", e.getLocalizedMessage()));
        }
    }

    public void clearTables() throws DataAccessException {
        try (Statement statement = conn.createStatement()){
            String sql = "DELETE FROM person";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}