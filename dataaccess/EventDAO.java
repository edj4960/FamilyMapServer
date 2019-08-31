package cs240.evanjones.server.dataaccess;

import java.sql.*;
import java.util.ArrayList;

import cs240.evanjones.server.model.Event;

/** Adds, updates, or deletes events from the database */
public class EventDAO
{
  private Connection conn;
  /**New EventDAO object to connect to database*/
  public EventDAO(Connection conn) { this.conn = conn; }

  /**
  * Adds an event to the database
  * @param event name of event to be added
  * @return true or false based on success
  */
  public boolean addEvent(Event event) throws Exception {
    boolean commit = true;
    String sql = "INSERT INTO Events (EventID, Descendant, PersonID, Latitude, " +
            "Longitude, Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, event.getEventId());
      stmt.setString(2, event.getDescendant());
      stmt.setString(3, event.getPersonId());
      if(event.getLatitude() != null)
        stmt.setDouble(4, event.getLatitude());
      if(event.getLongitude() != null)
        stmt.setDouble(5, event.getLongitude());
      if(event.getCountry() != null)
        stmt.setString(6, event.getCountry());
      if(event.getCity() != null)
        stmt.setString(7, event.getCity());
      if(event.getEventType() != null)
        stmt.setString(8, event.getEventType());
      if(event.getYear() != null)
        stmt.setInt(9, event.getYear());

      stmt.executeUpdate();
    } catch (SQLException e) {
      commit = false;
      throw new DataAccessException("Error encountered while inserting into the database");
    }
    return commit;
  }

  /**
  * Looks up an event in database through ID
  * @param eventID the ID of the event to find
  * @return event object
  */
  public Event getEvent(String eventID) throws Exception {
    Event event;
    ResultSet rs;
    String sql = "SELECT * FROM Events WHERE EventID = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, eventID);
      rs = stmt.executeQuery();
      if (rs.next() == true) {
        event = new Event(rs.getString("EventID"), rs.getString("Descendant"), rs.getString("PersonID"));
        event.setLatitude(rs.getDouble("Latitude"));
        event.setLongitude(rs.getDouble("Longitude"));
        event.setCountry(rs.getString("Country"));
        event.setCity(rs.getString("City"));
        event.setEventType(rs.getString("EventType"));
        event.setYear(rs.getInt("Year"));
        return event;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding event");
    }
    return null;
  }

  /**
  * Finds person events from the database through an authToken
  * @param username ID of person to find event for
  * @return the group of events found for the person
  */
  public ArrayList<Event> getUserEvents(String username) throws Exception {
    ArrayList<Event> events = new ArrayList<>();
    ResultSet rs = null;
    String sql = "SELECT * FROM Events WHERE Descendant = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, username);
      rs = stmt.executeQuery();
      while (rs.next() == true) {
        Event event = new Event(rs.getString("EventID"),
                                rs.getString("Descendant"),
                                rs.getString("PersonID"));
        event.setLatitude(rs.getDouble("Latitude"));
        event.setLongitude(rs.getDouble("Longitude"));
        event.setCountry(rs.getString("Country"));
        event.setCity(rs.getString("City"));
        event.setEventType(rs.getString("EventType"));
        event.setYear(rs.getInt("Year"));
        events.add(event);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding event");
    }
    return events;
  }

  /**
  * removes all events in database for specific user
  * @param descendant (Username) of user whose events are to be deleted
  * @return true or false for successful
  */
  public void removeUserEvents(String descendant) throws Exception {
    String sql = "DELETE FROM Events WHERE Descendant = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, descendant);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while deleting events");
    }
  }

  /** Reomves all events from database */
  public void removeAllEvents() throws Exception {
      String sql = "Delete From Events;";
      try(PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.executeUpdate();
      } catch (SQLException e) {
          e.printStackTrace();
          throw new DataAccessException("Error encountered while deleting all events");
      }
  }
}
