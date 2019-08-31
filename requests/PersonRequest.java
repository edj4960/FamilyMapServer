package cs240.evanjones.server.requests;

/** A request to find a single person or multiple persons */
public class PersonRequest {
  /** unique person ID */
  private String personID;
  /** authToken of user */
  private String authToken;

  /**Constructor of Person Request object*/
  public PersonRequest() {}

  public String getPersonID() { return personID; }

  public void setPersonID(String personID) { this.personID = personID; }

  public String getAuthToken() { return authToken; }

  public void setAuthToken(String authToken) { this.authToken = authToken; }
}
