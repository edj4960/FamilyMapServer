package cs240.evanjones.server.model;

/** An event connected to a Person */
public class Event
{
  /**Unique Event ID*/
  private String eventID;
  /**User to whom the Person belongs*/
  private String descendant;
  /**Person to whom the event belongs*/
  private String personID;
  /**Country where event occurred*/
  private String country;
  /**City where the event occurred*/
  private String city;
  /**Year when the event occurred*/
  private Integer year;
  /**longitude of the event*/
  private Double longitude;
  /**latitude of the event*/
  private Double latitude;
  /**Type of event*/
  private String eventType;

  /**
   * Creates a new Event object
   * @param eventID   eventID
   * @param personID  personID
   */
  public Event(String eventID, String descendant, String personID) {
    this.eventID = eventID;
    this.descendant = descendant;
    this.personID = personID;
  }

  public String getEventId() { return eventID; }

  public void setEventId(String e) { eventID = e; }

  public String getDescendant() { return descendant; }

  public void setDescendant(String d) {
    descendant = d;
  }

  public String getPersonId() {
    return personID;
  }

  public void setPersonId(String p) {
    personID = p;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String c) {
    country = c;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String c) {
    city = c;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer y) {
    year = y;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double l) {
    longitude = l;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double l) {
    latitude = l;
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String e) {
    eventType = e;
  }
}
