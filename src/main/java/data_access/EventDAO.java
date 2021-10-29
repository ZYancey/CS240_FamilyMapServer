package data_access;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;

import model.Event;

public class EventDAO {
    
    public EventDAO(Connection c) {
        setConnection(c);
    }

    private Connection c;

    
    public void setConnection(Connection c) { this.c = c; }

    public Connection getConnection() { return c; }

    
    public void addEvent(Event event) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "INSERT INTO event (EventID, AssociatedUsername, PersonID, Latitude, Longitude, " +
                        "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
                stmt = c.prepareStatement(sql);

                //Fill the statement with the Event parameters.
                stmt.setString(1, event.getEventID());
                stmt.setString(2, event.getUsername());
                stmt.setString(3, event.getPersonID());
                stmt.setFloat(4, event.getLatitude());
                stmt.setFloat(5, event.getLongitude());
                stmt.setString(6, event.getCountry());
                stmt.setString(7, event.getCity());
                stmt.setString(8, event.getEventType());
                stmt.setInt(9, event.getYear());


                stmt.executeUpdate();
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException err) {
            throw new DataAccessException(String.format("Add Event failed. : %s", err.getLocalizedMessage()));
        }
    }

    public void modifyEvent(Event event) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "UPDATE event SET personID=?, AssociatedUsername=?, latitude=?, longitude=?, country=?, city=?, eventType=?, year=? WHERE eventID=?;";
                stmt = c.prepareStatement(sql);

                //Fill the statement with the Event parameters.
                stmt.setString(1, event.getPersonID());
                stmt.setString(2, event.getUsername());
                stmt.setFloat(3, event.getLatitude());
                stmt.setFloat(4, event.getLongitude());
                stmt.setString(5, event.getCountry());
                stmt.setString(6, event.getCity());
                stmt.setString(7, event.getEventType());
                stmt.setInt(8, event.getYear());
                stmt.setString(9, event.getEventID());

                stmt.executeUpdate();
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException err) {
            throw new DataAccessException(String.format("Modify Event failed. : %s", err.getLocalizedMessage()));
        }
    }

    public void deleteEvent(Event event) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "DELETE FROM event WHERE eventID = ?;";
                stmt = c.prepareStatement(sql);

                stmt.setString(1, event.getEventID());

                stmt.executeUpdate();
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException err) {
            throw new DataAccessException(String.format("Delete Event failed. : %s", err.getLocalizedMessage()));
        }
    }

    public void deleteAllEvents() throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "DELETE FROM event;";
                stmt = c.prepareStatement(sql);

                stmt.executeUpdate();
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException err) {
            System.out.println("Delete All event failed.");
            throw new DataAccessException(String.format("Delete all event failed. : %s", err.getLocalizedMessage()));
        }
    }

    public Event getEvent(String eventID) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "SELECT * FROM event WHERE eventID = ?;";
                stmt = c.prepareStatement(sql);

                stmt.setString(1, eventID);

                ResultSet rs = stmt.executeQuery();
                return new Event(
                        rs.getString("eventID"),
                        rs.getString("personID"),
                        rs.getString("AssociatedUsername"),
                        rs.getFloat("latitude"),
                        rs.getFloat("longitude"),
                        rs.getString("country"),
                        rs.getString("city"),
                        rs.getString("eventType"),
                        rs.getInt("year"));
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException err) {
            throw new DataAccessException(String.format("Get Event failed. : %s", err.getLocalizedMessage()));
        }
    }

    public Event[] getAllEvents(String AssociatedUsername) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            try {
                String sql = "SELECT * FROM event WHERE AssociatedUsername=?;";
                stmt = c.prepareStatement(sql);

                //Fill the statement with the AssociatedUsername's userNamerr.
                stmt.setString(1, AssociatedUsername);

                ResultSet rs = stmt.executeQuery();

                //Iterate over the ResultSet to construct Event objects and add them to the Set to be returned.
                ArrayList<Event> res = new ArrayList<Event>();
                while(rs.next()) {
                    String eID = rs.getString("eventID");
                    String pID = rs.getString("personID");
                    String desc = rs.getString("AssociatedUsername");
                    float lati = rs.getFloat("latitude");
                    float longi = rs.getFloat("longitude");
                    String country = rs.getString("country");
                    String city = rs.getString("city");
                    String eT = rs.getString("eventType");
                    int year = rs.getInt("year");
                    res.add(new Event(eID, pID, desc, lati, longi, country, city, eT, year));
                }
                //For some reason it won't let me just do the toArray() function and cast as an Event[].....
                Event[] all = new Event[res.size()];
                res.toArray(all);
                return all;
            }
            finally {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException err) {
            throw new DataAccessException(String.format("Get All event failed. : %s", err.getLocalizedMessage()));
        }
    }


    public void clearTables() throws DataAccessException {
        try (Statement stmt = c.createStatement()){
            String sql = "DELETE FROM event";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}