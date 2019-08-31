package cs240.evanjones.server.requests;

/**Request for an Event Service to find a single or multiple events*/
public class EventRequest {
  /** unique event ID */
  private String eventID;
  /** AuthToken of user submitting request */
  private String authToken;

  /**Constructor of Event Request object*/
  public EventRequest() {}

  public String getEventID() {
    return eventID;
  }

  public void setEventID(String eventID) {this.eventID = eventID;}

  public String getAuthToken() { return authToken;}

  public void setAuthToken(String authToken) {this.authToken = authToken;}
}
