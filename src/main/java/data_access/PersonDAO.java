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
        PreparedStatement preparedStatement = null;
        try {
            try {
                String sql = "INSERT INTO person (PersonID, Username, FirstName, LastName, Gender, FatherID, MotherID, SpouseID) " +
                        "VALUES(?,?,?,?,?,?,?,?)";
                preparedStatement = conn.prepareStatement(sql);

                //Fill the statement with the Person parameters.
                preparedStatement.setString(1, person.getPersonID());
                preparedStatement.setString(2, person.getUsername());
                preparedStatement.setString(3, person.getFirstName());
                preparedStatement.setString(4, person.getLastName());
                preparedStatement.setString(5, person.getGender());
                preparedStatement.setString(6, person.getFatherID());
                preparedStatement.setString(7, person.getMotherID());
                preparedStatement.setString(8, person.getSpouseID());

                preparedStatement.executeUpdate();
            }
            finally {
                if(preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        } catch (SQLException exception) {
            throw new DataAccessException(String.format("Add Person failed. : %s", exception.getLocalizedMessage()));
        }
    }

    public void deletePerson(Person person) throws DataAccessException {
        PreparedStatement preparedStatement = null;
        try {
            try {
                String sql = "DELETE FROM person WHERE PersonID = ?;";
                preparedStatement = conn.prepareStatement(sql);

                //Fill the statement with the Person's personID.
                preparedStatement.setString(1, person.getPersonID());

                preparedStatement.executeUpdate();
            }
            finally {
                if(preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        } catch (SQLException exception) {
            throw new DataAccessException(String.format("Delete Person failed. : %s", exception.getLocalizedMessage()));
        }
    }

    public Person getPerson(String personID) throws DataAccessException {
        PreparedStatement preparedStatement = null;
        try {
            try {
                String sql = "SELECT * FROM person WHERE PersonID = ?;";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, personID);

                ResultSet resultSet = preparedStatement.executeQuery();
                return new Person(
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("Gender"),
                        resultSet.getString("PersonID"),
                        resultSet.getString("SpouseID"),
                        resultSet.getString("FatherID"),
                        resultSet.getString("MotherID"),
                        resultSet.getString("Username"));
            }
            finally {
                if(preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        } catch (SQLException exception) {
            throw new DataAccessException(String.format("Get Person failed. : %s", exception.getLocalizedMessage()));
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
                ResultSet resultSet = statement.executeQuery();

                //Iterate over the ResultSet and use the data to construct Person objects and add them to the Set.
                ArrayList<Person> res = new ArrayList<>();
                while(resultSet.next()) {
                    String person = resultSet.getString("PersonID");
                    String username = resultSet.getString("Username");
                    String firstName = resultSet.getString("FirstName");
                    String lastName = resultSet.getString("LastName");
                    String gender = resultSet.getString("Gender");
                    String FatherID = resultSet.getString("FatherID");
                    String MotherID = resultSet.getString("MotherID");
                    String SpouseID = resultSet.getString("SpouseID");
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
        } catch (SQLException exception) {
            throw new DataAccessException(String.format("Get All Persons failed. : %s", exception.getLocalizedMessage()));
        }
    }

    public void clearTables() throws DataAccessException {
        try (Statement statement = conn.createStatement()){
            String sql = "DELETE FROM person";
            statement.executeUpdate(sql);
        } catch (SQLException exception) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}