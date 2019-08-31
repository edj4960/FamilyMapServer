package cs240.evanjones.server.requests;

/**A request to log in the user*/
public class LoginRequest {
  /** UserName (non-empty)*/
  private String userName;
  /** Password (non-empty)*/
  private String password;

  /** Constructor of login request */
  public LoginRequest(String userName, String password) {
    this.userName = userName;
    this.password = password;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) { this.userName = userName; }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) { this.password = password; }
}
