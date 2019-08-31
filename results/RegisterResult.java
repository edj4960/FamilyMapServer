package cs240.evanjones.server.results;

/**Holds result of an Register Service*/
public class RegisterResult
{
  /** Authorization Token (non-empty)*/
  private String authToken;
  /** Username passed in the request*/
  private String userName;
  /** Person ID of the user Person Object*/
  private String personID;
  /** Message to return*/
  private String message;

  /** Generates a RegisterResult Object */
  public RegisterResult() {}

  public void successResponse(String authToken, String userName, String personID) {
    this.authToken = authToken;
    this.userName = userName;
    this.personID = personID;
  }

  public String getAuthToken() {
    return authToken;
  }

  public void setAuthToken(String authToken) {this.authToken = authToken;}

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {this.userName = userName;}

  public String getPersonID() {
    return personID;
  }

  public void setPersonID(String personID) {this.personID = personID; }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {this.message = message;}
}
