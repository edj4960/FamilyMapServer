package cs240.evanjones.server.services;

import java.sql.Connection;
import java.util.UUID;
import cs240.evanjones.server.dataaccess.AuthTokenDAO;
import cs240.evanjones.server.dataaccess.DataAccessException;
import cs240.evanjones.server.dataaccess.Database;
import cs240.evanjones.server.dataaccess.UserDAO;
import cs240.evanjones.server.model.AuthToken;
import cs240.evanjones.server.model.User;
import cs240.evanjones.server.requests.LoginRequest;
import cs240.evanjones.server.results.LoginResult;

/** Service to login user */
public class LoginService {

  /** Constructor of a login service object */
  public LoginService(){}

  /**
   * Attempts to log in the user
   * @return loginResult
   */
  public LoginResult login(LoginRequest request) throws Exception {
    Database db = new Database();
    db.createTables();
    User user = null;
    LoginResult result = new LoginResult();
    String token = "";
    // Attempt to find user
    try {
      Connection conn = db.openConnection();
      UserDAO uDao = new UserDAO(conn);
      user = uDao.getUser(request.getUserName());

      // Check that user exists
      if (user == null || !user.getPassword().equals(request.getPassword())) {
        result.setMessage("Failed to login. User does not exist");
        db.closeConnection(true);
        return result;
      }
      else {
        // Create Auth Token and add to Authorization database
        token = UUID.randomUUID().toString();
        AuthToken authtoken = new AuthToken(token, user.getUserName(), user.getPersonID());
        AuthTokenDAO aDao = new AuthTokenDAO(conn);
        aDao.addAuthToken(authtoken);
      }

      db.closeConnection(true);
      }catch (DataAccessException e) {
      db.closeConnection(false);
    }
      // Create success response
      result.successResponse(token, user.getUserName(), user.getPersonID());
      return result;
  }
}
