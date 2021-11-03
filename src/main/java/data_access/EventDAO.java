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
        PreparedStatement statement = null;
        try {
            try {
                String sql = "INSERT INTO event (EventID, AssociatedUsername, PersonID, Latitude, Longitude, " +
                        "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
                statement = conn.prepareStatement(sql);

                //Fill the statement with the Event parameters.
                statement.setString(1, event.getEventID());
                statement.setString(2, event.getUsername());
                statement.setString(3, event.getPersonID());
                statement.setFloat(4, event.getLatitude());
                statement.setFloat(5, event.getLongitude());
                statement.setString(6, event.getCountry());
                statement.setString(7, event.getCity());
                statement.setString(8, event.getEventType());
                statement.setInt(9, event.getYear());


                statement.executeUpdate();
            }
            finally {
                if(statement != null) {
                    statement.close();
                }
            }
        } catch (SQLException err) {
            throw new DataAccessException(String.format("Add Event failed. : %s", err.getLocalizedMessage()));
        }
    }

    public void deleteEvent(Event event) throws DataAccessException {
        PreparedStatement statement = null;
        try {
            try {
                String sql = "DELETE FROM event WHERE EventID = ?;";
                statement = conn.prepareStatement(sql);

                statement.setString(1, event.getEventID());

                statement.executeUpdate();
            }
            finally {
                if(statement != null) {
                    statement.close();
                }
            }
        } catch (SQLException err) {
            throw new DataAccessException(String.format("Delete Event failed. : %s", err.getLocalizedMessage()));
        }
    }

    public Event getEvent(String eventID) throws DataAccessException {
        PreparedStatement statement = null;
        try {
            try {
                String sql = "SELECT * FROM event WHERE EventID = ?;";
                statement = conn.prepareStatement(sql);

                statement.setString(1, eventID);

                ResultSet rs = statement.executeQuery();
                return new Event(
                        rs.getString("EventID"),
                        rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"),
                        rs.getFloat("Latitude"),
                        rs.getFloat("Longitude"),
                        rs.getString("Country"),
                        rs.getString("City"),
                        rs.getString("EventType"),
                        rs.getInt("Year"));
            }
            finally {
                if(statement != null) {
                    statement.close();
                }
            }
        } catch (SQLException err) {
            throw new DataAccessException(String.format("Get Event failed. : %s", err.getLocalizedMessage()));
        }
    }

    public Event[] getAllEvents(String AssociatedUsername) throws DataAccessException {
        PreparedStatement statement = null;
        try {
            try {
                String sql = "SELECT * FROM event WHERE AssociatedUsername=?;";
                statement = conn.prepareStatement(sql);

                statement.setString(1, AssociatedUsername);

                ResultSet rs = statement.executeQuery();

                ArrayList<Event> resultSet = new ArrayList<>();
                while(rs.next()) {
                    String EventID = rs.getString("EventID");
                    String PersonID = rs.getString("PersonID");
                    String Username = rs.getString("AssociatedUsername");
                    float   Latitude = rs.getFloat("Latitude");
                    float   Longitude = rs.getFloat("Longitude");
                    String Country = rs.getString("Country");
                    String City = rs.getString("City");
                    String EventType = rs.getString("EventType");
                    int    Year = rs.getInt("Year");
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
        } catch (SQLException err) {
            throw new DataAccessException(String.format("Get all event failed. : %s", err.getLocalizedMessage()));
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