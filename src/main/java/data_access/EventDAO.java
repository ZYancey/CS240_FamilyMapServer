package data_access;

import java.util.ArrayList;
import java.sql.*;

import model.Event;
public class EventDAO {
    public EventDAO(Connection conn) {
        setConnection(conn);
    }
    private Connection conn;
    public void setConnection(Connection conn) { this.conn = conn; }
    public Connection getConnection() { return conn; }

    
    public void addEvent(Event event) throws DataAccessException {
        PreparedStatement preparedStatement = null;
        try {
            try {
                String sql = "INSERT INTO event (EventID, AssociatedUsername, PersonID, Latitude, Longitude, " +
                        "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
                preparedStatement = conn.prepareStatement(sql);

                //Fill the statement with the Event parameters.
                preparedStatement.setString(1, event.getEventID());
                preparedStatement.setString(2, event.getUsername());
                preparedStatement.setString(3, event.getPersonID());
                preparedStatement.setFloat(4, event.getLatitude());
                preparedStatement.setFloat(5, event.getLongitude());
                preparedStatement.setString(6, event.getCountry());
                preparedStatement.setString(7, event.getCity());
                preparedStatement.setString(8, event.getEventType());
                preparedStatement.setInt(9, event.getYear());


                preparedStatement.executeUpdate();
            }
            finally {
                if(preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        } catch (SQLException exception) {
            throw new DataAccessException(String.format("Add Event failed. : %s", exception.getLocalizedMessage()));
        }
    }

    public void deleteEvent(Event event) throws DataAccessException {
        PreparedStatement preparedStatement = null;
        try {
            try {
                String sql = "DELETE FROM event WHERE EventID = ?;";
                preparedStatement = conn.prepareStatement(sql);

                preparedStatement.setString(1, event.getEventID());

                preparedStatement.executeUpdate();
            }
            finally {
                if(preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        } catch (SQLException err) {
            throw new DataAccessException(String.format("Delete Event failed. : %s", err.getLocalizedMessage()));
        }
    }

    public Event getEvent(String eventID) throws DataAccessException {
        PreparedStatement preparedStatement = null;
        try {
            try {
                String sql = "SELECT * FROM event WHERE EventID = ?;";
                preparedStatement = conn.prepareStatement(sql);

                preparedStatement.setString(1, eventID);

                ResultSet resultSet = preparedStatement.executeQuery();
                return new Event(
                        resultSet.getString("EventID"),
                        resultSet.getString("AssociatedUsername"),
                        resultSet.getString("PersonID"),
                        resultSet.getFloat("Latitude"),
                        resultSet.getFloat("Longitude"),
                        resultSet.getString("Country"),
                        resultSet.getString("City"),
                        resultSet.getString("EventType"),
                        resultSet.getInt("Year"));
            }
            finally {
                if(preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        } catch (SQLException exception) {
            throw new DataAccessException(String.format("Get Event failed. : %s", exception.getLocalizedMessage()));
        }
    }

    public Event[] getAllEvents(String AssociatedUsername) throws DataAccessException {
        PreparedStatement statement = null;
        try {
            try {
                String sql = "SELECT * FROM event WHERE AssociatedUsername=?;";
                statement = conn.prepareStatement(sql);

                statement.setString(1, AssociatedUsername);

                ResultSet tempResultSet = statement.executeQuery();

                ArrayList<Event> resultSet = new ArrayList<>();
                while(tempResultSet.next()) {
                    String EventID = tempResultSet.getString("EventID");
                    String PersonID = tempResultSet.getString("PersonID");
                    String Username = tempResultSet.getString("AssociatedUsername");
                    float   Latitude = tempResultSet.getFloat("Latitude");
                    float   Longitude = tempResultSet.getFloat("Longitude");
                    String Country = tempResultSet.getString("Country");
                    String City = tempResultSet.getString("City");
                    String EventType = tempResultSet.getString("EventType");
                    int    Year = tempResultSet.getInt("Year");
                    resultSet.add(new Event(EventID,Username,PersonID,Latitude,Longitude,Country,City,EventType,Year));
                }

                Event[] events = new Event[resultSet.size()];
                resultSet.toArray(events);
                return events;
            }
            finally {
                if(statement != null) {
                    statement.close();
                }
            }
        } catch (SQLException exception) {
            throw new DataAccessException(String.format("Get all event failed. : %s", exception.getLocalizedMessage()));
        }
    }

    public void clearTables() throws DataAccessException {
        try (Statement statement = conn.createStatement()){
            String sql = "DELETE FROM event";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}