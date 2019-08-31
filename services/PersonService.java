package cs240.evanjones.server.services;

import java.sql.Connection;
import java.util.ArrayList;

import cs240.evanjones.server.dataaccess.AuthTokenDAO;
import cs240.evanjones.server.dataaccess.DataAccessException;
import cs240.evanjones.server.dataaccess.Database;
import cs240.evanjones.server.dataaccess.PersonDAO;
import cs240.evanjones.server.model.AuthToken;
import cs240.evanjones.server.model.Person;
import cs240.evanjones.server.requests.PersonRequest;
import cs240.evanjones.server.results.PersonResult;

/** Service to find one or all persons for a user */
public class PersonService {
  PersonRequest request;

  /**
   * Creates a new person service object
   * @param request of person service
   * */
  public PersonService(PersonRequest request) { this.request = request; }

  /**
   * Checks authToken of user
   * @param authToken of user
   * @return username of holder of token
   */
  public String checkAuth(String authToken) throws Exception {
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
  * Finds a single person
  * @return Person Object
  */
  public PersonResult getPerson() throws Exception {
    PersonResult result = new PersonResult();
    String userName = checkAuth(request.getAuthToken());
    if(userName == null) {
      result.setMessage("Error: Invalid AuthToken");
      return result;
    }
    // Attempt to find person
    Database db = new Database();
    Person person = null;
    try {
      Connection conn = db.openConnection();
      PersonDAO pDao = new PersonDAO(conn);
      person = pDao.getPerson(request.getPersonID());
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }
    // Create response
    if(person == null)
      result.setMessage("Error: Person Does Not Exist");
    else if(!userName.equals(person.getDescendant()))
      result.setMessage("Error: Person Does not belong to user");
    else {
      ArrayList<Person> insertPerson = new ArrayList<>();
      insertPerson.add(person);
      result.setPersonData(insertPerson);
    }
    return result;
  }

  /**
   * Find all persons for a user
   */
  public PersonResult getUserPersons() throws Exception {
    PersonResult result = new PersonResult();
    // Check AuthToken
    String userName = checkAuth(request.getAuthToken());
    if(userName == null) {
      result.setMessage("Error: Invalid AuthToken");
      return result;
    }
    // Find all persons for user
    Database db = new Database();
    ArrayList<Person> persons = null;
    try {
      Connection conn = db.openConnection();
      PersonDAO pDao = new PersonDAO(conn);
      persons = pDao.getUserPersons(userName);
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }
    // Create response
    if(persons == null)
      result.setMessage("Error: No Persons for user exist");
    else
      result.setPersonData(persons);
    return result;
  }
}