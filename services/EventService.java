package cs240.evanjones.server.services;

import java.sql.Connection;
import java.util.ArrayList;

import cs240.evanjones.server.dataaccess.AuthTokenDAO;
import cs240.evanjones.server.dataaccess.DataAccessException;
import cs240.evanjones.server.dataaccess.Database;
import cs240.evanjones.server.dataaccess.EventDAO;
import cs240.evanjones.server.model.AuthToken;
import cs240.evanjones.server.model.Event;
import cs240.evanjones.server.requests.EventRequest;
import cs240.evanjones.server.results.EventResult;

/** Service to return one or all events for a personID */
public class EventService {
  private EventRequest request;

  /**
   * Creates Event Service object
   * @param request for event
   * */
  public EventService(EventRequest request) {
    this.request = request;
  }

  /**
   * Checks authToken of user
   * @param authToken of user
   * @return username of whom authtoken belongs to
   */
  public String checkAuthToken(String authToken) throws Exception {
    Database db = new Database();
    AuthToken token = null;
    try {
      Connection conn = db.openConnection();
      AuthTokenDAO aDao = new AuthTokenDAO(conn);
      token = aDao.getAuthToken(authToken);
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }

    if(token != null)
      return token.getUserName();
    return null;
  }

  /**
  * Finds and returns a single event
  * @return result
  */
  public EventResult getEvent() throws Exception {
    EventResult result = new EventResult();
    // Check AuthToken
    String userName = checkAuthToken(request.getAuthToken());
    if(userName == null) {
      result.setMessage("Error: Invalid AuthToken");
      return result;
    }
    // Look for Event
    Database db = new Database();
    Event event = null;
    try {
      Connection conn = db.openConnection();
      EventDAO eDao = new EventDAO(conn);
      event = eDao.getEvent(request.getEventID());
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }
    // Create Result
    if(event == null)
      result.setMessage("Error: Event Does Not Exist");
    else if(!userName.equals(event.getDescendant()))
      result.setMessage("Error: Event Does not belong to user");
    else {
      ArrayList<Event> insertPerson = new ArrayList<>();
      insertPerson.add(event);
      result.setEventData(insertPerson);
    }
    return result;
  }

  /**
   * Finds and returns ALL events for a user
   * @return eventType
   */
  public EventResult getUserEvents() throws Exception {
    EventResult result = new EventResult();
    // Check AuthToken
    String userName = checkAuthToken(request.getAuthToken());
    if(userName == null) {
      result.setMessage("Error: Invalid AuthToken");
      return result;
    }
    // finds all events for user
    Database db = new Database();
    ArrayList<Event> events = null;
    try {
      Connection conn = db.openConnection();
      EventDAO eDao = new EventDAO(conn);
      events = eDao.getUserEvents(userName);
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }
    // Create Result
    if(events == null)
      result.setMessage("Error: No Events for user exist");
    else
      result.setEventData(events);
    return result;
  }
}
