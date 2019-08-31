package cs240.evanjones.server.results;

import java.util.ArrayList;
import cs240.evanjones.server.model.Event;

/**Holds result of an Event Service*/
public class EventResult
{
  /** Holds all persons that are related to this person */
  private ArrayList<Event> eventData;
  /** Message if failed to retrieve event */
  private String message;

  /** Constructor for Event Result Object */
  public EventResult() {}

  public ArrayList<Event> getEventData() { return eventData; }

  public void setEventData(ArrayList<Event> eventData) {this.eventData = eventData;}

  public String getMessage() {return message;}

  public void setMessage(String message) { this.message = message; }
}
