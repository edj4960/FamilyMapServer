package cs240.evanjones.server.dataaccess;

import cs240.evanjones.server.model.AuthToken ;

import java.sql.*;

/** Create, finds, or removes AuthTokens from the database */
public class AuthTokenDAO
{
  Connection conn;

  /** Creates a new AuthTokenDAO object to connect to database */
  public AuthTokenDAO(Connection conn) { this.conn = conn; }

  /**
  * Adds an  AuthToken to the database
  * @param token AuthToken to be added
  * @return true or false
  */
  public boolean addAuthToken(AuthToken token) throws Exception{
    boolean commit = true;
    String sql = "INSERT INTO Authorization (AuthToken, Username, PersonID) VALUES(?,?,?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, token.getToken());
      stmt.setString(2, token.getUserName());
      stmt.setString(3, token.getPersonID());

      stmt.executeUpdate();
    } catch (SQLException e) {
      commit = false;
      throw new DataAccessException("Error encountered while inserting into the database");
    }
    return commit;
  }

  /**
  * finds an AuthToken from the database
  * @param token AuthToken string to find
  * @return AuthToken object that was found
  */
  public AuthToken getAuthToken(String token) throws Exception {
    AuthToken authtoken = null;
    ResultSet rs = null;
    String sql = "SELECT * FROM Authorization WHERE AuthToken = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, token);
      rs = stmt.executeQuery();
      if (rs.next() == true) {
        authtoken = new AuthToken(rs.getString("AuthToken"), rs.getString("Username"),
                rs.getString("PersonID"));
        return authtoken;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding authtoken");
    }
    return null;
  }

  /**
   * Removes all authTokens from database
   * @throws Exception
   */
  public void removeAllAuthTokens() throws Exception {
    String sql = "Delete FROM Authorization;";
    try(PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while deleting all authTokens");
    }
  }
}
