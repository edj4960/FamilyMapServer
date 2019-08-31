package cs240.evanjones.server.results;

import java.util.ArrayList;

import cs240.evanjones.server.model.Person;

/**Holds result of an Person Service*/
public class PersonResult
{
  /** Holds all persons that are related to this person */
  private ArrayList<Person> personData;
  /** Message if the service failed */
  private String message;

  /** Constructor for Person Result Object */
  public PersonResult() {}

  public ArrayList<Person> getPersonData() {
    return personData;
  }

  public void setPersonData(ArrayList<Person> personData) { this.personData = personData; }

  public String getMessage() { return message; }

  public void setMessage(String message) { this.message = message; }
}
