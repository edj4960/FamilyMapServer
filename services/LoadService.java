package cs240.evanjones.server.services;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;

import cs240.evanjones.server.dataaccess.DataAccessException;
import cs240.evanjones.server.dataaccess.Database;
import cs240.evanjones.server.dataaccess.EventDAO;
import cs240.evanjones.server.dataaccess.PersonDAO;
import cs240.evanjones.server.dataaccess.UserDAO;
import cs240.evanjones.server.model.Event;
import cs240.evanjones.server.model.Person;
import cs240.evanjones.server.model.User;
import cs240.evanjones.server.requests.LoadRequest;
import cs240.evanjones.server.results.LoadResult;

/** Service to load data into the database */
public class LoadService {

  /** Constructor for Load Service Object */
  public LoadService() {}

  /**
  * Completes a load on the requested data
  * @param request an object of type loadRequest containing a request for a
  * single event
  * @return LoadRequest object containing the desired data
  */
  public LoadResult loadData(LoadRequest request) throws Exception {
    Database db = new Database();
    // Creating Arrays
    ArrayList<User> users = request.getUsers();
    ArrayList<Event> events = request.getEvents();
    ArrayList<Person> persons = request.getPersons();
    // Creating Iterators
    Iterator <User> iterUsers = users.iterator();
    Iterator <Event> iterEvents = events.iterator();
    Iterator <Person> iterPerson = persons.iterator();
    // Counters
    int userCount = 0;
    int eventCount = 0;
    int personCount = 0;
    LoadResult result = new LoadResult();
    try {
      Connection conn = db.openConnection();
      // Adding Users
      UserDAO uDao = new UserDAO(conn);
      while(iterUsers.hasNext()) {
        uDao.addUser(iterUsers.next());
        userCount++;
      }
      // Adding Events
      EventDAO eDao = new EventDAO(conn);
      while(iterEvents.hasNext()) {
        eDao.addEvent(iterEvents.next());
        eventCount++;
      }
      // Adding Persons
      PersonDAO pDao = new PersonDAO(conn);
      while(iterPerson.hasNext()) {
        pDao.addPerson(iterPerson.next());
        personCount++;
      }
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
      result.setMessage("Error, invalid input. Could not add objects to database.");
      return result;
    }
    // Creating return statement
    result.setMessage("Successfully added " + userCount + " users, "
                                            + personCount +" persons, and "
                                            + eventCount + " events to the database.");
    return result;
  }
}
