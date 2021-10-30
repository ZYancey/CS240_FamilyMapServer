package data_access;

import model.Person;

import java.sql.*;
import java.util.ArrayList;

public class PersonDAOEXAM {
    public PersonDAOEXAM(Connection c) {
        setConnection(c);
    }
    private Connection c;
    public void setConnection(Connection c) { this.c = c; }
    public Connection getConnection() { return c; }

    public void addPerson(Person person) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "INSERT INTO person (PersonID, Username, FirstName, LastName, Gender, FatherID, MotherID, SpouseID) " +
                        "VALUES(?,?,?,?,?,?,?,?)";
                stmt = c.prepareStatement(sql);
                //Using the statements built-in set(type) functions we can pick the question mark we want
                //to fill in and give it a proper value. The first argument corresponds to the first
                //question mark found in our sql String
                stmt.setString(1, person.getPersonID());
                stmt.setString(2, person.getUsername());
                stmt.setString(3, person.getFirstName());
                stmt.setString(4, person.getLastName());
                stmt.setString(5, person.getGender());
                stmt.setString(6, person.getFatherID());
                stmt.setString(7, person.getMotherID());
                stmt.setString(8, person.getSpouseID());
                stmt.executeUpdate();
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Add Person failed. : %s", e.getLocalizedMessage()));
        }
    }

    public void modifyPerson(Person person) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "UPDATE person SET Username=?, FirstName=?, LastName=?, Gender=?, FatherID=?, MotherID=?, SpouseID=? WHERE PersonID=?;";
                stmt = c.prepareStatement(sql);

                //Fill the statement with the Person parameters.
                stmt.setString(1, person.getUsername());
                stmt.setString(2, person.getFirstName());
                stmt.setString(3, person.getLastName());
                stmt.setString(4, person.getGender());
                stmt.setString(5, person.getFatherID());
                stmt.setString(6, person.getMotherID());
                stmt.setString(7, person.getSpouseID());
                stmt.setString(8, person.getPersonID());

                //Execute the finalized statement.
                stmt.executeUpdate();
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Modify Person failed. : %s", e.getLocalizedMessage()));
        }
    }

    public void deletePerson(Person person) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "DELETE FROM person WHERE PersonID = ?;";
                stmt = c.prepareStatement(sql);

                //Fill the statement with the Person's PersonID.
                stmt.setString(1, person.getPersonID());

                stmt.executeUpdate();
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Delete Person failed. : %s", e.getLocalizedMessage()));
        }
    }

    public void deleteAllPersons() throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "DELETE FROM person;";
                stmt = c.prepareStatement(sql);

                //No extra parameters to add to the statement, so proceed to execution.
                stmt.executeUpdate();
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Delete all person failed. : %s", e.getLocalizedMessage()));
        }
    }

    public Person getPerson(String PersonID) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "SELECT * FROM person WHERE PersonID = ?;";
                stmt = c.prepareStatement(sql);

                //Fill the statement with the given PersonID.
                stmt.setString(1, PersonID);

                //Execute the query, and construct a new Person object from the data in the ResultSet.
                ResultSet rs = stmt.executeQuery();
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
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Get Person failed. : %s", e.getLocalizedMessage()));
        }
    }

    public Person[] getAllPersons(String Username) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "SELECT * FROM person WHERE Username=?;";
                stmt = c.prepareStatement(sql);

                //Fill the statement with the Username's userName.
                stmt.setString(1, Username);

                //Execute the finalized query.
                ResultSet rs = stmt.executeQuery();

                //Iterate over the ResultSet and use the data to construct Person objects and add them to the Set.
                ArrayList<Person> res = new ArrayList<Person>();
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
                    //res.add(new Person(person, username, firstName, lastName, gender, FatherID, MotherID, SpouseID));
                }
                Person[] all = new Person[res.size()];
                res.toArray(all);
                return all;
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Get All person failed. : %s", e.getLocalizedMessage()));
        }
    }

    public void clearTables() throws DataAccessException {
        try (Statement stmt = c.createStatement()){
            String sql = "DELETE FROM person";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}