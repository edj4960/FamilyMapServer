package cs240.evanjones.server.services;

import java.sql.Connection;
import java.util.UUID;

import cs240.evanjones.server.dataaccess.AuthTokenDAO;
import cs240.evanjones.server.dataaccess.DataAccessException;
import cs240.evanjones.server.dataaccess.Database;
import cs240.evanjones.server.dataaccess.UserDAO;
import cs240.evanjones.server.model.AuthToken;
import cs240.evanjones.server.model.User;
import cs240.evanjones.server.requests.FillRequest;
import cs240.evanjones.server.requests.RegisterRequest;
import cs240.evanjones.server.results.RegisterResult;

/** Service to register user */
public class RegisterService {

  /** Creates an instance of Register Service */
  public RegisterService(){}

  /**
  * Registers a user and populates the database with 4 generations of data
  * @param request RegisterRequest object containing all needed database
  * @return registerResult object
  */
  public RegisterResult register(RegisterRequest request) throws Exception {
    Database db = new Database();
    db.createTables();
    User user = null;
    UserDAO uDao = null;
    RegisterResult result = new RegisterResult();
    // Attempt to find user
    try {
      Connection conn = db.openConnection();
      uDao = new UserDAO(conn);
      user = uDao.getUser(request.getUserName());

      // Check that user doesn't exists
      if (user != null) {
        // Create Fail Response
        result.setMessage("Failed to Register. User already exists");
        db.closeConnection(true);
        return result;
      }

      // Creating user
      String personId = UUID.randomUUID().toString();
      user = new User(request.getUserName(), request.getPassword(), request.getEmail(),
                      request.getFirstName(), request.getLastName(), request.getGender(), personId);
      // Attempting to add user
      uDao.addUser(user);
      // Creating and adding authorization
      String token = UUID.randomUUID().toString();
      AuthToken authtoken = new AuthToken(token, user.getUserName(), user.getPersonID());
      AuthTokenDAO aDao = new AuthTokenDAO(conn);
      aDao.addAuthToken(authtoken);
      // Create success response
      result.successResponse(token, user.getUserName(), user.getPersonID());
      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
      // Invalid user data. Set Failed Message
      result.setMessage("Failed to Register. User Data invalid");
      return result;
    }
    // Call FillService to generate data;
    FillService service = new FillService();
    FillRequest fillRequest = new FillRequest();
    fillRequest.setUserName(user.getUserName());
    service.populateDatabase(fillRequest);
    return result;
  }
}