package cs240.evanjones.server.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

import cs240.evanjones.server.dataaccess.DataAccessException;
import cs240.evanjones.server.dataaccess.Database;
import cs240.evanjones.server.dataaccess.EventDAO;
import cs240.evanjones.server.dataaccess.PersonDAO;
import cs240.evanjones.server.dataaccess.UserDAO;
import cs240.evanjones.server.model.Event;
import cs240.evanjones.server.model.Person;
import cs240.evanjones.server.model.User;
import cs240.evanjones.server.results.FillResult;
import cs240.evanjones.server.requests.FillRequest;
import cs240.evanjones.server.util.Deserializer;

/** Service to fill the database */
public class FillService {
  // Files to use to Populate database
  private static String femaleNamesPath = "C:\\Users\\EvanJones\\MyFamilyMapServer\\server\\libs\\fakeData\\fnames.json";
  private static String maleNamesPath = "C:\\Users\\EvanJones\\MyFamilyMapServer\\server\\libs\\fakeData\\mnames.json";
  private static String surnamesPath = "C:\\Users\\EvanJones\\MyFamilyMapServer\\server\\libs\\fakeData\\snames.json";
  private static String locationsPath = "C:\\Users\\EvanJones\\MyFamilyMapServer\\server\\libs\\fakeData\\locations.json";
  private static String emailsPath = "C:\\Users\\EvanJones\\MyFamilyMapServer\\server\\libs\\fakeData\\emails.json";
  private static String passwordsPath = "C:\\Users\\EvanJones\\MyFamilyMapServer\\server\\libs\\fakeData\\passwords.json";
  private static String usernamesPath = "C:\\Users\\EvanJones\\MyFamilyMapServer\\server\\libs\\fakeData\\usernames.json";
  private static String personIDsPath = "C:\\Users\\EvanJones\\MyFamilyMapServer\\server\\libs\\fakeData\\personIDs.json";
  private static String eventIDsPath = "C:\\Users\\EvanJones\\MyFamilyMapServer\\server\\libs\\fakeData\\eventIDs.json";
  //Arrays to populateDatabase database
  private ArrayList<String> femaleNames;
  private ArrayList<String> maleNames;
  private ArrayList<String> surnames;
  private ArrayList<String> emails;
  private ArrayList<String> passwords;
  private ArrayList<String> usernames;
  //private ArrayList<String> personIDs;
  //private ArrayList<String> eventIDs;
  private HashMap<Integer, JsonObject> locations = new HashMap<>();
  // Request information
  private User user;
  private int generations = 4;

  /** Constructor for Fill Service object */
  public FillService() {}

  /**
   * Populates the servers database with generated data for a specified userName
   * @param request for population of database
   * @return FillResult object with success or failure message
   */
  public FillResult populateDatabase(FillRequest request) throws Exception {
    Database db = new Database();
    db.createTables();
    FillResult result = new FillResult();
    try {
      Connection conn = db.openConnection();
      UserDAO uDao = new UserDAO(conn);
      // Attempt to find user
      user = uDao.getUser(request.getUserName());
      if (user == null) {
        result.setMessage("Error: Invalid username");
        return result;
      }
      // Delete all user data
      EventDAO eDao = new EventDAO(conn);
      eDao.removeUserEvents(user.getUserName());
      PersonDAO pDao = new PersonDAO(conn);
      pDao.removeUserPersons(user.getUserName());
      // Insert Data from files into arrays
      femaleNames = jsonFileToArray(femaleNamesPath);
      maleNames = jsonFileToArray(maleNamesPath);
      surnames = jsonFileToArray(surnamesPath);
      emails = jsonFileToArray(emailsPath);
      passwords = jsonFileToArray(passwordsPath);
      usernames = jsonFileToArray(usernamesPath);
      //personIDs = jsonFileToArray(personIDsPath);
      //eventIDs = jsonFileToArray(eventIDsPath);
      // Insert data for locations
      JsonObject obj = Deserializer.deserializeFile(locationsPath);
      JsonArray jsonArray = obj.get("data").getAsJsonArray();
      Iterator iterator = jsonArray.iterator();
      ArrayList<String> arrayList = new ArrayList<>();

      Integer count = 0;
      while (iterator.hasNext()){
        JsonObject locationSet = (JsonObject) iterator.next();
        locations.put(count, locationSet);
        count++;
      }

      // Set generations
      if(request.getGenerations().intValue() != 0)
        generations = request.getGenerations();
      else
        generations = 4;
      // Create user person object
      Person person = new Person(user.getPersonID(), user.getUserName(),
                                 user.getFirstName(), user.getLastName(), user.getGender());
      String fatherID = UUID.randomUUID().toString();
      String motherID = UUID.randomUUID().toString();
      person.setFather(fatherID);
      person.setMother(motherID);
      pDao.addPerson(person);
      // Create user event
      int birthYear = 1996;
      Event birth = eventCreate(user.getUserName(), person.getPersonId(), "Birth", birthYear);
      eDao.addEvent(birth);
      // POPULATE DATABASE
      recursePopulate(0, user.getLastName(), user.getUserName(),
                       fatherID, motherID, conn, birthYear);

      db.closeConnection(true);
    } catch (DataAccessException e) {
      db.closeConnection(false);
    }
    // Calculate persons and events added
    int peopleAdded = 1;
    int increment = 1;
    for(int i=0; i<generations; i++) {
      increment = increment * 2;
      peopleAdded += increment;
    }
    // Three events per person so * 3, except for user who only has 1 so minus 2.
    int eventsAdded = (peopleAdded * 3) - 2;
    result.setMessage("Successfully added " + peopleAdded + " people and " +
            eventsAdded + " events to the database.");
    return result;
  }

  /**
   * Populates the database recursively based on given parameters
   * @param currentGen
   * @param surname
   * @param username
   * @param fatherID
   * @param motherID
   * @param conn
   * @param birthYear
   * @throws Exception
   */
  private void recursePopulate(int currentGen, String surname, String username, String fatherID,
                               String motherID, Connection conn, int birthYear) throws Exception {
    if(currentGen >= generations)
      return;
    // Update Variables
    currentGen++;
    birthYear -= 20;
    String fFatherID = UUID.randomUUID().toString();
    String mFatherID = UUID.randomUUID().toString();
    String fMotherID = UUID.randomUUID().toString();
    String mMotherID = UUID.randomUUID().toString();

    // Create Father and Mother
    String fatherName = assignRandomValue(maleNames);
    String motherName = assignRandomValue(femaleNames);
    Person father = new Person(fatherID, username, fatherName, surname, "m");
    Person mother = new Person(motherID, username, motherName, surname, "f");
    father.setSpouse(motherID);
    mother.setSpouse(fatherID);

    // Birth Event
    Event fatherBirth = eventCreate(username, fatherID, "Birth", birthYear);
    Event motherBirth = eventCreate(username, motherID, "Birth", birthYear);
    // Marriage Event
    int marriageYear = birthYear + 19;
    Event fatherMarriage = eventCreate(username, fatherID, "Marriage", marriageYear);
    Event motherMarriage = new Event(UUID.randomUUID().toString(), username, motherID);
    motherMarriage.setCountry(fatherMarriage.getCountry());
    motherMarriage.setCity(fatherMarriage.getCity());
    motherMarriage.setLatitude(fatherMarriage.getLatitude());
    motherMarriage.setLongitude(fatherMarriage.getLongitude());
    motherMarriage.setYear(marriageYear);
    motherMarriage.setEventType("Marriage");
    // Death Event
    int deathYear = marriageYear + 20;
    Event fatherDeath = eventCreate(username, fatherID, "Death", deathYear);
    Event motherDeath = eventCreate(username, motherID, "Death", deathYear);

    // Assign parents if another generation will occur
    if((currentGen + 1) <= generations) {
      father.setFather(fFatherID);
      father.setMother(fMotherID);
      mother.setFather(mFatherID);
      mother.setMother(mMotherID);
    }

    // Add to database
    PersonDAO personDAO = new PersonDAO(conn);
    EventDAO eventDAO = new EventDAO(conn);
    personDAO.addPerson(father);
    personDAO.addPerson(mother);
    eventDAO.addEvent(fatherBirth);
    eventDAO.addEvent(motherBirth);
    eventDAO.addEvent(fatherMarriage);
    eventDAO.addEvent(motherMarriage);
    eventDAO.addEvent(fatherDeath);
    eventDAO.addEvent(motherDeath);

    // Add next generation
    recursePopulate(currentGen, surname, username, fFatherID, fMotherID, conn, birthYear);
    recursePopulate(currentGen, assignRandomValue(surnames), username, mFatherID, mMotherID,
                    conn, birthYear);
  }

  /**
   * Grabs random value from array and then deletes that value from the array
   * @param array to grab random index
   * @return random value from given array
   */
  private String assignRandomValue(ArrayList<String> array) {
    Random randomGenerator = new Random();
    int index = randomGenerator.nextInt(array.size());
    String output = array.get(index);
    array.remove(index);
    return output;
  }

  /**
   * Creates event based on criteria
   * @param username
   * @param personID
   * @param eventType
   * @param year
   * @return created event
   */
  private Event eventCreate(String username, String personID, String eventType, int year) {
    Event event = new Event(UUID.randomUUID().toString(), username, personID);

    JsonObject obj = null;
    while(obj == null){
      Random randomGenerator = new Random();
      int index = randomGenerator.nextInt(locations.size());
      obj = locations.get(index);
      locations.remove(index);
    }

    event.setYear(year);
    event.setCountry(obj.get("country").getAsString().replaceAll("\"",""));
    event.setCity(obj.get("city").getAsString().replaceAll("\"",""));
    event.setLatitude(obj.get("latitude").getAsDouble());
    event.setLongitude(obj.get("longitude").getAsDouble());
    event.setEventType(eventType);
    return event;
  }

  /**
   * Extracts data from json file into an array
   * @param filePath
   * @return array of data in file
   * @throws Exception
   */
  private ArrayList<String> jsonFileToArray(String filePath) throws Exception {
    JsonObject obj = Deserializer.deserializeFile(filePath);
    JsonArray jsonArray = obj.get("data").getAsJsonArray();
    Iterator iterator = jsonArray.iterator();
    ArrayList<String> arrayList = new ArrayList<>();
    while (iterator.hasNext())
      arrayList.add(iterator.next().toString().replaceAll("\"",""));
    return arrayList;
  }
}