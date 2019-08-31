package cs240.evanjones.server.dataaccess;

import cs240.evanjones.server.model.User ;

import java.sql.*;

/** Interacts with database for user Creation, finding a user, and deleting a user */
public class UserDAO
{
  private Connection conn;

  /**
  * Creates new userDAO object to connect with the database
  * @param conn the connection to the database
  */
  public UserDAO(Connection conn) { this.conn = conn; }

  /**
  * adds a user to the database
  * @param user the User to be added to the database
  * @return either true or false if successful
  */
  public boolean addUser(User user) throws Exception {
      boolean commit = true;
      String sql = "INSERT INTO Users (Username, Password, Email, FirstName, LastName," +
              " Gender, PersonID) VALUES(?,?,?,?,?,?,?)";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.setString(1, user.getUserName());
          stmt.setString(2, user.getPassword());
          stmt.setString(3, user.getEmail());
          stmt.setString(4, user.getFirstName());
          stmt.setString(5, user.getLastName());
          stmt.setString(6, user.getGender());
          stmt.setString(7, user.getPersonID());

          stmt.executeUpdate();
      } catch (SQLException e) {
          commit = false;
          throw new DataAccessException("Error encountered while inserting into the database");
      }
      return commit;
  }

  /**
  * Finds a user based on getUserName
  * @param userName User name
  * @return User object
  */
  public User getUser(String userName) throws Exception{
      User user = null;
      ResultSet rs = null;
      String sql = "SELECT * FROM Users WHERE Username = ?;";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.setString(1, userName);
          rs = stmt.executeQuery();
          if (rs.next() == true) {
              user = new User(rs.getString("Username"), rs.getString("Password"),
                      rs.getString("Email"), rs.getString("FirstName"),
                      rs.getString("LastName"), rs.getString("Gender"),
                      rs.getString("PersonID"));
              return user;
          }
      } catch (SQLException e) {
          e.printStackTrace();
          throw new DataAccessException("Error encountered while finding user");
      }
      return null;
  }

    /**
     * Removes all users from database
     * @throws Exception
     */
  public void removeAllUsers() throws Exception {
      String sql = "Delete FROM Users;";
      try(PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.executeUpdate();
      } catch (SQLException e) {
          e.printStackTrace();
          throw new DataAccessException("Error encountered while deleting all persons");
      }
  }
}
