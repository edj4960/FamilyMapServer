package cs240.evanjones.server.requests;

import java.util.ArrayList;

import cs240.evanjones.server.model.Event;
import cs240.evanjones.server.model.Person;
import cs240.evanjones.server.model.User;

/**A request to load multiple users, events, and persons*/
public class LoadRequest {
  /** User object array for request */
  private ArrayList<User> users;
  /** events object array for request */
  private ArrayList<Event> events;
  /** persons object array for request */
  private ArrayList<Person> persons;

  /** Constructor for a load request object */
  public LoadRequest() {}

  public void setUsers(ArrayList<User> users) {this.users = users;}

  public ArrayList<User> getUsers() {return users;}

  public void setEvents(ArrayList<Event> events) {this.events = events;}

  public ArrayList<Event> getEvents() {return events;}

  public void setPersons(ArrayList<Person> persons) {this.persons = persons;}

  public ArrayList<Person> getPersons() {return persons;}
}
