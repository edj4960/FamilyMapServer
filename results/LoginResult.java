package cs240.evanjones.server.results;

/**Holds result of an Login Service*/
public class LoginResult
{
  /** authToken (non-empty) */
  private String authToken;
  /** User Name from request */
  private String userName;
  /** Person Id of Person Object */
  private String personID;
  /** message if failed */
  private String message;

  /** Constructor of Login Request object */
  public LoginResult() { }

  /**
  * @param authToken nonempty token String
  * @param userName Username from Request
  * @param personID ID of person
  */
  public void successResponse(String authToken, String userName, String personID) {
    this.authToken = authToken;
    this.userName = userName;
    this.personID = personID;
  }

  public String getMessage() { return message; }

  public void setMessage(String message) { this.message = message; }

  public String getAuthToken() { return authToken; }

  public void setAuthToken(String authToken) { this.authToken = authToken; }

  public String getUserName() { return userName; }

  public void setUserName(String userName) { this.userName = userName; }

  public String getPersonId() { return personID; }

  public void setPersonId(String personID) { this.personID = personID; }
}
